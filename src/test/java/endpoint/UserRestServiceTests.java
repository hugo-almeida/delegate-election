package endpoint;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.Degree.DegreeType;
import core.DegreeYear;
import core.Student;
import core.StudentDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest
public class UserRestServiceTests {

    @Autowired
    CalendarDAO calendarRepository;

    @Autowired
    StudentDAO studentRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    Student studentOne;
    Student studentTwo;
    Degree degreeOne;
    Degree degreeTwo;
    DegreeYear firstDegreeYear;

    @Before
    public void setUp() {
        calendarRepository.deleteAll();

        Calendar calendar = new Calendar(1);
        degreeOne = new Degree("Degree1", "deg1", "1", DegreeType.Bachelor.toString(), calendar);
        calendar.addDegree(degreeOne);
        degreeTwo = new Degree("Degree2", "deg2", "2", DegreeType.Master.toString(), calendar);
        calendar.addDegree(degreeTwo);
        degreeOne.initDegreeYears();
        degreeTwo.initDegreeYears();

        firstDegreeYear = degreeOne.getDegreeYear(1);

        studentOne = new Student("Student1", "id1", "email1@email.com", "", "");
        studentTwo = new Student("Student2", "id2", "email2@email.com", "", "");

        calendarRepository.save(calendar);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void studentWithNoDegressTest() {
        studentTwo.setDegreeYear(firstDegreeYear);
        firstDegreeYear.addStudent(studentTwo);

        calendarRepository.save(calendarRepository.findFirstByOrderByYearDesc());

        when().get("/students/{istId}/degrees", studentOne.getUsername()).then().assertThat().statusCode(200).body("$",
                emptyIterable());
    }

    // Not working
    @Test
    public void studentWithOneDegressTest() {
        studentOne.setDegreeYear(firstDegreeYear);
        firstDegreeYear.addStudent(studentOne);

        calendarRepository.save(calendarRepository.findFirstByOrderByYearDesc());

        when().get("/students/{istId}/degrees", studentOne.getUsername()).then().assertThat().statusCode(200).body("$[0].id",
                equalTo(degreeOne.getId()));
    }

}
