package core;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class StudentAdapter implements JsonSerializer<Student> {
    @Override
    public JsonElement serialize(Student s, Type type, JsonSerializationContext jsc) {
        Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
//        JsonObject periodJson = new JsonObject();
        jsonObject.addProperty("name", s.getName());
        jsonObject.addProperty("username", s.getUsername());
//        if (s.getDegreeYear().getActivePeriod() != null) {
//            periodJson.addProperty("start", s.getDegreeYear().getActivePeriod().getStart().toString());
//            periodJson.addProperty("end", s.getDegreeYear().getActivePeriod().getEnd().toString());
//        }
        if (s.getPhotoBytes() == null) {
            jsonObject2.add("data", parser.parse(""));
            jsonObject2.add("type", parser.parse(""));
        } else {
            jsonObject2.add("data", parser.parse(s.getPhotoBytes()));
            jsonObject2.add("type", parser.parse(s.getPhotoType()));
        }
        jsonObject.add("photo", jsonObject2);
//        jsonObject.add("currentPeriod", periodJson);
        return jsonObject;
    }
}