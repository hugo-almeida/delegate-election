package endpoint;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
        final Calendar c = new Calendar(2014);
        c.init();
//        Calendar c2 = new Calendar(2015);
//        c2.init();
        cd.save(c);
//        cd.save(c2);
        return "Done";
    }

    @RequestMapping("/test-periods")
    public String periodSetup() {
        final Calendar testCalendar = cd.findByYear(2014);

        for (final Degree d : testCalendar.getDegrees()) {
            for (final DegreeYear dy : d.getYears()) {
                try {
                    if (dy.getDegreeYear() % 2 == 0) {
                        //add ongoing application period
                        final Period period =
                                new ApplicationPeriod(LocalDate.of(2015, Month.JULY, 2), LocalDate.of(2015, Month.JULY, 30), dy);
                        dy.addPeriod(period);
                        dy.setActivePeriod(period);
                    } else {
                        // add application period in the past
                        dy.addPeriod(new ApplicationPeriod(LocalDate.of(2015, Month.JUNE, 2), LocalDate.of(2015, Month.JUNE, 30),
                                dy));

                        // add candiLocalDates
                        int i = 0;
                        for (final Student s : dy.getStudents()) {
                            s.apply();
                            if (i++ > 2) {
                                break;
                            }
                        }

                        // add ongoing election period
//                        dy.addPeriod(new ElectionPeriod(LocalDate.of(2015, Month.JULY, 2), LocalDate.of(2015, Month.JULY, 30), dy));
                    }
                } catch (final InvalidPeriodException e) {
                    System.out.println("You're dumb " + e);
                }
            }
        }
        cd.save(testCalendar);
        return "ok";
    }

    @RequestMapping("/find-student")
    public String findStudent(@RequestParam(value = "username", required = true) String username) {
        final Student s = st.findByUsername(username);
        return s.getName();
    }

    @RequestMapping("/test-calendar2")
    public int testCalendar2() {
        final Calendar c = cd.findByYear(2014);
        return c.getDegrees().size();
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

    @RequestMapping("/test-pedagogico")
    public ModelAndView testpedagogico() {
        final List<String> l = new ArrayList<String>();
        l.add("ist167066");
        final OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        final Map<String, Object> userDetails = (Map<String, Object>) auth.getUserAuthentication().getDetails();
        if (l.contains(userDetails.get("username"))) {
            return new ModelAndView("redirect:/pedagogico.html");
        } else {
            return new ModelAndView();
        }
    }
}
