package core;

public class Vote {

    private final int voter;
    private final int voted;

    public Vote(int voter, int voted) {
        this.voter = voter;
        this.voted = voted;
    }

    public int getVoter() {
        return voter;
    }

    public int getVoted() {
        return voted;
    }

}
