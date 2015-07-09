package core;

import java.time.LocalDate;
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

    public ApplicationPeriod(LocalDate start, LocalDate end, DegreeYear degreeYear) {
        super(start, end, degreeYear);
        candidates = new HashSet<Student>();
    }

    public void addCandidate(Student s) {
        candidates.add(s);
        s.apply();
    }

    public int getCandidateCount() {
        return candidates.size();
    }

    @Override
    public PeriodType getType() {
        return PeriodType.Application;
    }
}
