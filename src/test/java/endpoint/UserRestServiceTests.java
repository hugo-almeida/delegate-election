package endpoint;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import core.ApplicationPeriod;
import core.Calendar;
import core.CalendarDAO;
import core.Degree;
import core.Degree.DegreeType;
import core.DegreeYear;
import core.ElectionPeriod;
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

    private Student studentOne;
    private Student studentTwo;
    private Degree degreeOne;
    private Degree degreeTwo;
    private Student studentFour;
    private Student studentThree;

    private DegreeYear firstDegreeYear;

    private DegreeYear secondDegreeYear;

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
        secondDegreeYear = degreeOne.getDegreeYear(2);

        firstDegreeYear
                .addPeriod(new ApplicationPeriod(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), firstDegreeYear));

        secondDegreeYear
                .addPeriod(new ElectionPeriod(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), firstDegreeYear));

        studentOne = new Student("Student1", "id1", "email1@email.com", "", "");
        studentTwo = new Student("Student2", "id2", "email2@email.com", "", "");
        studentThree = new Student("Student3", "id3", "email3@email.com", "", "");
        studentFour = new Student("Student4", "id4", "email4@email.com", "", "");

        studentOne.setDegreeYear(firstDegreeYear);
        firstDegreeYear.addStudent(studentOne);

        studentTwo.setDegreeYear(secondDegreeYear);
        secondDegreeYear.addStudent(studentTwo);
        studentFour.setDegreeYear(secondDegreeYear);
        secondDegreeYear.addStudent(studentFour);

        calendarRepository.save(calendar);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void studentWithNoDegressTest() {
        when().get("/students/{istId}/degrees", studentThree.getUsername()).then().assertThat().statusCode(200).body("$",
                emptyIterable());
    }

    // Not working
    @Test
    public void studentWithOneDegressTest() {
        when().get("/students/{istId}/degrees", studentOne.getUsername()).then().assertThat().statusCode(200).body("$[0].id",
                equalTo(degreeOne.getId()));
    }

    @Test
    public void votingStudentTest() {

    }

    @Test
    public void nonVotingStudentTest() {

    }

}
