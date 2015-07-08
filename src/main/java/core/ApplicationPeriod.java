package core;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Application")
public class ApplicationPeriod extends Period {

    ApplicationPeriod() {
        super();
    }

    public ApplicationPeriod(LocalDate start, LocalDate end, DegreeYear degreeYear) {
        super(start, end, degreeYear);
        //candidates = new HashSet<Student>();
    }

    public void addCandidate(Student s) {
        super.getCandidates().add(s);
        s.addPeriod(this);
    }

    //@PreRemove
    public void removeCandidates(Student s) {
        if (super.getCandidates().contains(s)) {
            super.getCandidates().remove(s);
        }
        s.removePeriod(this);
    }

    @Override
    public PeriodType getType() {
        return PeriodType.Application;
    }

//    @Override
//    public boolean equals(Object o) {
//        return super.equals(o);
//    }
}
