package fr.grimtown.RaidToDragon.entities;

import java.util.UUID;

public class GamePlayer {

    private final UUID uniqueId;

    private long firstTime;
    private long portalEndTime;

    public GamePlayer(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public long getFirstTime() {
        return this.firstTime;
    }

    public void setFirstTime(final long firstTime) {
        this.firstTime = firstTime;
    }

    public long getPortalEndTime() {
        return this.portalEndTime;
    }

    public void setPortalEndTime(final long portalEndTime) {
        this.portalEndTime = portalEndTime;
    }

    public long timeUntilEnter() {
        return this.portalEndTime - this.firstTime;
    }
}
