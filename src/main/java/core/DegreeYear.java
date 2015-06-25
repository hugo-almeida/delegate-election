package core;

import java.util.Arrays;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.exception.InvalidPeriodException;

@Entity
@Table(name = "DegreeYear")
public class DegreeYear {

    @EmbeddedId
    private DegreeDegreeYearPK degreeDegreeYearPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "CalendarDegreePK_DegreeName", insertable = true, updatable = false),
            @JoinColumn(name = "CalendarDegreePK_CalendarYear", insertable = true, updatable = false) })
    private Degree degree;

    //private int year;

    @OneToOne(mappedBy = "activeDegreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Period activePeriod;

    @OneToMany(mappedBy = "degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Period> inactivePeriods;

    @OneToMany(mappedBy = "degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<Student>();

    DegreeYear() {

    }

    public DegreeYear(int year, Degree d) {
        this.degreeDegreeYearPK = new DegreeDegreeYearPK(d.getName(), year, d.getYear());
        this.degree = d;
        //this.year = year;
        //initPeriod();
        //initStudents();
    }

    public void initStudents() {
        RestTemplate t = new RestTemplate();
        //final ApplicationConfiguration config = ApplicationConfiguration.fromPropertyFilename("/fenixedu.properties");
        //FenixEduClientImpl client = new FenixEduClientImpl(config);
        Student[] s =
                t.getForObject(
                        "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees/"
                                + degree.getId()
                                + "/students?curricularYear="
                                + getDegreeYear()
                                + "&access_token=ODUxOTE1MzUzMDk2MTkzOjg1NDJmMDMwN2Y5ZDZiZWY4NTQxZThhM2NlMzkyZjQwYzE3MzNmOWM0NzJlYzM4NDM2ZjJlZjFkYzMyNjM2ZTc2ZDkxNTdlNjZmNjM4OGUzMGMxYTU4ZTk5YzYzNWFiMDMxN2RhOTA2MWI0MDExN2Y3NTAwNGRmMTFlOTk5N2Q0",
                        Student[].class);
        String url =
                "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/person?access_token=ODUxOTE1MzUzMDk2MTkzOjg1NDJmMDMwN2Y5ZDZiZWY4NTQxZThhM2NlMzkyZjQwYzE3MzNmOWM0NzJlYzM4NDM2ZjJlZjFkYzMyNjM2ZTc2ZDkxNTdlNjZmNjM4OGUzMGMxYTU4ZTk5YzYzNWFiMDMxN2RhOTA2MWI0MDExN2Y3NTAwNGRmMTFlOTk5N2Q0";
        HttpHeaders headers = new HttpHeaders();
        for (Student st : s) {
            st.setDegreeYear(this);
            headers.set("__username__", st.getUsername());
            HttpEntity entity = new HttpEntity(headers);
            HttpEntity<String> response = t.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject res = new JsonParser().parse(response.getBody()).getAsJsonObject();
            if (!res.get("email").isJsonNull()) {
                st.setEmail(res.get("email").toString());
            }
            if (!res.get("photo").isJsonNull()) {
                st.setPhotoType(res.getAsJsonObject("photo").get("type").toString());
                st.setPhotoBytes(res.getAsJsonObject("photo").get("data").toString());
            }
        }
        students.addAll(Arrays.asList(s));

    }

    public void initPeriod() {
        // TODO Auto-generated method stub

    }

    public int getDegreeYear() {
        return degreeDegreeYearPK.getDegreeYear();
    }

    public String getDegreeName() {
        return degreeDegreeYearPK.getDegreeName();
    }

    public int getCalendarYear() {
        return degreeDegreeYearPK.getCalendarYear();
    }

    public Period getActivePeriod() {
        return activePeriod;
    }

    public Set<Period> getInactivePeriods() {
        return inactivePeriods;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setActivePeriod(Period period) {
        if (activePeriod != null) {
            inactivePeriods.add(activePeriod);
        }
        inactivePeriods.remove(period);
        activePeriod = period;
    }

    public void addPeriod(Period period) throws InvalidPeriodException {
        if (period.getStart().before(new Date())) {
            throw new InvalidPeriodException("The start date should be in the future");
        }

        if (activePeriod != null && period.getStart().before(activePeriod.getEnd())) {
            throw new InvalidPeriodException("A period can't start before the active period ends");
        }

        for (final Period p : inactivePeriods) {
            if (period.conflictsWith(p)) {
                throw new InvalidPeriodException("There should not be overlapping periods");
            }
        }

        inactivePeriods.add(period);
    }
}
