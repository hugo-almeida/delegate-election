package services;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

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
        final Student voter = studentDAO.findByUsername(voterId);
        final Student voted = studentDAO.findByUsername(votedId);
        final Period period = voter.getDegreeYear().getActivePeriod();
        try {
            final ElectionPeriod ePeriod = (ElectionPeriod) period;
            ePeriod.vote(voter, voted);
        } catch (final ClassCastException e) {
            // Se o cast não foi possivel, o periodo actual nao e de eleicao
            final Gson gson = new Gson();
            return gson.toJson("fail");
        }

        final Gson gson = new Gson();
        return gson.toJson("success");
    }
}
