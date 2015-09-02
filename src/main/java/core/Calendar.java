package core;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.web.client.RestTemplate;

@Entity
@Table(name = "calendar")
public class Calendar {

    @Id
    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Degree> degrees = new HashSet<Degree>();

    public int getYear() {
        return year;
    }

    Calendar() {
    }

    public Calendar(int year) {
        this.year = year;
        //initDegrees();
    }

    public void init() {
        final RestTemplate t = new RestTemplate();
        final Degree[] c = t.getForObject("https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees", Degree[].class);

        for (final Degree element : c) {
            if (element.getType().equals("Licenciatura Bolonha") || element.getType().equals("Mestrado Bolonha")
                    || element.getType().equals("Mestrado Integrado")) {
                element.setCalendar(this);
                element.setKey();
                degrees.add(element);
            }
        }

        for (final Degree d : degrees) {
            d.initDegreeYears();
        }
    }

    public void addDegree(Degree degree) {
        for (Degree d : degrees) {
            if (d.getId().equals(degree.getId())) {
                return;
            }
        }
        degrees.add(degree);
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

}
