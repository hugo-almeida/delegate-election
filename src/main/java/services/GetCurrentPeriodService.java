package services;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import core.Period;
import core.Student;
import core.StudentDAO;

public class GetCurrentPeriodService implements DelegatesService {

    @Autowired
    StudentDAO studentDAO;

    String studentId;

    public GetCurrentPeriodService(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String execute() {
        Student student = studentDAO.findByUsername(studentId);
        Period period = student.getDegreeYear().getActivePeriod();

        Gson gson = new Gson();
        return gson.toJson(period);
    }
}
