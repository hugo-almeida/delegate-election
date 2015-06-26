package services;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import core.DegreeYear;
import core.Student;
import core.StudentDAO;

public class GetCandidatesFromDegreeYear implements DelegatesService {

    @Autowired
    StudentDAO studentDAO;

    private final String username;

    public GetCandidatesFromDegreeYear(String username) {
        this.username = username;
    }

    @Override
    public String execute() {
        Student s = studentDAO.findByUsername(username);
        DegreeYear dy = s.getDegreeYear();
        Gson g = new Gson();
        return g.toJson(dy.getCandidates());

    }

}
