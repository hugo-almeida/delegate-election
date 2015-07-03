package endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.Student;
import core.StudentDAO;

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
