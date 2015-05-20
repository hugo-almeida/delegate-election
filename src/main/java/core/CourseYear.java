package core;

import java.util.Set;

public class CourseYear {
    private final int year;
    private Period activePeriod;
    private Set<Period> inactivePeriods;
    private Set<Student> studends;

    public CourseYear(int year) {
        this.year = year;
    }

    public int getCourseYear() {
        return year;
    }

    public Period getActivePeriod() {
        return activePeriod;
    }

    public Set<Period> getInactivePeriods() {
        return inactivePeriods;
    }

    public Set<Student> getStudents() {
        return studends;
    }
}
