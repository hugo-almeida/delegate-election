package core.util;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    // TODO Excepções não devem impedir a continuação do método

//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
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

                    if (candidates != null) {
                        newActivePeriod.setCandidates(candidates);
                    } else {
                        Period lastPeriod = degreeYear.getLastPeriod(LocalDate.now());
                        if (lastPeriod != null) {
                            newActivePeriod.setCandidates(lastPeriod.getCandidates());
                        }
                    }

                    periodDAO.save(newActivePeriod);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void retrieveStudents() {
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
                Period activePeriod = degreeYear.getActivePeriod();
                if (activePeriod != null && !degreeYear.areStudentsLoaded()) {
                    degreeYear.initStudents();
                    degreeDAO.save(degreeYear.getDegree());
                }
            }
        }

    }

    @Scheduled(cron = "0 59 23 31 8 *")
    public void createCalendar() {
        final Calendar calendar = new Calendar(LocalDate.now().getYear());
        calendar.init();
        calendarDAO.save(calendar);
    }
}
