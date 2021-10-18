package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinLogic {

    public static void run(final PlayerJoinEvent event) {
        event.setJoinMessage("");
        RaidPlugin.get().getServer().getWorlds()
                .stream().filter(world ->
                        world.getUID() != event.getPlayer().getWorld().getUID()
                ).findAny().ifPresent(world ->
                        event.getPlayer().setCompassTarget(world.getSpawnLocation())
                );
    }
}
