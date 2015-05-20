package core;

import java.util.Set;

public class Calendar {
    private int year;
    private Set<Course> courses;

    public int getYear() {
        return year;
    }

    public Calendar(int year) {
        this.year = year;
        initCourses();
    }

    private void initCourses() {
        // TODO Get all Courses from Fenix
        // TODO Add all Courses to the Calendar
    }

    public void setYear(int year) {
        this.year = year;
    }

}
