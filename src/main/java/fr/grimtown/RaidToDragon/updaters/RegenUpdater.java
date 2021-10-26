package fr.grimtown.RaidToDragon.updaters;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenUpdater extends BukkitRunnable {

    private final double regen;
    private final long time;

    public RegenUpdater() {
        this.regen = Config.get().getRegenStrength();
        this.time = Config.get().getRegenTime();
    }

    @Override
    public void run() {
        final long now = System.currentTimeMillis();
        RaidPlugin.get().getGameManager().getPlayers().values().stream().filter(GamePlayer::isAlive).filter(GamePlayer::isOnline).forEach(player -> {
            if (player.getLastRegen() + this.time <= now) {
                player.setLastRegen(now);
                GameAdapter.adapt(player).ifPresent(bukkitPlayer ->
                    bukkitPlayer.setHealth(Math.min(bukkitPlayer.getHealth() + this.regen, 20D))
                );
            }
        });
    }

}
