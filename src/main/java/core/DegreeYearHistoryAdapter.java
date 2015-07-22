package core;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DegreeYearHistoryAdapter implements JsonSerializer<DegreeYear> {

    @Override
    public JsonElement serialize(DegreeYear degree, Type arg1, JsonSerializationContext arg2) {
        final JsonObject periodJson = new JsonObject();

        return periodJson;
    }
}
