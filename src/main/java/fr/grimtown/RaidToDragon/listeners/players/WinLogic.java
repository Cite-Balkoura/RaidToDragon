package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.logs.VictoryLog;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.World;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        }.runTaskLater(RaidPlugin.get(), 10L); // TODO : the time may be reduced
    }

    public static void run(final PlayerChangedWorldEvent event) {
        if (    !POTENTIAL_WINNERS.remove(event.getPlayer().getUniqueId())      ||
                event.getFrom().getEnvironment() != World.Environment.THE_END   ||
                event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL)
            return;
        // TODO : stop the game because the player just went into the end portal to go back in overworld
        final Optional<GamePlayer> gamePlayer = GameAdapter.adapt(event.getPlayer());
        if (gamePlayer.isEmpty() || !gamePlayer.get().isRealPlayer() && gamePlayer.get().isAlive())
            return;

        final UUID[] winners = new UUID[gamePlayer.get().getTeam().getPlayers().length];
        for (int i = 0; i < winners.length; i++) {
            winners[i] = gamePlayer.get().getTeam().getPlayers()[i].getUniqueId();
        }
        new VictoryLog(event.getPlayer(), winners);
    }
}
