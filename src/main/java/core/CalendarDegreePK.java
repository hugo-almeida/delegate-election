package core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CalendarDegreePK implements Serializable {

    @Column(name = "CalendarDegreePK_DegreeName")
    private String degreeName;

    @Column(name = "CalendarDegreePK_CalendarYear")
    private int calendarYear;

    CalendarDegreePK() {

    }

    public CalendarDegreePK(String name, int year) {
        this.degreeName = name;
        this.calendarYear = year;
    }

    public String getName() {
        return degreeName;
    }

    public int getCalendarYear() {
        return calendarYear;
    }
}
