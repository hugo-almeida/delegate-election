package endpoint;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.sso.EnableOAuth2Sso;
import org.springframework.cloud.security.oauth2.sso.OAuth2SsoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import adapter.DegreeAdapter;
import adapter.DegreeChange;
import adapter.DegreePeriodAdapter;
import adapter.DegreeYearAdapter;
import adapter.DegreeYearHistoryAdapter;
import adapter.HibernateProxyTypeAdapter;
import adapter.PeriodChange;
import adapter.StudentAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.ApplicationPeriod;
import core.CalendarDAO;
import core.Degree;
import core.DegreeDAO;
import core.DegreeYear;
import core.ElectionPeriod;
import core.Period;
import core.Period.PeriodType;
import core.PeriodDAO;
import core.Student;
import core.StudentDAO;
import endpoint.exception.UnauthorizedException;

@EnableOAuth2Sso
@RestController
public class Controller {

    private static final String ACCESS_TOKEN =
            "ODUxOTE1MzUzMDk2MTkzOjg1NDJmMDMwN2Y5ZDZiZWY4NTQxZThhM2NlMzkyZjQwYzE3MzNmOWM0NzJlYzM4NDM2ZjJlZjFkYzMyNjM2ZTc2ZDkxNTdlNjZmNjM4OGUzMGMxYTU4ZTk5YzYzNWFiMDMxN2RhOTA2MWI0MDExN2Y3NTAwNGRmMTFlOTk5N2Q0";
    @Autowired
    StudentDAO studentDAO;

    @Autowired
    DegreeDAO degreeDAO;

    @Autowired
    CalendarDAO calendarDAO;

    @Autowired
    PeriodDAO periodDAO;

