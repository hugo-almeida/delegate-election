package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Entity
//@Table(name = "ElectionPeriod")
public class ElectionPeriod extends Period {

    private final Map<String, List<Vote>> votes;

    public ElectionPeriod(Date start, Date end, DegreeYear degreeYear) {
        super(start, end, degreeYear);
        votes = new HashMap<String, List<Vote>>();
    }

    public void vote(Student voter, Student voted) {
        final Vote v = new Vote(voter.getUsername(), voted.getUsername());
        if (votes.containsKey(voted.getUsername())) {
            votes.get(voted.getUsername()).add(v);
        } else {
            votes.put(voted.getUsername(), new ArrayList<Vote>());
            votes.get(voted.getUsername()).add(v);
        }
        voter.vote();
    }
}
