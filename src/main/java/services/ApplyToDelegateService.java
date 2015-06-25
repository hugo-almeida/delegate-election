package services;

import org.springframework.beans.factory.annotation.Autowired;

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
        Student applicant = studentDAO.findByUsername(applicantId);
        Period period = applicant.getDegreeYear().getActivePeriod();
        try {
            ApplicationPeriod aPeriod = (ApplicationPeriod) period;
            aPeriod.addCandidate(applicant);
        } catch (ClassCastException e) {
            // Se o cast não foi possivel, o periodo actual nao e de condidatura
        }
        return null;
    }

}
