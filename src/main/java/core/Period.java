package core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import core.util.ActivatePeriod;
import core.util.DeactivatePeriod;
import core.util.RetrieveStudentListTask;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "period_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "period")
public abstract class Period implements Serializable {

    public enum PeriodType {
        Application {
            @Override
            public String toString() {
                return "APPLICATION";
            }
        },
        Election {
            @Override
            public String toString() {
                return "ELECTION";
            }
        }
    }

    @Id
    @GeneratedValue
    @Column(name = "period_id")
    private int id;

    @Column(name = "degree_name")
    private String degreeName;

    @Column(name = "degree_year")
    private int degree_Year;

    @Column(name = "calendar_year")
    private int calendarYear;

    @Column(name = "start")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate start;

    @Column(name = "end")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate end;

    @ManyToMany(mappedBy = "applicationPeriod", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected Set<Student> candidates;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "degree_name", referencedColumnName = "degree_year_pk_degree_name", insertable = false,
                    updatable = false),
            @JoinColumn(name = "degree_year", referencedColumnName = "degree_year_pk_degree_year", insertable = false,
                    updatable = false),
            @JoinColumn(name = "calendar_year", referencedColumnName = "degree_year_pk_calendar_year", insertable = false,
                    updatable = false) })
    private DegreeYear degreeYear;

    @Column(name = "active")
    private boolean active;

    @Transient
    private Timer timer;

    Period() {

    }

    // This constructor is only for temporary Periods.
    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public Period(LocalDate start, LocalDate end, DegreeYear degreeYear) {
        if (end.isBefore(start) || start.isBefore(LocalDate.now())) {
        }
        //this.periodPK = new PeriodPK(degreeYear.getDegreeName(), degreeYear.getDegreeYear(), degreeYear.getCalendarYear());
        this.start = start;
        this.end = end;
        this.degreeName = degreeYear.getDegreeName();
        this.degree_Year = degreeYear.getDegreeYear();
        this.calendarYear = degreeYear.getCalendarYear();
        this.degreeYear = degreeYear;
        this.active = false;
        this.candidates = new HashSet<Student>();
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

    public boolean conflictsWith(LocalDate otherStart, LocalDate otherEnd) {
        if (end.isBefore(otherStart) || start.isAfter(otherEnd)) {
            return false;
        }
        return true;
    }

    public DegreeYear getDegreeYear() {
        return degreeYear;
    }

    public void setDegreeYear(DegreeYear degreeYear) {
        this.degreeYear = degreeYear;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public Set<Student> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(Set<Student> candidates) {
        this.candidates = candidates;
        for (Student s : candidates) {
            s.addPeriod(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Period)) {
            return false;
        }
        Period p = null;
        if (o instanceof ApplicationPeriod) {
            p = (ApplicationPeriod) o;
        } else {
            p = (ElectionPeriod) o;
        }
//        if (this.degree_Year == p.degree_Year && this.calendarYear == p.calendarYear && this.degreeName.equals(p.degreeName)
//                && this.start.equals(p.start) && this.end.equals(p.end)) {
//            return true;
//        } else {
//            return false;
//        }
        return p.getId() == getId();
    }

    @Override
    public int hashCode() {
//        int year = this.degree_Year;
//        int calendar = this.calendarYear;
//        int degreeName = this.degreeName.hashCode();
//        int start = this.start.hashCode();
//        int end = this.end.hashCode();
//        return year * calendar * degreeName * start * end;
        return getId();
        //Forcing equals to be called
//        return 0;
    }

    public int getId() {
        return id;
    }

    abstract public PeriodType getType();

    public void unschedulePeriod(PeriodDAO periodDAO, DegreeDAO degreeDAO) {
        timer.cancel();
    }

    public void schedulePeriod(PeriodDAO periodDAO, DegreeDAO degreeDAO) {
        timer = new Timer(true);

        schedulePeriodStart(periodDAO, degreeDAO);
        schedulePeriodEnd(periodDAO);
    }

    public void schedulePeriodStart(PeriodDAO periodDAO, DegreeDAO degreeDAO) {
        if (timer == null) {
            timer = new Timer(true);
        }

        TimerTask retrieveStudentsTask = new RetrieveStudentListTask(getDegreeYear(), degreeDAO);
        TimerTask activatePeriodTask = new ActivatePeriod(this, periodDAO);

        timer.schedule(retrieveStudentsTask, Date.from(getStart().atStartOfDay().minusHours(1).toInstant(ZoneOffset.UTC))); //Vai buscar os alunos 1 hora antes
        timer.schedule(activatePeriodTask, Date.from(getStart().atStartOfDay().toInstant(ZoneOffset.UTC)));
    }

    public void schedulePeriodEnd(PeriodDAO periodDAO) {
        if (timer == null) {
            timer = new Timer(true);
        }

        TimerTask deactivatePeriodTask = new DeactivatePeriod(this, periodDAO);
        timer.schedule(deactivatePeriodTask,
                Date.from(getEnd().plusDays(1).atStartOfDay().minusMinutes(1).toInstant(ZoneOffset.UTC))); //termina às 23:59 do dia de fim
    }
}
