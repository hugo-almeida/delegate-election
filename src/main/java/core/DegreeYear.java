package core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.Period.PeriodType;
import core.exception.InvalidPeriodException;

@Entity
@Table(name = "DegreeYear")
public class DegreeYear {

    @EmbeddedId
    private DegreeDegreeYearPK degreeDegreeYearPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "degree_name", referencedColumnName = "degree_pk_degree_name", insertable = true,
                    updatable = false),
            @JoinColumn(name = "calendar_year", referencedColumnName = "degree_pk_calendar_year", insertable = true,
                    updatable = false) })
    private Degree degree;

    @OneToMany(mappedBy = "degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Period> periods = new HashSet<Period>();

    @OneToMany(mappedBy = "degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<Student>();

    DegreeYear() {

    }

    public DegreeYear(int year, Degree d) {
        this.degreeDegreeYearPK = new DegreeDegreeYearPK(d.getName(), year, d.getYear());
        this.degree = d;
        //this.year = year;
        initPeriod();
        initStudents();
    }

    public void initStudents() {
        final String accessToken =
                "ODUxOTE1MzUzMDk2MTkzOjg1NDJmMDMwN2Y5ZDZiZWY4NTQxZThhM2NlMzkyZjQwYzE3MzNmOWM0NzJlYzM4NDM2ZjJlZjFkYzMyNjM2ZTc2ZDkxNTdlNjZmNjM4OGUzMGMxYTU4ZTk5YzYzNWFiMDMxN2RhOTA2MWI0MDExN2Y3NTAwNGRmMTFlOTk5N2Q0";

        RestTemplate t = new RestTemplate();
        Student[] degreeYearStudents =
                t.getForObject("https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees/" + degree.getId()
                        + "/students?curricularYear=" + getDegreeYear() + "&access_token=" + accessToken, Student[].class);

        String infoUrl = "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/person?access_token=" + accessToken;

        HttpHeaders requestHeaders = new HttpHeaders();
        for (Student student : degreeYearStudents) {
            student.setDegreeYear(this);

            requestHeaders.set("__username__", student.getUsername());
            HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);

            HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.GET, requestEntity, String.class);
            JsonObject result = new JsonParser().parse(response.getBody()).getAsJsonObject();
            if (!result.get("email").isJsonNull()) {
                student.setEmail(result.get("email").toString());
            }
            if (!result.get("photo").isJsonNull()) {
                student.setPhotoType(result.getAsJsonObject("photo").get("type").toString());
                student.setPhotoBytes(result.getAsJsonObject("photo").get("data").toString());
            }
        }
        students.addAll(Arrays.asList(degreeYearStudents));

    }

    public void initPeriod() {
        // TODO Auto-generated method stub

    }

    public int getDegreeYear() {
        return degreeDegreeYearPK.getDegreeYear();
    }

    public String getDegreeName() {
        return degree.getName();
    }

    public int getCalendarYear() {
        return degreeDegreeYearPK.getCalendarYear();
    }

    public Period getActivePeriod() {
        for (Period p : periods) {
            if (p.isActive()) {
                return p;
            }
        }
        return null;
    }

    public ApplicationPeriod getCurrentApplicationPeriod() {
        ApplicationPeriod period = null;
        for (Period p : periods) {
            if (p.getType().equals(PeriodType.Application) && (period == null || p.getStart().isAfter(period.getStart()))) {
                period = (ApplicationPeriod) p;
            }
        }
        return period;
    }

    public ElectionPeriod getCurrentElectionPeriod() {
        ElectionPeriod period = null;
        for (Period p : periods) {
            if (p.getType().equals(PeriodType.Election) && (period == null || p.getStart().isAfter(period.getStart()))) {
                period = (ElectionPeriod) p;
            }
        }
        return period;
    }

    public Set<Period> getInactivePeriods() {
        Set<Period> inactive = new HashSet<Period>();
        for (Period p : periods) {
            if (!p.isActive()) {
                inactive.add(p);
            }
        }
        return inactive;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setActivePeriod(Period period) {
        if (periods.contains(period)) {
            if (getActivePeriod() != null) {
                getActivePeriod().setInactive();
            }
            period.setActive();
        }
    }

    public Set<Student> getCandidates() {
        return getActivePeriod().getCandidates();
    }

    public void addPeriod(Period period) throws InvalidPeriodException {
        if (getActivePeriod() != null && period.getStart().isBefore(getActivePeriod().getEnd())) {
            throw new InvalidPeriodException("A period can't start before the active period ends");
        }

        for (final Period p : periods) {
            if (period.conflictsWith(p)) {
                throw new InvalidPeriodException("There should not be overlapping periods");
            }
        }

        periods.add(period);
    }
}
