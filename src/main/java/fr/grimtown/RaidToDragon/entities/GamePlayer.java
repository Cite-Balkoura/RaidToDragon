package fr.grimtown.RaidToDragon.entities;

import java.util.UUID;

public class GamePlayer {

    private final UUID uniqueId;

    private boolean alreadyRevive;
    private double enderDamage;

    private long startTime;
    private long firstDeathTime;
    private long resurrectTime;
    private long deadTime;
    private long portalEndTime;

    public GamePlayer(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.alreadyRevive = false;
        this.enderDamage = 0;
        this.startTime = -1;
        this.deadTime = -1;
        this.portalEndTime = -1;
    }

    public void resurrect(final GamePlayer doctor) {
        if (this.alreadyRevive)
            return;
        this.alreadyRevive = true;
        this.resurrectTime = System.currentTimeMillis();
        this.deadTime = 0L;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public boolean wasRevived() {
        return this.alreadyRevive;
    }

    public double getEnderDamage() {
        return this.enderDamage;
    }

    public void setEnderDamage(final double enderDamage) {
        this.enderDamage = enderDamage;
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

    public void setDeadTime(final long deadTime) {
        if (this.wasRevived())
            this.firstDeathTime = deadTime;
        this.deadTime = deadTime;
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
