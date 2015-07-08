package core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Embeddable
public class PeriodPK implements Serializable {

    @Column(name = "degree_name")
    private String degreeName;

    @Column(name = "degree_year")
    private int degreeYear;

    @Column(name = "calendar_year")
    private int calendarYear;

    @Column(name = "period_pk_id")
    @SequenceGenerator(name = "periodSeq", sequenceName = "PERIOD_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "periodSeq")
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

    public int getId() {
        return id;
    }
}
