package fr.grimtown.RaidToDragon.mongo;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.RaidToDragon.mongo.classes.Team;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.configuration.Configuration;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

public class MongoData {
    /**
     * Get the datastore, see {@link MongoConnect#initDatastore(Configuration)}
     */
    private static final Datastore DATASTORE = RaidPlugin.getDatastore();

    /**
     * Get a team with an uuid of a member
     * @param playerUuid uuid of a member
     * @return null if no team found for this UUID
     */
    @Nullable
    public static Team getTeam(UUID playerUuid) {
        return DATASTORE.find(Team.class)
                .filter(Filters.in("members", Collections.singletonList(playerUuid)))
                .first();
    }

    /**
     * Get a team with the name of the team
     * @param teamName name of team
     * @return null if no team found with this name
     */
    @Nullable
    public static Team getTeam(String teamName) {
        return DATASTORE.find(Team.class)
                .filter(Filters.eq("teamName", teamName))
                .first();
    }
}
