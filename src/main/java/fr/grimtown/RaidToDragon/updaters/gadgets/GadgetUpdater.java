package fr.grimtown.RaidToDragon.updaters.gadgets;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public abstract class GadgetUpdater extends BukkitRunnable {

    protected final Player player;
    protected GamePlayer gamePlayer;
    protected double radius;

    protected int step;
    protected long end;

    public GadgetUpdater(final Player player, final GamePlayer gamePlayer, final long time, final double radius) {
        this.player = player;

        this.gamePlayer = gamePlayer;
        gamePlayer.getActiveGadgets().removeIf(gadget -> {
            if (gadget.getType() == this.getType()) {
                gadget.cancel();
                return true;
            }
            return false;
        });
        gamePlayer.getActiveGadgets().add(this);
        this.radius = radius;
        this.step = 0;
        this.end = System.currentTimeMillis() + time;
    }

    public Player getPlayer() {
        return this.player;
    }

    public abstract Gadget getType();

    public enum Gadget {
        COMPASS,
        WATCH
    }
}
