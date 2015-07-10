package core;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ElectionPeriodAdapter implements JsonSerializer<ElectionPeriod>, JsonDeserializer<ElectionPeriod> {

    @Override
    public JsonElement serialize(ElectionPeriod period, Type arg1, JsonSerializationContext arg2) {
        JsonObject periodObject = new JsonObject();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        periodObject.addProperty("id", oeriod.getId());
        periodObject.addProperty("degree", period.getDegreeYear().getDegree().getAcronym());
        periodObject.addProperty("degreeId", period.getDegreeYear().getDegree().getId());
        periodObject.addProperty("degreeYear", period.getDegreeYear().getDegreeYear());
        periodObject.addProperty("start", period.getStart().format(dtf));
        periodObject.addProperty("end", period.getEnd().format(dtf));
        periodObject.addProperty("type", period.getType().toString());
        periodObject.addProperty("voteCount", period.getVotes().size());
        // TODO Falta a foto!

        return null;
    }

    @Override
    public ElectionPeriod deserialize(JsonElement periodElement, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
        JsonObject periodObject = periodElement.getAsJsonObject();

//        String id = periodObject.get("id");
        LocalDate start = LocalDate.parse(periodObject.get("start").getAsString());
        LocalDate end = LocalDate.parse(periodObject.get("end").getAsString());

        return new ElectionPeriod(start, end, null);
    }
}
