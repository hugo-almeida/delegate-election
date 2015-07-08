package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class VoteHolder implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "period_id", insertable = true, updatable = false)
    private ElectionPeriod electionPeriod;

    @Id
    private String voted;

    @OneToMany(mappedBy = "voteHolder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Vote> votes = new ArrayList<Vote>();

    public VoteHolder() {

    }

    public VoteHolder(ElectionPeriod ep, String voted) {
        this.electionPeriod = ep;
        this.voted = voted;
    }

    public ElectionPeriod getElectionPeriod() {
        return electionPeriod;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void addVote(String voter) {
        Vote v = new Vote(voter, this);
        votes.add(v);
    }
}
