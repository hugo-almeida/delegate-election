package core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ApplicationPeriod extends Period {

    private final Set<Student> candidates;

    public ApplicationPeriod(Date start, Date end) {
        super(start, end);
        candidates = new HashSet<Student>();
    }

    public void addCandidate(Student s) {
        candidates.add(s);
        s.apply();
    }
}
