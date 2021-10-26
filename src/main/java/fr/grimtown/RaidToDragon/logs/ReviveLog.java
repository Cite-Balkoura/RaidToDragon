package fr.grimtown.RaidToDragon.logs;

import org.bukkit.entity.Player;

import java.util.UUID;

public class ReviveLog extends LogEvent {

    private final UUID healer;

    public ReviveLog(final Player formerDeath, final Player healer) {
        super (formerDeath.getUniqueId());
        this.healer = healer.getUniqueId();
    }

    public UUID getHealer() {
        return this.healer;
    }
}
