package core.util;

import java.util.TimerTask;

import core.Period;
import dao.PeriodDAO;

public class DeactivatePeriod extends TimerTask {

    private final Period period;
    private final PeriodDAO periodDAO;

    public DeactivatePeriod(Period period, PeriodDAO dao) {
        super();
        this.period = period;
        periodDAO = dao;
    }

    @Override
    public void run() {
        period.setInactive();
        periodDAO.save(period);
    }
}
