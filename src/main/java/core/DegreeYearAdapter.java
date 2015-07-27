package core;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import core.Period.PeriodType;

public class DegreeYearAdapter implements JsonSerializer<Degree>, JsonDeserializer<DegreeChange> {

    @Override
    public JsonElement serialize(Degree degree, Type arg1, JsonSerializationContext arg2) {
        JsonObject degreeObject = new JsonObject();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");

        degreeObject.addProperty("degree", degree.getAcronym());
        degreeObject.addProperty("degreeName", degree.getName());
        degreeObject.addProperty("degreeId", degree.getId());
        degreeObject.addProperty("academicYear", degree.getYear() + "/" + (degree.getYear() + 1));

        JsonArray years = new JsonArray();
        Map<Integer, DegreeYear> sortedDegrees = new HashMap<Integer, DegreeYear>();
        for (DegreeYear degreeYear : degree.getYears()) {
            sortedDegrees.put(degreeYear.getDegreeYear(), degreeYear);
        }
        for (Integer i : sortedDegrees.keySet()) {
            DegreeYear degreeYear = sortedDegrees.get(i);
            JsonObject yearObject = new JsonObject();
            JsonObject applicationObject = new JsonObject();
            JsonObject electionObject = new JsonObject();

            yearObject.addProperty("degreeYear", degreeYear.getDegreeYear());
            LocalDate now = LocalDate.now();
            if (degreeYear.getCurrentApplicationPeriod() != null) {
                applicationObject.addProperty("applicationPeriodId", degreeYear.getCurrentApplicationPeriod().getId());
                applicationObject.addProperty("applicationPeriodStart", degreeYear.getCurrentApplicationPeriod().getStart()
                        .format(dtf));
                applicationObject.addProperty("applicationPeriodEnd",
                        degreeYear.getCurrentApplicationPeriod().getEnd().format(dtf));
                applicationObject.addProperty("candidateCount", degreeYear.getCurrentApplicationPeriod().getCandidateCount());
                if (degreeYear.getCurrentApplicationPeriod().getEnd().isBefore(now)) {
                    applicationObject.addProperty("state", "passado");
                } else {
                    applicationObject.addProperty("state", "presente/futuro");
                }
            }

            if (degreeYear.getCurrentElectionPeriod() != null) {
                electionObject.addProperty("electionPeriodId", degreeYear.getCurrentElectionPeriod().getId());
                electionObject.addProperty("electionPeriodStart", degreeYear.getCurrentElectionPeriod().getStart().format(dtf));
                electionObject.addProperty("electionPeriodEnd", degreeYear.getCurrentElectionPeriod().getEnd().format(dtf));
                electionObject.addProperty("voteCount", degreeYear.getCurrentElectionPeriod().getVotes().size());
                if (degreeYear.getCurrentElectionPeriod().getEnd().isBefore(now)) {
                    electionObject.addProperty("state", "passado");
                } else {
                    electionObject.addProperty("state", "presente/futuro");
                }
            }

            yearObject.add("applicationPeriod", applicationObject);
            yearObject.add("electionPeriod", electionObject);
            years.add(yearObject);
        }
        degreeObject.add("years", years);

        return degreeObject;
    }

    @Override
    public DegreeChange deserialize(JsonElement degreeElement, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JsonObject degreeObject = degreeElement.getAsJsonObject();

        String degreeId = degreeObject.get("degreeId").getAsString();
        DegreeChange degree = new DegreeChange(degreeId);

        JsonArray years = degreeObject.get("years").getAsJsonArray();

        for (JsonElement yearElement : years) {
            JsonObject yearObject = yearElement.getAsJsonObject();

            int year = yearObject.get("degreeYear").getAsInt();

            Set<PeriodChange> periods = new HashSet<PeriodChange>();

            JsonObject applicationPeriodObject = yearObject.getAsJsonObject("applicationPeriod");
            if (applicationPeriodObject.has("applicationPeriodId")
                    || (applicationPeriodObject.has("applicationPeriodStart") && applicationPeriodObject
                            .has("applicationPeriodEnd"))) {
                int id = Integer.MIN_VALUE;
                if (applicationPeriodObject.has("applicationPeriodId")) {
                    id = applicationPeriodObject.get("applicationPeriodId").getAsInt();
                }
                LocalDate start = LocalDate.parse(applicationPeriodObject.get("applicationPeriodStart").getAsString(), dtf);
                LocalDate end = LocalDate.parse(applicationPeriodObject.get("applicationPeriodEnd").getAsString(), dtf);
                PeriodChange applicationPeriod = new PeriodChange(PeriodType.Application, id, start, end);
                periods.add(applicationPeriod);
            }

            JsonObject electionPeriodObject = yearObject.getAsJsonObject("electionPeriod");
            if (electionPeriodObject.has("electionPeriodId")
                    || (electionPeriodObject.has("electionPeriodStart") && electionPeriodObject.has("electionPeriodEnd"))) {
                int id = Integer.MIN_VALUE;
                if (electionPeriodObject.has("electionPeriodId")) {
                    id = electionPeriodObject.get("electionPeriodId").getAsInt();
                }
                LocalDate start = LocalDate.parse(electionPeriodObject.get("electionPeriodStart").getAsString(), dtf);
                LocalDate end = LocalDate.parse(electionPeriodObject.get("electionPeriodEnd").getAsString(), dtf);
                PeriodChange electionPeriod = new PeriodChange(PeriodType.Election, id, start, end);
                periods.add(electionPeriod);
            }

            degree.addYear(year, periods);

        }

        return degree;

    }
}