    @PostConstruct
    public void schedulePeriods() {

        //Pode nao haver nada iniciado
        if (calendarDAO.findFirstByOrderByYearDesc() == null) {
            return;
        }
        if (calendarDAO.findFirstByOrderByYearDesc().getDegrees() == null) {
            return;
        }

        for (final Degree d : calendarDAO.findFirstByOrderByYearDesc().getDegrees()) {
            for (final DegreeYear dy : d.getYears()) {
                for (final Period p : dy.getInactivePeriods()) {
                    if (p.getStart().isAfter(LocalDate.now())) {
                        p.schedulePeriod(periodDAO, degreeDAO);
                    } else if (p.getStart().isBefore(LocalDate.now()) && p.getEnd().isAfter(LocalDate.now())) {
                        if (dy.getStudents().isEmpty()) {
                            // Caso seja uma segunda oportunidade de candidatura deviamos voltar a inicializar os alunos.
                            dy.initStudents();
                        }
                        dy.setActivePeriod(p);
                    }
                }
                final Period activePeriod = dy.getActivePeriod();
                // Passar tudo a localdatetime talvez seja melhor.
                if (activePeriod != null) {
                    if (!activePeriod.getEnd().isBefore(LocalDate.now())) {
                        activePeriod.schedulePeriodEnd(periodDAO);
                    } else {
                        activePeriod.setInactive();
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/students/{istId}/degrees", method = RequestMethod.GET)
    public @ResponseBody String getStudentDegrees(@PathVariable String istId) {

        final Set<DegreeYear> studentDegrees =
                StreamSupport
                        .stream(studentDAO.findAll().spliterator(), false)
                        .filter(s -> s.getUsername().equals(istId)
                                && s.getDegreeYear().getDegree().getYear() == calendarDAO.findFirstByOrderByYearDesc().getYear())
                        .map(Student::getDegreeYear).collect(Collectors.toSet());

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        gsonBuilder.registerTypeAdapter(DegreeYear.class, new DegreeAdapter());
        final Gson gson = gsonBuilder.create();
        return gson.toJson(studentDegrees);
    }

    @RequestMapping(value = "/students/{istId}/degrees/{degreeId}/votes", method = RequestMethod.GET)
    public @ResponseBody String getVote(@PathVariable String istId, @PathVariable String degreeId) {

        final Student student =
                StreamSupport
                        .stream(studentDAO.findAll().spliterator(), false)
                        .filter(s -> s.getUsername().equals(istId) && s.getDegreeYear().getDegree().getId().equals(degreeId)
                                && s.getDegreeYear().getDegree().getYear() == calendarDAO.findFirstByOrderByYearDesc().getYear())
                        .collect(Collectors.toList()).get(0);

        // Desta forma é possível obter o voto do último periodo também.
        final String voted = student.getDegreeYear().getCurrentElectionPeriod().getVote(istId);
        if (voted == null) {
            return new Gson().toJson("");
        }
        if (voted.isEmpty()) {
            return new Gson().toJson("null");
        }

        return getUser(voted);
    }

    @RequestMapping(value = "/students/{istId}/degrees/{degreeId}/votes", method = RequestMethod.POST)
    public @ResponseBody String addVote(@PathVariable String istId, @PathVariable String degreeId, @RequestBody String vote) {
        if (!getLoggedUsername().equals(istId)) {
            return new Gson().toJson("");
        }

        final Student student =
                StreamSupport
                        .stream(studentDAO.findAll().spliterator(), false)
                        .filter(s -> s.getUsername().equals(istId) && s.getDegreeYear().getDegree().getId().equals(degreeId)
                                && s.getDegreeYear().getDegree().getYear() == calendarDAO.findFirstByOrderByYearDesc().getYear())
                        .collect(Collectors.toList()).get(0);
        Student candidate;
        if (vote.equals("nil")) {
            candidate = null;
        } else {
            candidate =
                    StreamSupport
                            .stream(studentDAO.findAll().spliterator(), false)
                            .filter(s -> s.getUsername().equals(vote)
                                    && s.getDegreeYear().getDegree().getId().equals(degreeId)
                                    && s.getDegreeYear().getDegree().getYear() == calendarDAO.findFirstByOrderByYearDesc()
                                            .getYear()).collect(Collectors.toList()).get(0);
        }

        //The active periods must be the same for both students
        final Period period = student.getDegreeYear().getActivePeriod();
        try {
            final ElectionPeriod ePeriod = (ElectionPeriod) period;
            ePeriod.vote(student, candidate);
        } catch (final ClassCastException e) {
            // Se o cast não foi possivel, o periodo actual nao e de eleicao
            final Gson gson = new Gson();
            return gson.toJson("");
        }
        studentDAO.save(student);
        return getStudent(vote, degreeId);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates", method = RequestMethod.GET)
    public @ResponseBody String getCandidates(@PathVariable String degreeId, @PathVariable int year) {
        final Set<Student> candidates =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year)
                        .getCandidates();
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Student.class, new StudentAdapter()).create();
        return gson.toJson(candidates);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates", method = RequestMethod.POST)
    public @ResponseBody String addCandidate(@PathVariable String degreeId, @PathVariable int year, @RequestBody String istId) {
        if (!getLoggedUsername().equals(istId) && !hasAccessToManagement()) {
            return new Gson().toJson("");
        }
        final Student applicant =
                StreamSupport
                        .stream(studentDAO.findAll().spliterator(), false)
                        .filter(s -> s.getUsername().equals(istId) && s.getDegreeYear().getDegree().getId().equals(degreeId)
                                && s.getDegreeYear().getDegreeYear() == year
                                && s.getDegreeYear().getDegree().getYear() == calendarDAO.findFirstByOrderByYearDesc().getYear())
                        .collect(Collectors.toList()).get(0);

        final Period period = applicant.getDegreeYear().getActivePeriod();
        try {
            final ApplicationPeriod aPeriod = (ApplicationPeriod) period;
            aPeriod.addCandidate(applicant);
        } catch (final ClassCastException e) {
            // Se o cast não foi possivel, o periodo actual nao e de condidatura
            final Gson gson = new Gson();
            return gson.toJson("fail");
        }
        studentDAO.save(applicant);
        //TODO Smarter responses
        final Gson gson = new Gson();
        return gson.toJson("success");
    }

    //Se a resposta for vazia, não se candidatou a este curso. Talvez seja mais simples retornar false ou true?
    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates/{istId}", method = RequestMethod.GET)
    public @ResponseBody String getCandidate(@PathVariable String degreeId, @PathVariable int year, @PathVariable String istId) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Student.class, new StudentAdapter()).create();
        final List<Student> s =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year)
                        .getCandidates().stream().filter(c -> c.getUsername().equals(istId)).collect(Collectors.toList());
        if (s.isEmpty()) {
            return gson.toJson("");
        }
        final Student candidate = s.get(0);

        return gson.toJson(candidate);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates/{istId}", method = RequestMethod.DELETE)
    public @ResponseBody String removeCandidate(@PathVariable String degreeId, @PathVariable int year, @PathVariable String istId) {
        if (!getLoggedUsername().equals(istId)) {
            return new Gson().toJson("");
        }

        final Student candidate =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year)
                        .getCandidates().stream().filter(c -> c.getUsername().equals(istId)).collect(Collectors.toList()).get(0);
        ((ApplicationPeriod) candidate.getDegreeYear().getActivePeriod()).removeCandidates(candidate);
        studentDAO.save(candidate);
        final Gson g = new Gson();
        //TODO smarter Responses
        return g.toJson("Ok");
    }

    // Este Endpoint pode ser usado para obter todos os estudantes em que se pode votar.
    // Falta acrescentar os filtros
    // Para cada estudante, devolve: nome, id e foto
    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/students", method = RequestMethod.GET)
    public @ResponseBody String getDegreeYearStudents(@PathVariable String degreeId, @PathVariable int year, @RequestParam(
            value = "begins", required = false) String start) {
        final Set<Student> students =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year)
                        .getStudents();
        Set<Student> filteredStudents = students;
        if (start != null) {
            filteredStudents =
                    students.stream()
                            .filter(s -> s.getName().toLowerCase().contains(start.toLowerCase())
                                    || s.getUsername().toLowerCase().contains(start.toLowerCase())).collect(Collectors.toSet());
        }
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Student.class, new StudentAdapter()).create();
        return gson.toJson(filteredStudents);
    }

