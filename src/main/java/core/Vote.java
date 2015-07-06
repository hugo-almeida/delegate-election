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
    @JoinColumns({ @JoinColumn(name = "degree_name", referencedColumnName = "degree_name", insertable = true, updatable = false),
            @JoinColumn(name = "degree_year", referencedColumnName = "degree_year", insertable = true, updatable = false),
            @JoinColumn(name = "calendar_year", referencedColumnName = "calendar_year", insertable = true, updatable = false),
            @JoinColumn(name = "period_pk_id", referencedColumnName = "period_pk_id", insertable = true, updatable = false),
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
