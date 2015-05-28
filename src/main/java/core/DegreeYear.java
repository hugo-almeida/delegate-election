package core;

import java.util.Date;
import java.util.Set;

public class DegreeYear {
    private final int year;
    private Period activePeriod;
    private Set<Period> inactivePeriods;
    private Set<Student> students;

    public DegreeYear(int year) {
        this.year = year;
    }

    public int getDegreeYear() {
        return year;
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

<<<<<<< HEAD:delegados/src/main/java/core/DegreeYear.java
        // We should also check for conflicting periods
        for (final Period p : inactivePeriods) {
=======
        for (Period p : inactivePeriods) {
>>>>>>> StudentFactory's constructor is now private.:delegados/src/main/java/core/CourseYear.java
            if (period.conflictsWith(p)) {
                //TODO Throw Invalid Period Exception - There should not be overlapping periods
            }
        }

        inactivePeriods.add(period);
    }
}
