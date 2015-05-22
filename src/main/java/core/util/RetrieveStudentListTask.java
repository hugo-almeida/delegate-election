package core.util;

import java.util.TimerTask;

import core.DegreeYear;

public class RetrieveStudentListTask extends TimerTask {

    private final DegreeYear courseYear;

    public RetrieveStudentListTask(DegreeYear year) {
        courseYear = year;
    }

    @Override
    public void run() {
        // Get all eletable students from fenix
        // And put them in the courseyear
        // This should be ran 5/10 minutes before the period becomes active
    }

}
