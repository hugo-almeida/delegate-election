package core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("Application")
public class ApplicationPeriod extends Period {

    @OneToMany(mappedBy = "applicationPeriod", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Student> candidates;

    ApplicationPeriod() {
        super();
    }

    public ApplicationPeriod(Date start, Date end, DegreeYear degreeYear) {
        super(start, end, degreeYear);
        candidates = new HashSet<Student>();
    }

    public void addCandidate(Student s) {
        candidates.add(s);
        s.apply();
    }
}
