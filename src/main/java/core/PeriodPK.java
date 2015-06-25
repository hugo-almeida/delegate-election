package core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Embeddable
public class PeriodPK implements Serializable {

    @Column(name = "degree_name")
    private String degreeName;

    @Column(name = "degree_year")
    private int degreeYear;

    @Column(name = "calendar_year")
    private int calendarYear;

    @Column(name = "peroid_pk_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    PeriodPK() {
    }

    public PeriodPK(String degreeName, int degreeYear, int calendarYear) {
        this.degreeName = degreeName;
        this.degreeYear = degreeYear;
        this.calendarYear = calendarYear;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public int getDegreeYear() {
        return degreeYear;
    }

    public int getCalendarYear() {
        return calendarYear;
    }
}
