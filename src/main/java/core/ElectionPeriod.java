package core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ElectionPeriod extends Period {

    private final Map<Integer, Vote> votes;

    public ElectionPeriod(Date start, Date end) {
        super(start, end);
        votes = new HashMap<Integer, Vote>();
    }

    public void vote(int voter, int voted) {
        votes.put(voter, new Vote(voter, voted));
    }
}
