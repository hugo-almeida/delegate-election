package core;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DegreePeriodAdapter implements JsonSerializer<DegreeYear> {

    @Override
    public JsonElement serialize(DegreeYear degree, Type arg1, JsonSerializationContext arg2) {
        JsonObject degreeJson = new JsonObject();
        ApplicationPeriod applicationPeriod = degree.getCurrentApplicationPeriod();
        ElectionPeriod electionPeriod = degree.getCurrentElectionPeriod();
        if (applicationPeriod != null) {
            degreeJson.addProperty("applicationStart", applicationPeriod.getStart().toString());
            degreeJson.addProperty("applicationEnd", applicationPeriod.getEnd().toString());
        } else {
            degreeJson.addProperty("applicationStart", "");
            degreeJson.addProperty("applicationEnd", "");
        }
        if (electionPeriod != null) {
            degreeJson.addProperty("electionStart", electionPeriod.getStart().toString());
            degreeJson.addProperty("electionEnd", electionPeriod.getEnd().toString());
        } else {
            degreeJson.addProperty("electionStart", "");
            degreeJson.addProperty("electionEnd", "");
        }

        return degreeJson;
    }
}
