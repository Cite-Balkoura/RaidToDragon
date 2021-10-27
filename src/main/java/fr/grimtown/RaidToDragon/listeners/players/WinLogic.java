package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.logs.VictoryLog;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.World;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WinLogic {

    private static final List<UUID> POTENTIAL_WINNERS = new ArrayList<>();

    public static void run(final EntityPortalEnterEvent event) {
        POTENTIAL_WINNERS.add(event.getEntity().getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                POTENTIAL_WINNERS.remove(event.getEntity().getUniqueId());
            }
        }.runTaskLater(RaidPlugin.get(), 20L); // TODO : the time may be reduced
    }

    public static void run(final PlayerChangedWorldEvent event) {
        if (    !POTENTIAL_WINNERS.remove(event.getPlayer().getUniqueId())      ||
                event.getFrom().getEnvironment() != World.Environment.THE_END   ||
                event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL)
            return;
        // TODO : stop the game because the player just went into the end portal to go back in overworld
        new VictoryLog(event.getPlayer());
    }
}
