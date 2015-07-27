package adapter;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import core.ApplicationPeriod;
import core.DegreeYear;
import core.ElectionPeriod;

public class DegreePeriodAdapter implements JsonSerializer<DegreeYear> {

    @Override
    public JsonElement serialize(DegreeYear degree, Type arg1, JsonSerializationContext arg2) {
        final JsonObject degreeJson = new JsonObject();

        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        final ApplicationPeriod applicationPeriod = degree.getCurrentApplicationPeriod();
        final ElectionPeriod electionPeriod = degree.getCurrentElectionPeriod();
        if (applicationPeriod != null) {
            degreeJson.addProperty("applicationStart", applicationPeriod.getStart().format(dtf));
            degreeJson.addProperty("applicationEnd", applicationPeriod.getEnd().format(dtf));
        } else {
            degreeJson.addProperty("applicationStart", "");
            degreeJson.addProperty("applicationEnd", "");
        }
        if (electionPeriod != null) {
            degreeJson.addProperty("electionStart", electionPeriod.getStart().format(dtf));
            degreeJson.addProperty("electionEnd", electionPeriod.getEnd().format(dtf));
        } else {
            degreeJson.addProperty("electionStart", "");
            degreeJson.addProperty("electionEnd", "");
        }

        return degreeJson;
    }
}
