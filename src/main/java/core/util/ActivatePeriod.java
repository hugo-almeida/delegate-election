package core.util;

import java.util.TimerTask;

import core.DegreeYear;
import core.Period;
import core.PeriodDAO;

public class ActivatePeriod extends TimerTask {

    private final DegreeYear degreeYear;
    private final Period period;
    private final PeriodDAO periodDAO;

    public ActivatePeriod(Period period, PeriodDAO dao) {
        super();
        this.degreeYear = period.getDegreeYear();
        this.period = period;
        periodDAO = dao;
    }

    @Override
    public void run() {
        degreeYear.setActivePeriod(period);
        periodDAO.save(period);
    }
}
