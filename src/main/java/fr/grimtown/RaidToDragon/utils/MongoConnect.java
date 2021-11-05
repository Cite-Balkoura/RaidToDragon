package fr.grimtown.RaidToDragon.utils;

import com.google.gson.JsonObject;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.logs.LogEvent;

import java.util.UUID;

public class MongoConnect {

    // retrieve JSON from MongoDB
    public static JsonObject getJSON(final String uniqueId) {
        return new JsonObject();
    }

    // from JSON to GamePlayer
    public static GamePlayer getGamePlayer(final JsonObject json) {
        return new GamePlayer(UUID.fromString(json.get("uuid").getAsString()));
    }

    // insert Event into MongoDB
    public static void insertEvent(final LogEvent event) {
    }

    // insert GamePlayer into MongoDB
    public static void insertGamePlayer(final GamePlayer player) {
    }

}
