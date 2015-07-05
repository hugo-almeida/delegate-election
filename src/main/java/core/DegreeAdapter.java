package core;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DegreeAdapter implements JsonSerializer<DegreeYear> {

    @Override
    public JsonElement serialize(DegreeYear degree, Type arg1, JsonSerializationContext arg2) {
        JsonObject degreeJson = new JsonObject();
        JsonObject periodJson = new JsonObject();

        degreeJson.addProperty("id", degree.getDegree().getId());
        degreeJson.addProperty("name", degree.getDegreeName());
        degreeJson.addProperty("academicYear", degree.getCalendarYear() + "/" + (degree.getCalendarYear() + 1));
        degreeJson.addProperty("curricularYear", degree.getDegreeYear());

        if (degree.getActivePeriod() != null) {
            periodJson.addProperty("type", degree.getActivePeriod().getType().toString());
            periodJson.addProperty("start", degree.getActivePeriod().getStart().toString());
            periodJson.addProperty("end", degree.getActivePeriod().getEnd().toString());
        }
        degreeJson.add("currentPeriod", periodJson);

        return degreeJson;
    }

}
