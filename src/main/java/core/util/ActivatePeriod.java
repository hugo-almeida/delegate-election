package core.util;

import java.util.TimerTask;

import core.DegreeYear;
import core.Period;

public class ActivatePeriod extends TimerTask {

    private final DegreeYear courseYear;
    private final Period period;

    public ActivatePeriod(DegreeYear courseYear, Period period) {
        super();
        this.courseYear = courseYear;
        this.period = period;
    }

    @Override
    public void run() {
        courseYear.setActivePeriod(period);
    }

}
