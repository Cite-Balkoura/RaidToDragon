package fr.grimtown.RaidToDragon.listeners;

import fr.grimtown.RaidToDragon.listeners.players.DeathLogic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ListenerManager implements Listener {

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        DeathLogic.run(event);
    }
}
