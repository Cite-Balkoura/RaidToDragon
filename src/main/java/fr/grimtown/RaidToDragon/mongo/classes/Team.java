package fr.grimtown.RaidToDragon.mongo.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;

import java.util.ArrayList;
import java.util.UUID;

@Entity(value = "team")
public class Team {
    @Indexed(options = @IndexOptions(unique = true, sparse = true))
    private String teamName;
    @Indexed(options = @IndexOptions(unique = true, sparse = true))
    private UUID owner;
    @Indexed(options = @IndexOptions(unique = true, sparse = true))
    private ArrayList<UUID> members;

    public Team() {}

    public String getTeamName() {
        return teamName;
    }

    public UUID getOwner() {
        return owner;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }
}
