package fr.grimtown.RaidToDragon.logs;

import org.bukkit.entity.Player;

public class VictoryLog extends LogEvent {

    public VictoryLog(final Player player) {
        super(player.getUniqueId());
    }

}
