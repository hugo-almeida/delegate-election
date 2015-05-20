package core;

import java.util.Set;

public class Course {
    private final String name;
    private Set<CourseYear> years;

    public Course(String name) {
        this.name = name;
        initCourseYears();
    }

    private void initCourseYears() {
        // TODO Get the amount of years of each Course from Fenix
        // TODO Create all the years for this Course.
    }

    public String getName() {
        return name;
    }
}
