package fr.grimtown.RaidToDragon.updaters.gadgets;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public abstract class GadgetUpdater extends BukkitRunnable {

    protected final Player player;
    protected Optional<GamePlayer> gamePlayer;
    protected double radius;

    protected int step;
    protected long end;

    public GadgetUpdater(final Player player, final long time, final double radius) {
        this.player = player;

        GameAdapter.adapt(player).ifPresentOrElse(gamePlayer -> {
            this.gamePlayer = Optional.of(gamePlayer);
            gamePlayer.getActiveGadgets().removeIf(gadget -> gadget.getType() == this.getType());
            gamePlayer.getActiveGadgets().add(this);
        }, () -> this.gamePlayer = Optional.empty());

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
