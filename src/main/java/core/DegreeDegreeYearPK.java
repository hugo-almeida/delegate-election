package core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DegreeDegreeYearPK implements Serializable {

    @Column(name = "DegreeDegreeYearPK_DegreeName")
    private String degreeName;

    @Column(name = "DegreeDegreeYearPK_DegreeYear")
    private int degreeYear;

    @Column(name = "DegreeDegreeYearPK_CalendarYear")
    private int calendarYear;

    DegreeDegreeYearPK() {

    }

    public DegreeDegreeYearPK(String degreeName, int degreeYear, int calendarYear) {
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
