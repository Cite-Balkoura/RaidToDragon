package fr.grimtown.RaidToDragon.updaters;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.entities.GameManager;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.color.RegularColor;

public class TotemUpdater extends BukkitRunnable {

    private final int particles;
    private final double cycle;
    private int step;

    public TotemUpdater() {
        this.particles = Config.get().getTotemParticles();
        this.cycle = Config.get().getTotemCycle();
        this.step = 0;
    }

    @Override
    public void run() {
        this.step++;
        for (final Player player : RaidPlugin.get().getServer().getOnlinePlayers()) {
            if (RaidPlugin.get().getTotem().isSimilar(player.getInventory().getItemInMainHand())) {
                final GamePlayer gamePlayer = GameAdapter.adapt(player).orElse(null);
                if (gamePlayer == null || !gamePlayer.isOnline())
                    continue;
                Vector vector;
                Vector normalized;
                final Location playerLocation = player.getEyeLocation().subtract(0, 0.5, 0);
                for (final GamePlayer mates : gamePlayer.getTeam().getPlayers()) {
                    if (mates.getUniqueId().equals(gamePlayer.getUniqueId()))
                        continue;
                    if (mates.isDead() || !mates.isOnline())
                        vector = mates.getLastGroundPosition().clone().subtract(playerLocation).toVector();
                    else
                        vector = GameAdapter.adapt(mates).get().getLocation().clone().subtract(playerLocation).toVector();
                    normalized = vector.clone().normalize();
                    int maxLength = Config.get().getTotemLength();
                    if (maxLength == -1)
                        maxLength = (int) vector.length();
                    else
                        maxLength = (int) Math.min(vector.length(), maxLength);
                    for (int block = 0; block < maxLength; block++) {
                        for (int particle = 1; particle <= this.particles; particle++) {
                            new ParticleBuilder(ParticleEffect.REDSTONE)
                                    .setAmount(0)
                                    .setParticleData(new RegularColor(255, 20, 255))
                                    .setLocation(playerLocation.clone().add(normalized.clone().multiply(block).multiply(1D / this.particles).multiply(particle * (1 + (this.step / this.cycle) % 1))))
                                    .display(player);
                        }
                    }
                }
            }
        }
    }

}
