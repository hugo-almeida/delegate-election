package core.util;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.DegreeDAO;
import core.DegreeYear;
import core.Period;
import core.PeriodDAO;
import core.Student;

@Component
public class ScheduledTasks {

    @Autowired
    DegreeDAO degreeDAO;

    @Autowired
    CalendarDAO calendarDAO;

    @Autowired
    PeriodDAO periodDAO;

    // Isto assume que a app está sempre a correr.

//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateCurrentPeriods() {
        // Obtem calendario actual
        Calendar calendar = calendarDAO.findFirstByOrderByYearDesc();
        if (calendar == null) {
            return;
        }

        Set<Degree> degrees = calendar.getDegrees();
        if (degrees == null) {
            return;
        }

        for (final Degree degree : degrees) {
            for (final DegreeYear degreeYear : degree.getYears()) {
                try {
                    Set<Student> candidates = null;
                    // Em cada degreeYear, verifica se o currentPeriod ja terminou
                    Period activePeriod = degreeYear.getActivePeriod();
                    if (activePeriod != null) {
                        if (activePeriod.getEnd().isBefore(LocalDate.now())) {
                            // Se terminou, tira esse de activo
                            activePeriod.setInactive();
                            candidates = activePeriod.getCandidates();
                            periodDAO.save(activePeriod);
                        } else {
                            // Se nao terminou, continua para o proximo degreeYear
                            continue;
                        }
                    }
                    // Depois verifica se há algum para entrar em vigor no dia actual, caso haja, coloca-o como activo
                    Period newActivePeriod = degreeYear.getNextPeriod(LocalDate.now());
                    if (newActivePeriod != null && newActivePeriod.getStart().isEqual(LocalDate.now())) {
                        degreeYear.setActivePeriod(newActivePeriod);
                        degreeYear.setStudentsLoaded(true);
                        if (candidates != null) {
                            newActivePeriod.setCandidates(candidates);
                        } else {
                            Period lastPeriod = degreeYear.getLastPeriod(LocalDate.now());
                            if (lastPeriod != null && lastPeriod.getCandidates() != null) {
                                newActivePeriod.setCandidates(lastPeriod.getCandidates());
                            }
                        }
                        periodDAO.save(newActivePeriod);
                    }
                } catch (Exception e) {
                    // Devia guardar excepcao no log
                    // Desta forma, ainda que o processamento de um ano/curso falhe, apenas esse fica por processar
                }
            }
        }
    }

    @Scheduled(cron = "0 30 0 * * *")
    @Transactional
    public void retrieveStudents() throws Exception {
        Calendar calendar = calendarDAO.findFirstByOrderByYearDesc();
        if (calendar == null) {
            return;
        }

        Set<Degree> degrees = calendar.getDegrees();
        if (degrees == null) {
            return;
        }

        for (Degree degree : degrees) {
            for (DegreeYear degreeYear : degree.getYears()) {
                if (!degreeYear.areStudentsLoaded()) {
                    degreeYear.initStudents();
                    //degreeDAO.save(degreeYear.getDegree());
                }
            }
        }
        degreeDAO.save(degrees);

    }

    @Scheduled(cron = "0 59 23 31 8 *")
    public void createCalendar() throws Exception {
        final Calendar calendar = new Calendar(LocalDate.now().getYear());
        calendar.init();
        calendarDAO.save(calendar);
    }
}
