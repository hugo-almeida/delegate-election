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

import core.Period.PeriodType;
import core.exception.InvalidPeriodException;

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

        String degreeId = periodObject.get("degreeId").getAsString();
        Degree degree = new Degree(null, null, degreeId, null, null);

        JsonArray years = periodObject.get("years").getAsJsonArray();

        for (JsonElement yearElement : years) {
            JsonObject yearObject = yearElement.getAsJsonObject();

            int year = yearObject.get("degreeYear").getAsInt();

            degree.addYear(year);

            if (yearObject.has("applicationPeriodStart") && yearObject.has("applicationPeriodEnd")) {
                int id = Integer.MIN_VALUE;
                if (yearObject.has("applicationPeriodId")) {
                    id = yearObject.get("applicationPeriodId").getAsInt();
                }
                LocalDate start = LocalDate.parse(yearObject.get("applicationPeriodStart").getAsString());
                LocalDate end = LocalDate.parse(yearObject.get("applicationPeriodEnd").getAsString());
                PeriodChange applicationPeriod = new PeriodChange(PeriodType.Application, id, start, end);
                try {
                    degree.getDegreeYear(year).addPeriod(applicationPeriod);
                } catch (InvalidPeriodException e) {
                    // Wut r u doing!??!
                }
            }

            if (yearObject.has("electionPeriodStart") && yearObject.has("electionPeriodEnd")) {
                int id = Integer.MIN_VALUE;
                if (yearObject.has("electionPeriodId")) {
                    id = yearObject.get("electionPeriodId").getAsInt();
                }
                LocalDate start = LocalDate.parse(yearObject.get("electionPeriodStart").getAsString());
                LocalDate end = LocalDate.parse(yearObject.get("electionPeriodEnd").getAsString());
                PeriodChange electionPeriod = new PeriodChange(PeriodType.Election, id, start, end);
                try {
                    degree.getDegreeYear(year).addPeriod(electionPeriod);
                } catch (InvalidPeriodException e) {
                    // Wut r u doing!??!
                }
            }

        }

        return degree;

    }

    public class PeriodChange extends Period {

        PeriodType periodType;

        int periodId;

        public PeriodChange(PeriodType periodType, int periodId, LocalDate start, LocalDate end) {
            super(start, end);

            this.periodType = periodType;
            this.periodId = periodId;
        }

        @Override
        public PeriodType getType() {
            return periodType;
        }

        public PeriodType getPeriodType() {
            return periodType;
        }

        public void setPeriodType(PeriodType periodType) {
            this.periodType = periodType;
        }

        public int getPeriodId() {
            return periodId;
        }

        public void setPeriodId(int periodId) {
            this.periodId = periodId;
        }

    }
}
