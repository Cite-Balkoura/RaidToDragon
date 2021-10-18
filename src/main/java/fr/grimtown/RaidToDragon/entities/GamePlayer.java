package fr.grimtown.RaidToDragon.entities;

import fr.grimtown.RaidToDragon.updaters.gadgets.GadgetUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GamePlayer {

    private final UUID uniqueId;
    private GameTeam team;

    private String[] lastKnownInventory;
    private List<GadgetUpdater> activeGadgets;

    private boolean realPlayer;

    private boolean dead;
    private boolean alreadyRevive;
    private double enderDamage;

    private long lastRegen;
    private long startTime;
    private long firstDeathTime;
    private long resurrectTime;
    private long deadTime;
    private long portalEndTime;

    public GamePlayer(final UUID uniqueId) {
        this.uniqueId = uniqueId;

        this.activeGadgets = new ArrayList<>();
        this.realPlayer = true;

        this.dead = false;
        this.alreadyRevive = false;
        this.enderDamage = 0;
        this.startTime = -1;
        this.deadTime = -1;
        this.portalEndTime = -1;
    }

    public void kill() {
        if (!this.alreadyRevive)
            this.firstDeathTime = this.deadTime;
        this.deadTime = System.currentTimeMillis();
        this.dead = true;
    }

    public void resurrect(final GamePlayer doctor) {
        if (this.alreadyRevive)
            return;
        this.alreadyRevive = true;
        this.resurrectTime = System.currentTimeMillis();
        this.deadTime = 0L;
        this.dead = false;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public GameTeam getTeam() {
        return this.team;
    }

    public void setTeam(final GameTeam team) {
        this.team = team;
    }

    public String[] getLastKnownInventory() {
        return this.lastKnownInventory;
    }

    public void setLastKnownInventory(final String[] lastKnownInventory) {
        this.lastKnownInventory = lastKnownInventory;
    }

    public List<GadgetUpdater> getActiveGadgets() {
        return this.activeGadgets;
    }

    public boolean isRealPlayer() {
        return this.realPlayer;
    }

    public void setRealPlayer(final boolean realPlayer) {
        this.realPlayer = realPlayer;
    }

    public boolean isDead() {
        return this.dead;
    }

    public boolean isAlive() {
        return !this.dead;
    }

    public boolean wasRevived() {
        return this.alreadyRevive;
    }

    public double getEnderDamage() {
        return this.enderDamage;
    }

    public void addEnderDamage(final double enderDamage) {
        this.enderDamage += enderDamage;
    }

    public long getLastRegen() {
        return this.lastRegen;
    }

    public void setLastRegen(final long lastRegen) {
        this.lastRegen = lastRegen;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    public long getDeadTime() {
        return this.deadTime;
    }

    public long getPortalEndTime() {
        return this.portalEndTime;
    }

    public void setPortalEndTime(final long portalEndTime) {
        this.portalEndTime = portalEndTime;
    }

    public long timeUntilEnter() {
        return this.portalEndTime - this.startTime;
    }
}
