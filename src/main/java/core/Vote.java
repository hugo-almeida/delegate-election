package core;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vote")
public class Vote implements Serializable {

    @Id
    private String voter;

//    @Id
//    @JoinColumn(name = "voted", referencedColumnName = "username")
//    private String voted;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "period_id", referencedColumnName = "period_id", insertable = true, updatable = false),
            @JoinColumn(name = "voted", referencedColumnName = "voted", insertable = true, updatable = false) })
    private VoteHolder voteHolder;

    Vote() {
    }

    public Vote(String voter, VoteHolder vh) {
        this.voter = voter;
        this.voteHolder = vh;
    }

//    public String getVoter() {
//        return votePK.getVoter();
//    }
//
//    public String getVoted() {
//        return votePK.getVoted();
//    }
//
//    public VoteHolder getVoteHolder() {
//        return voteHolder;
//    }

}
