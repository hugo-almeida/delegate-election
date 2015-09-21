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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.client.RestTemplate;

import endpoint.AccessTokenHandler;

@Entity
@Table(name = "calendar")
@Configurable
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

    public void init() throws Exception {
        final RestTemplate t = new RestTemplate();
        //ath = AccessTokenHandler.getInstance();
        final Degree[] c = AccessTokenHandler.getInstance().getDegrees();

        for (final Degree element : c) {
            if (element.getType().equals(Degree.DegreeType.Bachelor.toString())
                    || element.getType().equals(Degree.DegreeType.Integrated.toString())
                    || element.getType().equals(Degree.DegreeType.Master.toString())) {
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

    public void setYear(int year) {
        this.year = year;
    }

}
