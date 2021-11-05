package fr.grimtown.RaidToDragon.logs;

import org.bukkit.entity.Player;

import java.util.UUID;

public class VictoryLog extends LogEvent {

    private final UUID[] players;

    public VictoryLog(final Player player, final UUID[] players) {
        super(player.getUniqueId());
        this.players = players;
    }

    public UUID[] getPlayers() {
        return this.players;
    }
}
