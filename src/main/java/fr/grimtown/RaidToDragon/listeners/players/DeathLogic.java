package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathLogic {

    public static void run(final PlayerDeathEvent event) {
        // TODO : to complete later with fun messages
        RaidPlugin.get().getGameManager().getPlayers()
                .stream().filter(player -> player.getUniqueId().equals(event.getEntity().getUniqueId()))
                .forEach(player -> {
                    player.setDeadTime(System.currentTimeMillis());
                    // TODO : death logic : spectator, etc.
                });
    }
}
