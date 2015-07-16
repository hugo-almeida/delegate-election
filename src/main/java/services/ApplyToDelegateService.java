package services;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import core.ApplicationPeriod;
import core.Period;
import core.Student;
import core.StudentDAO;

public class ApplyToDelegateService implements DelegatesService {
    @Autowired
    StudentDAO studentDAO;

    private final String applicantId;

    public ApplyToDelegateService(String applicantId) {
        this.applicantId = applicantId;
    }

    @Override
    public String execute() {
        final Student applicant = studentDAO.findByUsername(applicantId);
        final Period period = applicant.getDegreeYear().getActivePeriod();
        try {
            final ApplicationPeriod aPeriod = (ApplicationPeriod) period;
            aPeriod.addCandidate(applicant);
        } catch (final ClassCastException e) {
            // Se o cast não foi possivel, o periodo actual nao e de condidatura
            final Gson gson = new Gson();
            return gson.toJson("fail");
        }
        final Gson gson = new Gson();
        return gson.toJson("success");
    }

}
