package core;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Degree")
public class Degree {

    @EmbeddedId
    @Column(name = "Course_CalendarDegreePK")
    private CalendarDegreePK calendarDegreePK;

    @ManyToOne(fetch = FetchType.LAZY)
    //  @Column(name = "Course_Calendar")
    @JoinColumn(name = "Calendar_Year")
    private Calendar calendar;

    @OneToMany(mappedBy = "degree", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<DegreeYear> years = new HashSet<DegreeYear>();

    private String name;
    private String id;
    private String acronym;
    private String type;

    Degree() {
    }

    public Degree(String name, String acronym, String id, String type, Calendar c) {
        this.calendarDegreePK = new CalendarDegreePK(name, c.getYear());
        this.calendar = c;
        this.name = name;
        this.acronym = acronym;
        this.id = id;
        this.type = type;
        this.years = new HashSet<DegreeYear>();
        //initDegreeYears();
    }

    public void setCalendar(Calendar c) {
        this.calendar = c;
    }

    public void setKey() {
        this.calendarDegreePK = new CalendarDegreePK(acronym, calendar.getYear());
    }

    public void initDegreeYears() {
        if (type.equals("Mestrado Bolonha")) {
            for (int i = 1; i <= 2; i++) {
                years.add(new DegreeYear(i, this));
            }
        } else if (type.equals("Licenciatura Bolonha")) {
            for (int i = 1; i <= 3; i++) {
                years.add(new DegreeYear(i, this));
            }
        } else if (type.equals("Mestrado Integrado")) {
            for (int i = 1; i <= 5; i++) {
                years.add(new DegreeYear(i, this));
            }
        }
        for (DegreeYear y : years) {
            y.initStudents();
        }
    }

    public String getName() {
        return calendarDegreePK.getName();
    }

    public int getYear() {
        return calendar.getYear();
    }

    public String getAcronym() {
        return acronym;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Set<DegreeYear> getYears() {
        return years;
    }

    public void addYear(int i) {
        years.add(new DegreeYear(i, this));
    }

    public String print() {
        return "Name: " + name + " Type: " + type + " Teste Integrado: " + type.equals("Mestrado Integrado");
    }

}
