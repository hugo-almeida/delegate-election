package endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.Degree.DegreeType;
import core.DegreeYear;
import core.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest
//@IntegrationTest("server.port:0")
public class UserRestServiceTests {

    @Autowired // 5
    CalendarDAO repository;

    @Value("${local.server.port}") // 6
    int port;

    @Before
    public void setUp() {
        repository.deleteAll();

        Calendar calendar = new Calendar(3);
        Degree degree = new Degree("", "", "1", DegreeType.Bachelor.toString(), calendar);
        calendar.addDegree(degree);
        degree.initDegreeYears();
        DegreeYear firstDegreeYear = degree.getDegreeYear(1);
        Student me = new Student("Fernando", "ist173833", "stuff@gmail.com", "", "");
        me.setDegreeYear(firstDegreeYear);
        firstDegreeYear.addStudent(me);

        repository.save(calendar);

        // 9
//        RestAssuredMockMvc.basePath = "http://localhost:8080";
        RestAssuredMockMvc.standaloneSetup(new Controller());
    }

    // 10
    @Test
    public void canFetchStudentDegrees() {
        RestAssuredMockMvc.when().get("/students/{istId}/degrees", "ist173833").then().statusCode(200);
    }

}
