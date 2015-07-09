package core;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("Election")
public class ElectionPeriod extends Period {

    @OneToMany(mappedBy = "electionPeriod", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@MapKey(name = "username")
    private Map<String, VoteHolder> votes;

    ElectionPeriod() {
    }

    public ElectionPeriod(LocalDate start, LocalDate end, DegreeYear degreeYear) {
        super(start, end, degreeYear);
        votes = new HashMap<String, VoteHolder>();
    }

    public void vote(Student voter, Student voted) {
        if (votes.containsKey(voted.getUsername())) {
            votes.get(voted.getUsername()).addVote(voter.getUsername());
        } else {
            VoteHolder vh = new VoteHolder(this, voted.getUsername());
            votes.put(voted.getUsername(), vh);
            votes.get(voted.getUsername()).addVote(voter.getUsername());
        }
        voter.vote();
    }

    // Get the vote from a given student
    public Vote getVote(String s) {
        // Workaround
        return null;
    }

    public Set<VoteHolder> getVotes() {
        return new HashSet<VoteHolder>(votes.values());
    }

    @Override
    public PeriodType getType() {
        return PeriodType.Election;
    }
}
