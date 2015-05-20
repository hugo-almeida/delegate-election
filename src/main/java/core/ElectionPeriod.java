package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionPeriod extends Period {

    private final Map<Integer, List<Vote>> votes;

    public ElectionPeriod(Date start, Date end) {
        super(start, end);
        votes = new HashMap<Integer, List<Vote>>();
    }

    public void vote(int voter, int voted) {
        final Vote v = new Vote(voter, voted);
        if (votes.containsKey(voted)) {
            votes.get(voted).add(v);
        } else {
            votes.put(voted, new ArrayList<Vote>());
            votes.get(voted).add(v);
        }
    }
}
