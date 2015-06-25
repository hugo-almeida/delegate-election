package services;

import org.springframework.beans.factory.annotation.Autowired;

import core.ElectionPeriod;
import core.Period;
import core.Student;
import core.StudentDAO;

public class VoteService implements DelegatesService {
    @Autowired
    StudentDAO studentDAO;

    private final String voterId;
    private final String votedId;

    public VoteService(String voterId, String votedId) {
        this.voterId = voterId;
        this.votedId = votedId;
    }

    @Override
    public String execute() {
        Student voter = studentDAO.findByUsername(voterId);
        Student voted = studentDAO.findByUsername(votedId);
        Period period = voter.getDegreeYear().getActivePeriod();
        try {
            ElectionPeriod ePeriod = (ElectionPeriod) period;
            ePeriod.vote(voter, voted);
        } catch (ClassCastException e) {
            // Se o cast não foi possivel, o periodo actual nao e de eleicao
        }
        return null;
    }
}
