package core.util;

import java.util.TimerTask;

import core.CourseYear;

public class RetrieveStudentListTask extends TimerTask {

    private final CourseYear courseYear;

    public RetrieveStudentListTask(CourseYear year) {
        courseYear = year;
    }

    @Override
    public void run() {
        // Get all eletable students from fenix
        // And put them in the courseyear
        // This should be ran 5/10 minutes before the period becomes active
    }

}
