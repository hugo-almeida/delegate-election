package core;

public class Vote {

    private final String voter;
    private final String voted;

    public Vote(String voter, String voted) {
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
