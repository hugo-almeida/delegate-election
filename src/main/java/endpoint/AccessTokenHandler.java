package endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.Degree;
import core.Student;

@Component
public class AccessTokenHandler {

    @Autowired
    Environment e;

    @Value("${environment.uri}")
    private String base_domain;

    @Value("${environment.serverId}")
    private String clientId;

    @Value("${environment.serverSecret}")
    private String clientSecret;

    private String accessToken = null;

    private final String refreshToken = null;

    private final String code = null;

    private static AccessTokenHandler instance = null;

    protected AccessTokenHandler() {

    }

    public static AccessTokenHandler getInstance() {
        if (instance == null) {
            instance = new AccessTokenHandler();
        }
        return instance;
    }

    public String getAccessToken() throws Exception {
        if (accessToken == null) {
            return getNewAccessToken();
        } else {
            return accessToken;
        }
    }

    public String getNewAccessToken() throws Exception {
//        Environment env = e;
//        String domain_base = env.getProperty("environment.uri");
//        String clientId = env.getProperty("environment.serverId");
//        String clientSecret = env.getProperty("environment.serverSecret");
        if (base_domain == null || clientId == null || clientSecret == null) {
            throw new Exception("Exception reading from .yml at AccessTokenHandler");
        }

        RestTemplate t = new RestTemplate();
        String infoUrl =
                "https://fenix.tecnico.ulisboa.pt/oauth/access_token?client_id=" + clientId + "&client_secret=" + clientSecret
                        + "&grant_type=client_credentials";
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
        HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.POST, requestEntity, String.class);
        JsonObject json = new JsonParser().parse(response.getBody()).getAsJsonObject();
        accessToken = json.get("access_token").getAsString();

        return accessToken;
    }

    public Degree[] getDegrees() {
        RestTemplate t = new RestTemplate();
        Degree[] degrees = null;
        do {
            try {
                degrees = t.getForObject(base_domain + "/api/fenix/v1/degrees", Degree[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (degrees == null);
        return degrees;
    }

    public Student[] getStudents(String degreeId, int degreeYear) throws Exception {
        RestTemplate t = new RestTemplate();
        Student[] students = null;
        do {
            try {
                students =
                        t.getForObject(base_domain + "/api/fenix/v1/degrees/" + degreeId + "/students?curricularYear="
                                + degreeYear + "&access_token=" + accessToken, Student[].class);
            } catch (Exception e) {
                //e.printStackTrace();
                getNewAccessToken();
            }
        } while (students == null);
        return students;
    }
}
