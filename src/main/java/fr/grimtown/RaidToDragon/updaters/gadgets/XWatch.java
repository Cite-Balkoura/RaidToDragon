package fr.grimtown.RaidToDragon.updaters.gadgets;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import java.util.List;

public class XWatch extends GadgetUpdater {

    private static final List<EntityType> ENTITY_TYPES = List.of(EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN, EntityType.PLAYER);

    private List<Entity> lastGlowing;

    public XWatch(final Player player, final GamePlayer gamePlayer, final long time, final double radius) {
        super(player, gamePlayer, time, radius);
    }

    /**
     * Beware : glowing effect doesn't match at all with shaders.
     * So please don't use this with shaders (even internal ones)
     */
    @Override
    public void run() {
        if (this.lastGlowing != null)
            this.lastGlowing.forEach(entity ->
                    GlowAPI.setGlowing(entity, false, this.player)
            );
        if (System.currentTimeMillis() > this.end) {
            this.cancel();
            this.gamePlayer.getActiveGadgets().remove(this);
            return;
        }
        this.step++;

        if (Config.get().getXWatchRadius() == -1)
            this.lastGlowing = this.player.getWorld().getEntities()
                    .stream().filter(entity -> ENTITY_TYPES.contains(entity.getType()))
                    .toList();
        else
            this.lastGlowing = this.player.getNearbyEntities(Config.get().getXWatchRadius(), Config.get().getXWatchRadius(), Config.get().getXWatchRadius())
                    .stream().filter(entity ->  ENTITY_TYPES.contains(entity.getType()))
                    .toList();
        GlowAPI.setGlowing(this.lastGlowing, GlowAPI.Color.valueOf(Config.get().getXWatchColor()), this.player);
    }

    @Override
    public Gadget getType() {
        return Gadget.WATCH;
    }
}
