package core;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "period_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "period")
public abstract class Period {

    @EmbeddedId
    @Column(name = "period_id")
    private PeriodPK periodPK;

    @Column(name = "start")
    private LocalDate start;
    @Column(name = "end")
    private LocalDate end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "degree_name", referencedColumnName = "degree_year_pk_degree_name", insertable = false,
                    updatable = false),
            @JoinColumn(name = "degree_year", referencedColumnName = "degree_year_pk_degree_year", insertable = false,
                    updatable = false),
            @JoinColumn(name = "calendar_year", referencedColumnName = "degree_year_pk_calendar_year", insertable = false,
                    updatable = false) })
    private DegreeYear degreeYear;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "active_degree_name", referencedColumnName = "degree_year_pk_degree_name", insertable = false,
                    updatable = false),
            @JoinColumn(name = "active_degree_year", referencedColumnName = "degree_year_pk_degree_year", insertable = false,
                    updatable = false),
            @JoinColumn(name = "active_calendar_year", referencedColumnName = "degree_year_pk_calendar_year", insertable = false,
                    updatable = false) })
    private DegreeYear activeDegreeYear;

    Period() {

    }

    public Period(LocalDate start, LocalDate end, DegreeYear degreeYear) {
        if (end.isBefore(start)) {
            //Throws a nice exception saying that nothing ends before starting
        }
        this.periodPK = new PeriodPK(degreeYear.getDegreeName(), degreeYear.getDegreeYear(), degreeYear.getCalendarYear());
        this.start = start;
        this.end = end;
        this.degreeYear = degreeYear;
        this.activeDegreeYear = degreeYear;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public boolean conflictsWith(Period p) {
        if (end.isBefore(p.getStart()) || start.isAfter(p.getEnd())) {
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
