package core;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class VoteHolderAdapter implements JsonSerializer<Vote> {

    @Override
    public JsonElement serialize(Vote vote, Type type, JsonSerializationContext jsc) {
        JsonParser parser = new JsonParser();

        JsonObject voteObject = new JsonObject();
        JsonObject studentObject = new JsonObject();
        JsonObject photoObject = new JsonObject();

        voteObject.addProperty("istId", vote.getVoted());
        voteObject.addProperty("voteCount", ((ElectionPeriod) vote.getPeriod()).getVotes().size());

//        studentObject.addProperty("name", s.getName());
//        studentObject.addProperty("username", s.getUsername());
//
//        if (s.getPhotoBytes() == null) {
//            photoObject.add("data", parser.parse(""));
//            photoObject.add("type", parser.parse(""));
//        } else {
//            photoObject.add("data", parser.parse(s.getPhotoBytes()));
//            photoObject.add("type", parser.parse(s.getPhotoType()));
//        }
//
//        studentObject.add("photo", photoObject);
//        voteObject.add("student", studentObject);

        return voteObject;
    }
}