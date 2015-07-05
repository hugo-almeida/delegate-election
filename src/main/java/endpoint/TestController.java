package endpoint;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.ApplicationPeriod;
import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.DegreeYear;
import core.Period;
import core.Student;
import core.StudentDAO;
import core.exception.InvalidPeriodException;

@RestController
public class TestController {
    @Autowired
    CalendarDAO cd;
    @Autowired
    StudentDAO st;

    @RequestMapping("/sanity")
    public String test() {
        return "It's working!";
    }

    @RequestMapping("/test-calendar")
    public String testCalendar() {
        Calendar c = new Calendar(2014);
        c.init();
//        Calendar c2 = new Calendar(2015);
//        c2.init();
        cd.save(c);
//        cd.save(c2);
        return "Done";
    }

    @RequestMapping("/test-periods")
    public String periodSetup() {
        Calendar testCalendar = cd.findByYear(2014);

        for (Degree d : testCalendar.getDegrees()) {
            for (DegreeYear dy : d.getYears()) {
                try {
                    if (dy.getDegreeYear() % 2 == 0) {
                        //add ongoing application period
                        Period period =
                                new ApplicationPeriod(LocalDate.of(2015, Month.JULY, 2), LocalDate.of(2015, Month.JULY, 30), dy);
                        dy.addPeriod(period);
                        dy.setActivePeriod(period);
                    } else {
                        // add application period in the past
                        dy.addPeriod(new ApplicationPeriod(LocalDate.of(2015, Month.JUNE, 2), LocalDate.of(2015, Month.JUNE, 30),
                                dy));

                        // add candiLocalDates
                        int i = 0;
                        for (Student s : dy.getStudents()) {
                            s.apply();
                            if (i++ > 2) {
                                break;
                            }
                        }

                        // add ongoing election period
//                        dy.addPeriod(new ElectionPeriod(LocalDate.of(2015, Month.JULY, 2), LocalDate.of(2015, Month.JULY, 30), dy));
                    }
                } catch (InvalidPeriodException e) {
                    System.out.println("You're dumb " + e);
                }
            }
        }
        cd.save(testCalendar);
        return "ok";
    }

    @RequestMapping("/find-student")
    public String findStudent(@RequestParam(value = "username", required = true) String username) {
        Student s = st.findByUsername(username);
        return s.getName();
    }

    @RequestMapping("/test-calendar2")
    public int testCalendar2() {
        Calendar c = cd.findByYear(2014);
        return c.getDegrees().size();
    }

    @RequestMapping("/add-years")
    public String year() {
        Calendar c = cd.findByYear(2015);
        for (Degree d : c.getDegrees()) {
            d.initDegreeYears();
        }
        cd.save(c);
        return "Ok";
    }
}
