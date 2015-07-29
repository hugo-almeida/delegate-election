package core;

import java.time.LocalDate;
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

    @OneToMany(mappedBy = "studentpk.degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<Student>();

    DegreeYear() {

    }

    public DegreeYear(int year, Degree d) {
        this.degreeDegreeYearPK = new DegreeDegreeYearPK(d.getName(), year, d.getYear());
        this.degree = d;
        initStudents(); // Development only.
    }

    public void initStudents() {
        final String accessToken =
                "ODUxOTE1MzUzMDk2MTkzOjg1NDJmMDMwN2Y5ZDZiZWY4NTQxZThhM2NlMzkyZjQwYzE3MzNmOWM0NzJlYzM4NDM2ZjJlZjFkYzMyNjM2ZTc2ZDkxNTdlNjZmNjM4OGUzMGMxYTU4ZTk5YzYzNWFiMDMxN2RhOTA2MWI0MDExN2Y3NTAwNGRmMTFlOTk5N2Q0";

        final RestTemplate t = new RestTemplate();
        final Student[] degreeYearStudents =
                t.getForObject("https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees/" + degree.getId()
                        + "/students?curricularYear=" + getDegreeYear() + "&access_token=" + accessToken, Student[].class);

        final String infoUrl = "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/person?access_token=" + accessToken;

        final HttpHeaders requestHeaders = new HttpHeaders();
        for (final Student student : degreeYearStudents) {
            student.setDegreeYear(this);

            requestHeaders.set("__username__", student.getUsername());
            final HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);

            final HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.GET, requestEntity, String.class);
            final JsonObject result = new JsonParser().parse(response.getBody()).getAsJsonObject();
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

    public int getDegreeYear() {
        return degreeDegreeYearPK.getDegreeYear();
    }

    public String getDegreeName() {
        return degree.getName();
    }

    public int getCalendarYear() {
        return degreeDegreeYearPK.getCalendarYear();
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

    public Set<Student> getCandidates() {
        final Period activePeriod = getActivePeriod();
        if (activePeriod != null) {
            return activePeriod.getCandidates();
        }

        return new HashSet<Student>();
    }

    public void setActivePeriod(Period period) {
        if (periods.contains(period)) {
            if (getActivePeriod() != null) {
                getActivePeriod().setInactive();
            }
            period.setActive();
        }
    }

    public void addPeriod(Period period) {
        if (getActivePeriod() != null && period.getStart().isBefore(getActivePeriod().getEnd())) {
            return;
        }

        for (final Period p : periods) {
            if (period.conflictsWith(p)) {
                return;
            }
        }

        periods.add(period);
    }

    public Set<Period> getInactivePeriods() {
        final Set<Period> inactive = new HashSet<Period>();
        for (final Period p : periods) {
            if (!p.isActive()) {
                inactive.add(p);
            }
        }
        return inactive;
    }

    public Period getActivePeriod() {
        for (final Period p : periods) {
            if (p.isActive()) {
                return p;
            }
        }
        return null;
    }

    public ApplicationPeriod getCurrentApplicationPeriod() {
        ApplicationPeriod period = null;
        for (final Period p : periods) {
            if (p.getType().equals(PeriodType.Application) && (period == null || p.getStart().isAfter(period.getStart()))) {
                period = (ApplicationPeriod) p;
            }
        }
        return period;
    }

    public ElectionPeriod getCurrentElectionPeriod() {
        ElectionPeriod period = null;
        for (final Period p : periods) {
            if (p.getType().equals(PeriodType.Election) && (period == null || p.getStart().isAfter(period.getStart()))) {
                period = (ElectionPeriod) p;
            }
        }
        return period;
    }

    public boolean setDate(LocalDate start, LocalDate end, PeriodType periodType) {
        final LocalDate now = LocalDate.now();
        Period newPeriod = null;
        //Changing the past is impossible, the end can't happen before the start
        if (end.isBefore(now) || end.isBefore(start)) {
            return true;
        }
        //Change cannot conflict with more than one period
        for (final Period p : periods) {
            //No conflict
            if (p.getEnd().isBefore(start) || p.getStart().isAfter(end)) {
                continue;
            } else {
                //Two conflicts - do nothing
                if (newPeriod != null) {
                    return true;
                } else {
                    newPeriod = p;
                }
            }
        }

        if (newPeriod == null) {
            return false;
        }
        //Types should match
        if (newPeriod.getType() != periodType) {
            return true;
        }
        if (newPeriod.getStart().isAfter(now) && start.isAfter(now)) {
            newPeriod.setStart(start);
        }
        if (newPeriod.getEnd().isAfter(now) && end.isAfter(now) && end.isAfter(newPeriod.getStart())) {
            newPeriod.setEnd(end);
        }
        return true;
    }

    public Period getPeriodActiveOnDate(LocalDate date) {
        for (final Period p : periods) {
            if (!p.getStart().isAfter(date) && !p.getEnd().isBefore(date)) {
                return p;
            }
        }
        return null;
    }

    public boolean hasPeriodBetweenDates(LocalDate first, LocalDate second, Period period) {
        for (final Period p : periods) {
            if (p.getId() == period.getId()) {
                continue;
            }
            if (p.conflictsWith(first, second)) {
                return true;
            }
        }
        return false;
    }

    public Period addPeriod(LocalDate start, LocalDate end, String periodType) {
        if (start.isAfter(end)) {
            return null;
        }
        if (start.isBefore(LocalDate.now()) || end.isBefore(LocalDate.now())) {
            return null;
        }
        if (getActivePeriod() != null && start.isBefore(getActivePeriod().getEnd())) {
            return null;
        }

        for (final Period p : periods) {
            if (p.conflictsWith(start, end)) {
                return null;
            }
        }
        Period p = null;
        if (periodType.equals(PeriodType.Application.toString())) {
            p = new ApplicationPeriod(start, end, this);
            periods.add(p);
        }
        if (periodType.equals(PeriodType.Election.toString())) {
            p = new ElectionPeriod(start, end, this);
            periods.add(p);
        }
        return p;
    }

    public String getType() {
        return degree.getType();
    }

    public Period getLastPeriod(LocalDate now) {
        Period last = null;
        for (Period p : periods) {
            if (p.getEnd().isBefore(now)) {
                if (last == null) {
                    last = p;
                } else if (p.getEnd().isAfter(last.getEnd())) {
                    last = p;
                }
            }
        }
        return last;
    }
}
