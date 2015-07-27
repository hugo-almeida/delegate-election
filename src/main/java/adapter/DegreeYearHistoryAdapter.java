package adapter;

import java.lang.reflect.Type;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import core.ApplicationPeriod;
import core.DegreeYear;
import core.ElectionPeriod;
import core.Period;
import core.Period.PeriodType;

public class DegreeYearHistoryAdapter implements JsonSerializer<DegreeYear> {

    @Override
    public JsonElement serialize(DegreeYear degree, Type arg1, JsonSerializationContext arg2) {
        final JsonObject degreeYearJson = new JsonObject();
        final JsonArray periodsJson = new JsonArray();

        degreeYearJson.addProperty("degreeName", degree.getDegreeName());
        degreeYearJson.addProperty("degreeYear", degree.getDegreeYear());

        List<Period> periods = new ArrayList<Period>(degree.getInactivePeriods());
        if (degree.getActivePeriod() != null) {
            periods.add(degree.getActivePeriod());
        }

        periods.sort(new Comparator<Period>() {
            @Override
            public int compare(Period firstPeriod, Period secondPeriod) {
                return firstPeriod.getStart().atStartOfDay().toInstant(ZoneOffset.UTC)
                        .compareTo(secondPeriod.getStart().atStartOfDay().toInstant(ZoneOffset.UTC));
//                return 0;
            }
        });

        for (Period p : periods) {
            JsonObject periodJson = new JsonObject();
            periodJson.addProperty("academicYear", p.getDegreeYear().getCalendarYear() + "/"
                    + (p.getDegreeYear().getCalendarYear() + 1));
            periodJson.addProperty("periodType", p.getType().toString());
            periodJson.addProperty("start", p.getStart().toString());
            periodJson.addProperty("end", p.getEnd().toString());

            if (p.getType().equals(PeriodType.Application)) {
                periodJson.addProperty("info", ((ApplicationPeriod) p).getCandidateCount());
            } else if (p.getType().equals(PeriodType.Election)) {
                periodJson.addProperty("info", ((ElectionPeriod) p).getVotes().size());
            }

            periodsJson.add(periodJson);
        }

        degreeYearJson.add("periods", periodsJson);
        return degreeYearJson;
    }
}
