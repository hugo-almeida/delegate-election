package core;

import java.util.Arrays;
import java.util.Set;

import org.springframework.web.client.RestTemplate;

public class Calendar {
    private int year;
    private Set<Degree> degrees;

    public int getYear() {
        return year;
    }

    public Calendar(int year) {
        this.year = year;
        initDegrees();
    }

    private void initDegrees() {
        final RestTemplate t = new RestTemplate();
        final Degree[] c = t.getForObject("https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees", Degree[].class);
        degrees.addAll(Arrays.asList(c));
    }

    public void setYear(int year) {
        this.year = year;
    }

}
