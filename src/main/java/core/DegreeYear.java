package core;

import java.util.Date;
import java.util.Set;

import core.exception.InvalidPeriodException;

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

    public void addPeriod(Period period) throws InvalidPeriodException {
        if (period.getStart().before(new Date())) {
            throw new InvalidPeriodException("The start date should be in the future");
        }

        if (activePeriod != null && period.getStart().before(activePeriod.getEnd())) {
            throw new InvalidPeriodException("A period can't start before the active period ends");
        }

        for (final Period p : inactivePeriods) {
            if (period.conflictsWith(p)) {
                throw new InvalidPeriodException("There should not be overlapping periods");
            }
        }

        inactivePeriods.add(period);
    }
}
