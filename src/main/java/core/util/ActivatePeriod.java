package core.util;

import java.util.TimerTask;

import core.CourseYear;
import core.Period;

public class ActivatePeriod extends TimerTask {

    private final CourseYear courseYear;
    private final Period period;

    public ActivatePeriod(CourseYear courseYear, Period period) {
        super();
        this.courseYear = courseYear;
        this.period = period;
    }

    @Override
    public void run() {
        courseYear.setActivePeriod(period);
    }

}
