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
        // TODO Auto-generated method stub

    }

    public String getName() {
        return name;
    }
}
