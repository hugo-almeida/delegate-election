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
        jsonObject.addProperty("name", s.getName());
        jsonObject.addProperty("username", s.getUsername());
        jsonObject2.add("data", parser.parse(s.getPhotoBytes()));
        jsonObject2.add("type", parser.parse(s.getPhotoType()));
        jsonObject.add("photo", jsonObject2);
        return jsonObject;
    }
}