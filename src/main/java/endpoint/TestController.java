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
import core.ElectionPeriod;
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

    @RequestMapping("/make-application")
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
                    Period p =
                            new ElectionPeriod(LocalDate.of(2015, Month.NOVEMBER, 12), LocalDate.of(2015, Month.NOVEMBER, 13), dy);
                    try {
                        dy.addPeriod(p);
                    } catch (InvalidPeriodException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    dy.setActivePeriod(p);
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
