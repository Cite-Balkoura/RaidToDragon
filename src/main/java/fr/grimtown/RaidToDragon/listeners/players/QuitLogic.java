package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitLogic {

    public static void run(final PlayerQuitEvent event) {
        event.setQuitMessage("");
        RaidPlugin.get().unregister(event.getPlayer());
    }

}