    /***************************** Manager API *****************************/
    @RequestMapping(value = "/students/{istId}", method = RequestMethod.GET)
    public @ResponseBody String getStudent(@PathVariable String istId, @PathVariable String degreeId) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final Student student =
                studentDAO.findByUsernameAndDegreeAndCalendarYear(istId, degreeId, calendarDAO.findFirstByOrderByYearDesc()
                        .getYear());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Student.class, new StudentAdapter()).create();
        return gson.toJson(student);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/votes", method = RequestMethod.GET)
    public @ResponseBody String getVotes(@PathVariable String degreeId, @PathVariable int year) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        //Obtem todos os votos (aluno -> numero de votos)
        final Map<String, Long> voteCount =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year)
                        .getCurrentElectionPeriod().getVotes().stream()
                        .collect(Collectors.groupingBy(v -> v.getVoted(), Collectors.counting()));
        final Gson gson = new Gson();
        return gson.toJson(voteCount);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/history", method = RequestMethod.GET)
    public @ResponseBody String getHistoy(@PathVariable String degreeId, @PathVariable int year) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final DegreeYear degreeYear =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DegreeYear.class, new DegreeYearHistoryAdapter()).create();
        return gson.toJson(degreeYear);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/periods", method = RequestMethod.GET)
    public @ResponseBody String getPeriods(@PathVariable String degreeId, @PathVariable int year) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final DegreeYear degreeYear =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DegreeYear.class, new DegreePeriodAdapter()).create();
        return gson.toJson(degreeYear);
    }

    @RequestMapping(value = "/periods", method = RequestMethod.GET)
    public @ResponseBody String getPeriods() {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final Set<Degree> degrees =
                StreamSupport.stream(degreeDAO.findAll().spliterator(), false)
                        .filter(d -> d.getYear() == calendarDAO.findFirstByOrderByYearDesc().getYear())
                        .collect(Collectors.toSet());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Degree.class, new DegreeYearAdapter()).create();
        final Gson gson = gsonBuilder.create();
        return gson.toJson(degrees);
    }

    // Permitir criar apenas periodos futuros
    @RequestMapping(value = "/periods", method = RequestMethod.POST)
    public @ResponseBody String addPeriod(@RequestBody String periodJson) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final JsonParser parser = new JsonParser();

        final JsonObject periodObject = (JsonObject) parser.parse(periodJson);
        final String degreeId = periodObject.get("degreeId").getAsString();
        final int year = periodObject.get("degreeYear").getAsInt();
        final String periodType = periodObject.get("periodType").getAsString();
        final LocalDate start = LocalDate.parse(periodObject.get("start").getAsString(), dtf);
        final LocalDate end = LocalDate.parse(periodObject.get("end").getAsString(), dtf);

        final DegreeYear degreeYear =
                degreeDAO.findByIdAndYear(degreeId, calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year);

        if (periodType.equals(PeriodType.Application.toString())) {
            final Period period = degreeYear.addPeriod(start, end, periodType);
            if (period != null) {
                period.schedulePeriod(periodDAO, degreeDAO);
                periodDAO.save(period);
            }
        } else if (periodType.equals(PeriodType.Election.toString())) {
            final Period period = degreeYear.addPeriod(start, end, periodType);
            if (period != null) {
                period.schedulePeriod(periodDAO, degreeDAO);
                periodDAO.save(period);
            }
        }

        return new Gson().toJson("ok");
    }

    // Permitir alterar apenas periodos futuros
    // Permitir criar
    @RequestMapping(value = "/periods", method = RequestMethod.PUT)
    public @ResponseBody String updatePeriods(@RequestBody String periodsJson) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }
        System.out.println(periodsJson);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DegreeChange.class, new DegreeYearAdapter()).create();
        final DegreeChange[] degrees = gson.fromJson(periodsJson, DegreeChange[].class);
        for (final DegreeChange degreeChange : degrees) {
            for (final Integer year : degreeChange.getPeriods().keySet()) {
                for (final PeriodChange change : degreeChange.getPeriods().get(year)) {
                    final DegreeYear degreeYear =
                            degreeDAO.findByIdAndYear(degreeChange.getDegreeId(),
                                    calendarDAO.findFirstByOrderByYearDesc().getYear()).getDegreeYear(year);
                    final boolean periodExists = degreeYear.setDate(change.getStart(), change.getEnd(), change.getPeriodType());
                    // Se o periodo existe, tem de agendar novamente, pois uma ou ambas as datas foram alteradas.
                    if (periodExists) {
                        System.out.println("Period Exists");
                        Period period = degreeYear.getPeriodActiveOnDate(change.getStart());
                        if (period == null) {
                            period = degreeYear.getPeriodActiveOnDate(change.getEnd());
                        } else {
                            continue;
                        }
                        period.unschedulePeriod(periodDAO, degreeDAO);
                        period.schedulePeriod(periodDAO, degreeDAO);
                    } else {
                        System.out.println("Period Does Not Exist");
                        if (change.getPeriodType().toString().equals(PeriodType.Application.toString())) {
                            final Period period = new ApplicationPeriod(change.getStart(), change.getEnd(), degreeYear);
                            degreeYear.addPeriod(period);
                            period.schedulePeriod(periodDAO, degreeDAO);
                            periodDAO.save(period);
                        } else if (change.getPeriodType().toString().equals(PeriodType.Election.toString())) {
                            final Period period = new ElectionPeriod(change.getStart(), change.getEnd(), degreeYear);
                            degreeYear.addPeriod(period);
                            period.schedulePeriod(periodDAO, degreeDAO);
                            periodDAO.save(period);
                        }
                    }
                }
            }
        }
        calendarDAO.save(calendarDAO.findFirstByOrderByYearDesc());
        return new Gson().toJson("ok");
    }

    @RequestMapping(value = "/periods", method = RequestMethod.DELETE)
    public @ResponseBody String removePeriods(@RequestBody String periodsJson) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DegreeChange.class, new DegreeYearAdapter()).create();
        final DegreeChange[] degrees = gson.fromJson(periodsJson, DegreeChange[].class);
        for (final DegreeChange degreeChange : degrees) {
            for (final Integer year : degreeChange.getPeriods().keySet()) {
                for (final PeriodChange change : degreeChange.getPeriods().get(year)) {
                    final Period period = periodDAO.findById(change.getPeriodId());
                    if (period.getStart().isAfter(LocalDate.now())) {
                        period.unschedulePeriod(periodDAO, degreeDAO);
                        periodDAO.delete(change.getPeriodId());
                    }
                }
            }
        }
        return new Gson().toJson("ok");
    }

    // recebe Json com 2 datas: start e end
    @RequestMapping(value = "/periods/{periodId}", method = RequestMethod.PUT)
    public @ResponseBody String updatePeriod(@PathVariable int periodId, @RequestBody String dates) {
        if (!hasAccessToManagement()) {
            return new Gson().toJson("");
        }

        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final JsonParser parser = new JsonParser();

        final Period period = periodDAO.findById(periodId);
        final DegreeYear degreeYear = period.getDegreeYear();

        final JsonObject datesObject = (JsonObject) parser.parse(dates);
        final LocalDate newStart = LocalDate.parse(datesObject.get("start").getAsString(), dtf);
        final LocalDate newEnd = LocalDate.parse(datesObject.get("end").getAsString(), dtf);

        final LocalDate start = period.getStart();
        final LocalDate end = period.getEnd();

        if (newStart.isAfter(LocalDate.now()) && start.isAfter(LocalDate.now())
                && !degreeYear.hasPeriodBetweenDates(newStart, end, period)) {
            period.setStart(newStart);
        }

        if (newEnd.isAfter(LocalDate.now()) && end.isAfter(LocalDate.now()) && newEnd.isAfter(period.getStart())
                && !degreeYear.hasPeriodBetweenDates(start, newEnd, period)) {
            period.setEnd(newEnd);
        }

        period.unschedulePeriod(periodDAO, degreeDAO);
        period.schedulePeriod(periodDAO, degreeDAO);

        periodDAO.save(period);
        return new Gson().toJson("ok");
    }

    @RequestMapping(value = "/periods/{periodId}/candidates", method = RequestMethod.GET)
    public @ResponseBody String getCandidatesFromPeriod(@PathVariable int periodId) {
        final Period period = periodDAO.findById(periodId);
        if (period == null) {
            return new Gson().toJson("No Period with that Id");
        }
        final Set<Student> candidates = period.getCandidates();
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Student.class, new StudentAdapter()).create();
        return gson.toJson(candidates);
    }

    @RequestMapping(value = "/periods/{periodId}/candidates", method = RequestMethod.POST)
    public @ResponseBody String addCandidateToPeriod(@PathVariable int periodId, @PathVariable String istId) {
        final Period period = periodDAO.findById(periodId);
        if (period == null) {
            return new Gson().toJson("No Period with that Id");
        }

        final Set<Student> students = period.getDegreeYear().getStudents();
        Student st = null;
        for (final Student s : students) {
            if (s.getUsername().equals(istId)) {
                st = s;
            }
        }
        //Student doesn't exist in DegreeYear must be created
        if (st == null) {
            final RestTemplate t = new RestTemplate();
            final String infoUrl = "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/person?access_token=" + ACCESS_TOKEN;

            st = t.getForObject(infoUrl, Student.class);
            st.setDegreeYear(period.getDegreeYear());
            final HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set("__username__", istId);
            final HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
            final HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.GET, requestEntity, String.class);
            final JsonObject result = new JsonParser().parse(response.getBody()).getAsJsonObject();
            if (!result.get("email").isJsonNull()) {
                st.setEmail(result.get("email").toString());
            }
            if (!result.get("photo").isJsonNull()) {
                st.setPhotoType(result.getAsJsonObject("photo").get("type").toString());
                st.setPhotoBytes(result.getAsJsonObject("photo").get("data").toString());
            }
        }
        period.addCandidate(st);
        studentDAO.save(st);
        return new Gson().toJson("ok");
    }

    /***************************** OLD API *****************************/
    @RequestMapping("/user")
    public @ResponseBody String user() {

        final OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        final Map<String, Object> userDetails = (Map<String, Object>) auth.getUserAuthentication().getDetails();

        /*   final OAuth2AuthenticationDetails authDetails = (OAuth2AuthenticationDetails) auth.getDetails();
           authDetails.getTokenValue();*/

        final Gson gson = new Gson();
        final String json = gson.toJson(userDetails);

        return json;
    }

    @RequestMapping(value = "/get-user", method = RequestMethod.POST)
    public @ResponseBody String getUser(@RequestBody String username) {
        final RestTemplate t = new RestTemplate();
        t.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        final String infoUrl = "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/person?access_token=" + ACCESS_TOKEN;

        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("__username__", username);
        final HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
        final HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.GET, requestEntity, String.class);
        final JsonObject result = new JsonParser().parse(response.getBody()).getAsJsonObject();

        final Gson gson = new Gson();
        return gson.toJson(result);
    }

    /************************************* MVC ***************************************/
    @RequestMapping("/pedagogico")
    public ModelAndView testpedagogico() throws UnauthorizedException {
        if (hasAccessToManagement()) {
            return new ModelAndView("redirect:/gerir.html");
        } else {
            throw new UnauthorizedException();
        }
    }

    private String getLoggedUsername() {
        final OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        final Map<String, Object> userDetails = (Map<String, Object>) auth.getUserAuthentication().getDetails();
        return (String) userDetails.get("username");
    }

    private boolean hasAccessToManagement() {
        final Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/pedagogico.properties"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        String userarray[];

        final String list = prop.getProperty("users");
        userarray = list.split(",");

        final Set<String> userset = new HashSet<String>();

        for (final String s : userarray) {
            userset.add(s);
        }

        final OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        final Map<String, Object> userDetails = (Map<String, Object>) auth.getUserAuthentication().getDetails();
        return userset.contains(userDetails.get("username"));
    }

    /*********************************** CONFIG *****************************************/
    @Configuration
    protected static class SecurityConfiguration extends OAuth2SsoConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.logout()
                    .and()
                    .antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/index.html", "/", "/login", "/test-calendar", "/get-user")
                    .permitAll()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/home.html", "/resource", "/user", "/period", "/vote", "/user", "/get-candidates", "/apply",
                            "/deapply", "get-students", "/students/**", "/degrees/**", "/pedagogico", "/periods", "/estudante")
                    .authenticated().and().csrf().csrfTokenRepository(csrfTokenRepository()).and()
                    .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    final CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    if (csrf != null) {
                        Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                        final String token = csrf.getToken();
                        if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                            cookie = new Cookie("XSRF-TOKEN", token);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }
    }
}