package core;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class VoteHolderAdapter implements JsonSerializer<VoteHolder> {
    @Override
    public JsonElement serialize(VoteHolder s, Type type, JsonSerializationContext jsc) {
        JsonObject voteObject = new JsonObject();
        voteObject.addProperty("istId", s.getVoted());
        voteObject.addProperty("voteCount", s.getVotes().size());
        return voteObject;
    }

}