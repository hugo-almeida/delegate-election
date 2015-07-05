package endpoint;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.CalendarDAO;
import core.DegreeDAO;
import core.DegreeYear;
import core.Period;
import core.Student;
import core.StudentAdapter;
import core.StudentDAO;

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

    @RequestMapping(value = "/students/{istId}", method = RequestMethod.GET)
    public @ResponseBody String getStudent(@PathVariable String istId) {
        //TODO Retorna a info do estudante
        // Deve ser chamado no início
        return null;
    }

    @RequestMapping(value = "/students/{istId}/degrees", method = RequestMethod.GET)
    public @ResponseBody String getStudentDegrees(@PathVariable String istId) {
        //TODO Retorna cursos em que o estudante está, incluindo o nome, id, ano curricular, ano lectivo e periodo de votacao
        return null;
    }

    @RequestMapping(value = "/students/{istId}/degrees/{degreeId}/votes", method = RequestMethod.GET)
    public @ResponseBody String getVote(@PathVariable String istId, @PathVariable String degreeId,
            HttpServletResponse httpResponse) {
        //TODO Retorna voto do aluno, ou 404 caso não haja
        httpResponse.setStatus(404);
        return new Gson().toJson(null);
    }

    @RequestMapping(value = "/students/{istId}/degrees/{degreeId}/votes", method = RequestMethod.POST)
    public @ResponseBody String addVote(@PathVariable String istId, @PathVariable String degreeId, @RequestBody String voteId) {
        //TODO Adiciona voto
        return null;
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/votes", method = RequestMethod.GET)
    public @ResponseBody String getVotes(@PathVariable String degreeId, @PathVariable int year) {
        //TODO Obtem todos os votos (aluno -> numero de votos)
        return null;
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates", method = RequestMethod.GET)
    public @ResponseBody String getCandidates(@PathVariable String degreeId, @PathVariable int year) {
        //TODO Obtem todos os candidatos do ano/curso (nome, id, foto)
        return null;
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates", method = RequestMethod.POST)
    public @ResponseBody String addCandidate(@PathVariable String degreeId, @PathVariable int year, @RequestBody String istId) {
        //TODO Adiciona candidatos ao ano/curso (id)
        return null;
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates/{istId}", method = RequestMethod.GET)
    public @ResponseBody String getCandidate(@PathVariable String degreeId, @PathVariable int year, @PathVariable String istId,
            HttpServletResponse httpResponse) {
        //TODO retorna o candidato ou 404 caso não haja
        httpResponse.setStatus(404);
        return new Gson().toJson(null);
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/candidates/{istId}", method = RequestMethod.DELETE)
    public @ResponseBody String removeCandidate(@PathVariable String degreeId, @PathVariable int year, @PathVariable String istId) {
        //TODO remove candidatura
        return null;
    }

    // Este Endpoint pode ser usado para obter todos os estudantes em que se pode votar.
    // Falta acrescentar os filtros
    // Para casa estudante, devolve: nome, id e foto
    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/students", method = RequestMethod.GET)
    public @ResponseBody String getDegreeYearStudents(@PathVariable String degreeId, @PathVariable int year) {
        Set<Student> students = degreeDAO.findById(degreeId).getDegreeYear(year).getStudents();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(Student.class, new StudentAdapter()).create();
        return gson.toJson(students);
    }

    /***************************** Manager API *****************************/

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/history", method = RequestMethod.GET)
    public @ResponseBody String getHistoy(@PathVariable String degreeId, @PathVariable int year) {
        //TODO Obtem historico do curso/ano
        return null;
    }

    @RequestMapping(value = "/degrees/{degreeId}/years/{year}/periods", method = RequestMethod.GET)
    public @ResponseBody String getPeriods(@PathVariable String degreeId, @PathVariable int year) {
        //TODO Obtem info dos periodos do ano actuais. Incluindo info dos candidatos e alunos com votos
        return null;
    }

    @RequestMapping(value = "/application-periods", method = RequestMethod.GET)
    public @ResponseBody String getApplicationPeriods() {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
    }

    @RequestMapping(value = "/application-periods", method = RequestMethod.POST)
    public @ResponseBody String addApplicationPeriods(@RequestBody String periods) {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
    }

    @RequestMapping(value = "/application-periods", method = RequestMethod.PUT)
    public @ResponseBody String updateApplicationPeriods(@RequestBody String periods) {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
    }

    @RequestMapping(value = "/application-periods", method = RequestMethod.DELETE)
    public @ResponseBody String removeApplicationPeriods(@RequestBody String periods) {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
    }

    @RequestMapping(value = "/election-periods", method = RequestMethod.GET)
    public @ResponseBody String getElectionPeriods() {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de votos e info do mais votado, incluindo foto
        return null;
    }

    @RequestMapping(value = "/election-periods", method = RequestMethod.POST)
    public @ResponseBody String addElectionPeriods(@RequestBody String periods) {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
    }

    @RequestMapping(value = "/election-periods", method = RequestMethod.PUT)
    public @ResponseBody String updateElectionPeriods(@RequestBody String periods) {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
    }

    @RequestMapping(value = "/election-periods", method = RequestMethod.DELETE)
    public @ResponseBody String removeElectionPeriods(@RequestBody String periods) {
        //TODO Obtem os periodos de candidatura actuais para cada ano/curso, incluindo numero de candidatos
        return null;
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
        RestTemplate t = new RestTemplate();
        t.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        String infoUrl = "https://fenix.tecnico.ulisboa.pt/api/fenix/v1/person?access_token=" + ACCESS_TOKEN;

        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("__username__", username);
        final HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
        final HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.GET, requestEntity, String.class);
        final JsonObject result = new JsonParser().parse(response.getBody()).getAsJsonObject();

        final Gson gson = new Gson();
        return gson.toJson(result);
    }

    //TODO
    @RequestMapping("/period")
    public @ResponseBody String currentPeriod(String istid) {
        System.out.println(istid);
        Student student = studentDAO.findByUsername(istid);
        Period period = student.getDegreeYear().getActivePeriod();
        System.out.println(period);
        Gson gson = new Gson();
        return gson.toJson(period);
    }

    //TODO
    @RequestMapping("/vote")
    public @ResponseBody String vote(String json) {
        System.out.println(json);
        final Gson g = new Gson();
        return g.toJson("Ok");
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public @ResponseBody String apply(@RequestBody String username) {
        final Student s = studentDAO.findByUsername(username);
        s.apply();
        studentDAO.save(s);
        final Gson g = new Gson();
        return g.toJson("Ok");
    }

    @RequestMapping(value = "/de-apply", method = RequestMethod.POST)
    public @ResponseBody String deapply(@RequestBody String username) {
        Student s = studentDAO.findByUsername(username);
        s.deapply();
        studentDAO.save(s);
        Gson g = new Gson();
        return g.toJson("Ok");
    }

    @RequestMapping(value = "/get-candidates", method = RequestMethod.POST)
    public @ResponseBody String getCandidates(@RequestBody String username) {
        Student s = studentDAO.findByUsername(username);
        DegreeYear dy = s.getDegreeYear();
        GsonBuilder b = new GsonBuilder();
        b.registerTypeHierarchyAdapter(Student.class, new StudentAdapter());
        Gson g = b.create();
        return g.toJson(dy.getCandidates());
    }

    @RequestMapping(value = "/get-students", method = RequestMethod.POST)
    public @ResponseBody String getStudents(@RequestBody String username) {
        Student s = studentDAO.findByUsername(username);
        DegreeYear dy = s.getDegreeYear();
        GsonBuilder b = new GsonBuilder();
        b.registerTypeHierarchyAdapter(Student.class, new StudentAdapter());
        Gson g = b.create();
        return g.toJson(dy.getStudents());
    }

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
                    .antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/home.html", "/resource", "/user", "/period", "/vote", "/user", "/get-candidates", "/apply",
                            "/deapply", "get-students").authenticated().and().csrf().csrfTokenRepository(csrfTokenRepository())
                    .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
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