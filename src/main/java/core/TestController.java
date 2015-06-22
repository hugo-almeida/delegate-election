package core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TestController {
    @Autowired
    CalendarDAO cd;

    @RequestMapping("/sanity")
    public String test() {
        return "It's working!";
    }

    @RequestMapping("/test-calendar")
    public String testCalendar() {
        Calendar c = new Calendar(2014);
        Calendar c2 = new Calendar(2015);
        cd.save(c);
        cd.save(c2);
        return "Done";
    }

    @RequestMapping("/test-calendar2")
    public int testCalendar2() {
        Calendar c = cd.findByYear(2014);
        return c.getDegrees().size();
    }

    @RequestMapping("students")
    public ModelAndView students() {
        return new ModelAndView(
                "redirect:https://fenix.tecnico.ulisboa.pt/api/fenix/v1/degrees/2761663971475/students?curricularYear=1");

    }
}
