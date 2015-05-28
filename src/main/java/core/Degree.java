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
    private Set<DegreeYear> years;

    private String name;
    private String acronym;
    private String id;
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
        initDegreeYears();
    }

    private void initDegreeYears() {
        if (type.equals("BOLONHA_MASTER_DEGREE")) {
            for (int i = 0; i < 2; i++) {
                years.add(new DegreeYear(i, this));
            }
        } else if (type.equals("BOLONHA_DEGREE")) {
            for (int i = 0; i < 3; i++) {
                years.add(new DegreeYear(i, this));
            }
        } else if (type.equals("BOLONHA_INTEGRATED_MASTER_DEGREE")) {
            for (int i = 0; i < 5; i++) {
                years.add(new DegreeYear(i, this));
            }
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

}
