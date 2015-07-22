package core.util;

import java.util.TimerTask;

import core.DegreeDAO;
import core.DegreeYear;

public class RetrieveStudentListTask extends TimerTask {

    private final DegreeYear degreeYear;

    DegreeDAO degreeDAO;

    public RetrieveStudentListTask(DegreeYear year, DegreeDAO dao) {
        degreeYear = year;
        degreeDAO = dao;
    }

    @Override
    public void run() {
        // Get all eletable students from fenix
        // And put them in the courseyear
        // This should be ran 5/10 minutes before the period becomes active
        degreeYear.initStudents();
        degreeDAO.save(degreeYear.getDegree());
    }

}
