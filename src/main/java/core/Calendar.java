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
        Set<Degree> toRemove = new HashSet<Degree>();
        for (Degree element : c) {
            if (element.getType().equals("Licenciatura Bolonha") || element.getType().equals("Mestrado Bolonha")
                    || element.getType().equals("Mestrado Integrado")) {
                element.setCalendar(this);
                element.setKey();
                degrees.add(element);
            }
        }
//        degrees.iterator().next().initDegreeYears();
//        Degree d = null;
//        Iterator<Degree> it = degrees.iterator();
//        do {
//            d = it.next();
//        } while (!d.getAcronym().equals("MEIC-A"));
//        d.initDegreeYears();
        for (Degree d : degrees) {
            if (d.getAcronym().equals("MSCIT")) {
                System.out.println("Found it!");
                d.initDegreeYears();
                break;
            }
        }
    }

    public void addDegree(Degree d) {
        degrees.add(d);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

}
