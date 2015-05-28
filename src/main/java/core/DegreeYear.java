package core;

import java.util.Date;
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

@Entity
@Table(name = "DegreeYear")
public class DegreeYear {

    @EmbeddedId
    private DegreeDegreeYearPK degreeDegreeYearPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "CalendarDegreePK_DegreeName", insertable = false, updatable = false),
            @JoinColumn(name = "CalendarDegreePK_CalendarYear", insertable = false, updatable = false) })
    private Degree degree;

    private int year;

    @OneToOne(mappedBy = "activeDegreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Period activePeriod;

    @OneToMany(mappedBy = "degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Period> inactivePeriods;

    @OneToMany(mappedBy = "degreeYear", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Student> students;

    DegreeYear() {

    }

    public DegreeYear(int year, Degree d) {
        this.degreeDegreeYearPK = new DegreeDegreeYearPK(d.getName(), year, d.getYear());
        this.degree = d;
        this.year = year;
        initPeriod();
    }

    private void initPeriod() {
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

    public void addPeriod(Period period) {
        if (period.getStart().before(new Date())) {
            //TODO Throw Invalid Period Exception - The start date should be in the future
        }

        if (activePeriod != null && period.getStart().before(activePeriod.getEnd())) {
            //TODO Throw Invalid Period Exception - A period can't start before the active period ends
        }

        for (Period p : inactivePeriods) {
            if (period.conflictsWith(p)) {
                //TODO Throw Invalid Period Exception - There should not be overlapping periods
            }
        }

        inactivePeriods.add(period);
    }
}
