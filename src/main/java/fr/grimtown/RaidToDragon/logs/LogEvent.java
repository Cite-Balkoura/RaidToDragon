package fr.grimtown.RaidToDragon.logs;

import java.util.ArrayList;
import java.util.UUID;

public abstract class LogEvent {

    public static final ArrayList<LogEvent> EVENTS_LIST = new ArrayList<>();

    protected final UUID playerId;
    protected final long timestamp;

    protected LogEvent(final UUID playerId) {
        this.playerId = playerId;
        this.timestamp = System.currentTimeMillis();
        EVENTS_LIST.add(this);
    }

    public UUID getPlayer() {
        return this.playerId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
