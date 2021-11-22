package fr.grimtown.RaidToDragon.mongo;

import com.google.gson.JsonObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.logs.LogEvent;
import fr.grimtown.RaidToDragon.mongo.classes.Team;
import org.bson.UuidRepresentation;
import org.bukkit.configuration.Configuration;

import java.util.Collections;

public class MongoConnect {

    // retrieve JSON from MongoDB
    public static JsonObject getJSON(final String uniqueId) {
        return new JsonObject();
    }

    // from JSON to GamePlayer
    public static PlayerData getPlayer(final JsonObject json) {
        return new PlayerData();
    }

    // insert Event into MongoDB
    public static void insertEvent(final LogEvent event) {
    }

    // insert GamePlayer into MongoDB
    public static void insertGamePlayer(final GamePlayer player) {
    }

    /**
     * MongoDB Connection (Morphia Datastore) to query
     */
    public Datastore initDatastore(Configuration config) {
        MongoCredential credential = MongoCredential.createCredential(
                config.getString("data.mongo.user"),
                config.getString("data.mongo.db"),
                config.getString("data.mongo.password").toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY) // TODO: 22/11/2021 Sadly, our BDD used JAVA_LEGACY instead of STANDARD... Maybe we have to convert all but... yeah xD
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(config.getString("data.mongo.host"), config.getInt("data.mongo.port")))))
                .credential(credential)
                .build();
        Datastore datastore = Morphia.createDatastore(MongoClients.create(settings), config.getString("data.mongo.database"), MapperOptions.builder()
                .enablePolymorphicQueries(true)
                .build());
        datastore.getMapper().map(Team.class);
        datastore.ensureIndexes();
        datastore.ensureCaps();
        datastore.enableDocumentValidation();
        return datastore;
    }
}
