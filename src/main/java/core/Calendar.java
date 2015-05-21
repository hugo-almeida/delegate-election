package core;

import java.util.Arrays;
import java.util.Set;

import org.springframework.web.client.RestTemplate;

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
        RestTemplate t = new RestTemplate();
        Course[] c = t.getForObject("https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees", Course[].class);
        courses.addAll(Arrays.asList(c));
    }

    public void setYear(int year) {
        this.year = year;
    }

}
