package com.example.bulletinboard;

import com.example.bulletinboard.data.model.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject responseJson = json.getAsJsonObject();
        JsonObject userJson = responseJson.getAsJsonObject("user");

        User user = new User(
                userJson.get("email").getAsString(),
                userJson.get("username").getAsString(),
                userJson.get("password").getAsString(),
                userJson.get("_id").getAsString()
        );
        return user;
    }
}
