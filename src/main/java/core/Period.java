package core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Inheritance
@DiscriminatorColumn(name = "Period_Type")
@Table(name = "Period")
public abstract class Period {

    @EmbeddedId
    private PeriodPK periodPK;

    @Column(name = "Period_Start")
    private Date start;
    @Column(name = "Period_End")
    private Date end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "DegreeDegreeYearPK_DegreeName", insertable = false, updatable = false),
            @JoinColumn(name = "DegreeYearPK_DegreeYear", insertable = false, updatable = false),
            @JoinColumn(name = "DegreeYearPK_CalendarYear", insertable = false, updatable = false) })
    private DegreeYear degreeYear;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "DegreeDegreeYearPK_DegreeName", insertable = false, updatable = false),
            @JoinColumn(name = "DegreeYearPK_DegreeYear", insertable = false, updatable = false),
            @JoinColumn(name = "DegreeYearPK_CalendarYear", insertable = false, updatable = false) })
    private DegreeYear activeDegreeYear;

    Period() {

    }

    public Period(Date start, Date end, DegreeYear degreeYear) {
        if (end.before(start)) {
            //Throws a nice exception saying that nothing ends before starting
        }
        this.periodPK = new PeriodPK(degreeYear.getDegreeName(), degreeYear.getDegreeYear(), degreeYear.getCalendarYear());
        this.start = start;
        this.end = end;
        this.degreeYear = degreeYear;
        this.activeDegreeYear = degreeYear;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean conflictsWith(Period p) {
        if (end.before(p.getStart()) || start.after(p.getEnd())) {
            return false;
        }
        return true;
    }

    public DegreeYear getDegreeYear() {
        return degreeYear;
    }

    public void setDegreeYear(DegreeYear degreeYear) {
        this.degreeYear = degreeYear;
        this.activeDegreeYear = degreeYear;
    }
}
