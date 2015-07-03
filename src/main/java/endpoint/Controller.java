package endpoint;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

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
    CalendarDAO calendarDAO;

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