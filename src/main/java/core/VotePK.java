package core;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VotePK {

    @Column(name = "voter")
    private final String voter;
    @Column(name = "voted")
    private final String voted;

    public VotePK(String voter, String voted) {
        this.voter = voter;
        this.voted = voted;
    }

    public String getVoter() {
        return voter;
    }

    public String getVoted() {
        return voted;
    }
}
