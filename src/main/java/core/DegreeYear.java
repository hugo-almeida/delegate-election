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
        // Be carefull not to add an active period here
        if (period.getStart().before(new Date())) {
            //TODO Throw Invalid Period
        }

        if (activePeriod != null && period.getStart().before(activePeriod.getEnd())) {
            //TODO Throw Invalid Period
        }

        // We should also check for conflicting periods
        for (final Period p : inactivePeriods) {
            if (period.conflictsWith(p)) {
                //TODO Throw Invalid Period
            }
        }

        inactivePeriods.add(period);
    }
}
