package endpoint;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.ApplicationPeriod;
import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.DegreeDAO;
import core.DegreeYear;
import core.ElectionPeriod;
import core.Period;
import core.PeriodDAO;
import core.Student;
import core.StudentDAO;
import core.util.ActivatePeriod;
import core.util.RetrieveStudentListTask;

@RestController
public class TestController {
    @Autowired
    CalendarDAO cd;

    @Autowired
    StudentDAO st;

    @Autowired
    DegreeDAO dd;

    @Autowired
    PeriodDAO pd;

    @RequestMapping("/sanity")
    public String test() {
        return "It's working!";
    }

    @RequestMapping("/test-calendar")
    public String testCalendar() {
        final Calendar c = new Calendar(2014);
        c.init();
//        Calendar c2 = new Calendar(2015);
//        c2.init();
        cd.save(c);
//        cd.save(c2);
        return "Done";
    }

    @RequestMapping("/timer-period")
    public String timerPeriod() {
//        dd.findByIdAndYear("2761663971465", cd.findFirstByOrderByYearDesc().getYear()).addYear(5);
        Period p = pd.findById(3);
//                new ApplicationPeriod(LocalDate.of(2015, 7, 22), LocalDate.of(2015, 7, 23), dd.findByIdAndYear("2761663971465",
//                        cd.findFirstByOrderByYearDesc().getYear()).getDegreeYear(5));
        TimerTask timerTask = new ActivatePeriod(p, pd);
        pd.save(p);
        Timer timer = new Timer(true);
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 10));
        timer.schedule(timerTask, tomorrow);
        System.out.println("Scheduled for time " + tomorrow.toString());
        return "Ok";
    }

    @RequestMapping("/timer")
    public String timer() {
        dd.findByIdAndYear("2761663971465", cd.findFirstByOrderByYearDesc().getYear()).addYear(5);
        TimerTask timerTask =
                new RetrieveStudentListTask(dd.findByIdAndYear("2761663971465", cd.findFirstByOrderByYearDesc().getYear())
                        .getDegreeYear(5), dd);
        Timer timer = new Timer(true);
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 10));
        timer.schedule(timerTask, tomorrow);
        System.out.println("Scheduled for time " + tomorrow.toString());
        return "Ok";
    }

    @RequestMapping("/application")
    public String application() {
        final Calendar testCalendar = cd.findByYear(2014);
        for (final Degree d : testCalendar.getDegrees()) {
            for (final DegreeYear dy : d.getYears()) {
                if (dy.getDegreeYear() == 2) {
                    final ApplicationPeriod p =
                            new ApplicationPeriod(LocalDate.of(2015, Month.NOVEMBER, 14), LocalDate.of(2015, Month.NOVEMBER, 15),
                                    dy);
                    dy.addPeriod(p);
                    dy.setActivePeriod(p);
                    cd.save(testCalendar);
                    return "Ok";
                }
            }
        }
        return "No ok";
    }

    @RequestMapping("/election")
    public String election() {
        final Calendar testCalendar = cd.findByYear(2014);
        for (final Degree d : testCalendar.getDegrees()) {
            for (final DegreeYear dy : d.getYears()) {
                if (dy.getDegreeYear() == 2) {
                    final ElectionPeriod p =
                            new ElectionPeriod(LocalDate.of(2015, Month.NOVEMBER, 16), LocalDate.of(2015, Month.NOVEMBER, 17), dy);
                    dy.addPeriod(p);
                    dy.setActivePeriod(p);
                    cd.save(testCalendar);
                    return "Ok";
                }
            }
        }
        return "No ok";
    }

    /* @RequestMapping("/make-application")
     public String makeApplication() {
         Calendar testCalendar = cd.findByYear(2014);
         for (Degree d : testCalendar.getDegrees()) {
             for (DegreeYear dy : d.getYears()) {
                 if (dy.getDegreeYear() == 1) {
                     ApplicationPeriod p =
                             new ApplicationPeriod(LocalDate.of(2015, Month.NOVEMBER, 14), LocalDate.of(2015, Month.NOVEMBER, 15),
                                     dy);
                     try {
                         dy.addPeriod(p);
                     } catch (InvalidPeriodException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     dy.setActivePeriod(p);
                     Student s2 = null;
                     for (Student s : dy.getStudents()) {
                         p.addCandidate(s);
                         s2 = s;
                     }
                     cd.save(testCalendar);
                     p.removeCandidates(s2);
                     cd.save(testCalendar);
                     return "ok";
                 }
             }
         }
         cd.save(testCalendar);
         return "ok";
     }

     @RequestMapping("/make-election")
     public String stuff() {
         Calendar testCalendar = cd.findByYear(2014);
         for (Degree d : testCalendar.getDegrees()) {
             for (DegreeYear dy : d.getYears()) {
                 if (dy.getDegreeYear() == 1) {
                     ElectionPeriod p =
                             new ElectionPeriod(LocalDate.of(2015, Month.NOVEMBER, 5), LocalDate.of(2015, Month.NOVEMBER, 6), dy);
                     try {
                         dy.addPeriod(p);
                     } catch (InvalidPeriodException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                     dy.setActivePeriod(p);
                     cd.save(testCalendar);
                     Iterator<Student> it = dy.getStudents().iterator();
                     Student s = it.next();
                     Student s2 = it.next();
                     Student s3 = it.next();
                     //Vote v1 = new Vote(s.getUsername(), s.getUsername(), dy.getActivePeriod());
                     //vd.save(v1);
                     ElectionPeriod p2 = (ElectionPeriod) dy.getActivePeriod();
                     p2.vote(s, s);
                     cd.save(testCalendar);
                     p2.vote(s2, s);
                     cd.save(testCalendar);
                     p2.vote(s3, s2);
                     cd.save(testCalendar);
                     return "Oki";
                 }
             }
         }
         cd.save(testCalendar);
         return "ok";
     }*/

    @RequestMapping("/votes")
    public int votes() {
        final Calendar testCalendar = cd.findByYear(2014);
        for (final Degree d : testCalendar.getDegrees()) {
            for (final DegreeYear dy : d.getYears()) {
                if (dy.getDegreeYear() == 1) {
                    final ElectionPeriod p = (ElectionPeriod) dy.getActivePeriod();
                    return p.getVotes().size();
                }
            }
        }
        return 999;
    }

    @RequestMapping("/find-student")
    public String findStudent(@RequestParam(value = "username", required = true) String username) {
//        Degree d = dd.findById("2761663971606");
//        DegreeYear degreeYear = null;
//        for (DegreeYear dy : d.getYears()) {
//            for (Student s : dy.getStudents()) {
//                if (s.getUsername().equals(username)) {
//                    degreeYear = dy;
//                    break;
//                }
//
//            }
//            if (degreeYear != null) {
//                break;
//            }
//        }
        final Student s = st.findByUsernameAndDegreeAndCalendarYear(username, "2761663971606", 2014);
        return s.getName();
    }

    @RequestMapping("/test-calendar2")
    public int testCalendar2() {
        return cd.findFirstByOrderByYearDesc().getYear();
    }

    @RequestMapping("/add-years")
    public String year() {
        final Calendar c = cd.findByYear(2015);
        for (final Degree d : c.getDegrees()) {
            d.initDegreeYears();
        }
        cd.save(c);
        return "Ok";
    }
}
