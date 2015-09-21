package endpoint;

import java.io.IOException;
import java.util.Properties;

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

    private String base_domain = null;

    private String clientId = null;

    private String clientSecret = null;

    private String accessToken = null;

    private static AccessTokenHandler instance = null;

    protected AccessTokenHandler() {

    }

    public static AccessTokenHandler getInstance() {
        if (instance == null) {
            instance = new AccessTokenHandler();
        }
        return instance;
    }

    public void getCredentials() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/application.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        base_domain = prop.getProperty("uri");
        clientId = prop.getProperty("serverId");
        clientSecret = prop.getProperty("serverSecret");
    }

    public String getAccessToken() throws Exception {
        if (accessToken == null) {
            getCredentials();
            return getNewAccessToken();
        } else {
            return accessToken;
        }
    }

    private String getNewAccessToken() throws Exception {
        if (base_domain == null || clientId == null || clientSecret == null) {
            throw new Exception("Exception reading from .yml at AccessTokenHandler");
        }

        RestTemplate t = new RestTemplate();
        String infoUrl =
                base_domain + "/oauth/access_token?client_id=" + clientId + "&client_secret=" + clientSecret
                        + "&grant_type=client_credentials";
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
        HttpEntity<String> response = t.exchange(infoUrl, HttpMethod.POST, requestEntity, String.class);
        JsonObject json = new JsonParser().parse(response.getBody()).getAsJsonObject();
        accessToken = json.get("access_token").getAsString();

        return accessToken;
    }

    public Degree[] getDegrees() throws Exception {
        if (accessToken == null) {
            getAccessToken();
        }
        RestTemplate t = new RestTemplate();
        Degree[] degrees = null;
//        do {
//            try {
        degrees = t.getForObject(base_domain + "/api/fenix/v1/degrees?lang=pt-PT", Degree[].class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } while (degrees == null);
        return degrees;
    }

    public Student[] getStudents(String degreeId, int degreeYear) throws Exception {
        if (accessToken == null) {
            getAccessToken();
        }
        RestTemplate t = new RestTemplate();
        Student[] students = null;
//        do {
        try {
            students =
                    t.getForObject(base_domain + "/api/fenix/v1/degrees/" + degreeId + "/students?curricularYear=" + degreeYear
                            + "&access_token=" + accessToken, Student[].class);
        } catch (Exception e) {
            e.printStackTrace();
//                getNewAccessToken();
        }
//        } while (students == null);
        return students;
    }
}
