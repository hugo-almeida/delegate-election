package core;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DegreeYearAdapter implements JsonSerializer<Degree>, JsonDeserializer<Degree> {

    @Override
    public JsonElement serialize(Degree degree, Type arg1, JsonSerializationContext arg2) {
        JsonObject degreeObject = new JsonObject();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        degreeObject.addProperty("degree", degree.getAcronym());
        degreeObject.addProperty("degreeId", degree.getId());

        JsonArray years = new JsonArray();
        for (DegreeYear degreeYear : degree.getYears()) {
            JsonObject yearObject = new JsonObject();
            JsonObject periodObject = new JsonObject();

            yearObject.addProperty("degreeYear", degreeYear.getDegreeYear());

            if (degreeYear.getCurrentApplicationPeriod() != null) {
                periodObject.addProperty("applicationPeriodId", degreeYear.getCurrentApplicationPeriod().getId());
                periodObject.addProperty("applicationPeriodStart", degreeYear.getCurrentApplicationPeriod().getStart()
                        .format(dtf));
                periodObject.addProperty("applicationPeriodEnd", degreeYear.getCurrentApplicationPeriod().getEnd().format(dtf));
                periodObject.addProperty("applicationPeriodType", degreeYear.getCurrentApplicationPeriod().getType().toString());
            }

            if (degreeYear.getCurrentElectionPeriod() != null) {
                periodObject.addProperty("electionPeriodId", degreeYear.getCurrentElectionPeriod().getId());
                periodObject.addProperty("electionPeriodStart", degreeYear.getCurrentElectionPeriod().getStart().format(dtf));
                periodObject.addProperty("electionPeriodEnd", degreeYear.getCurrentElectionPeriod().getEnd().format(dtf));
                periodObject.addProperty("electionPeriodType", degreeYear.getCurrentElectionPeriod().getType().toString());
                periodObject.addProperty("voteCount", degreeYear.getCurrentElectionPeriod().getVotes().size());
                // TODO Falta a foto!
            }

            yearObject.add("periods", periodObject);
            years.add(yearObject);
        }
        degreeObject.add("years", years);

        return degreeObject;
    }

    @Override
    public Degree deserialize(JsonElement periodElement, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        JsonObject periodObject = periodElement.getAsJsonObject();

        String id = periodObject.get("id").getAsString();
        LocalDate start = LocalDate.parse(periodObject.get("start").getAsString());
        LocalDate end = LocalDate.parse(periodObject.get("end").getAsString());
        int year = periodObject.get("degreeYear").getAsInt();
        String degreeId = periodObject.get("degreeId").getAsString();

//        return new ElectionPeriod(start, end, new DegreeYear(year, new Degree(null, null, degreeId, null, null)));
        return null;
    }
}
