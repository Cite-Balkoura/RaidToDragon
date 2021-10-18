package fr.grimtown.RaidToDragon.updaters.gadgets;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class XWatch extends GadgetUpdater {

    public XWatch(final Player player, final long time, final double radius) {
        super(player, time, radius);
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() > this.end) {
            this.cancel();
            return;
        }
        this.step++;
    }

    @Override
    public Gadget getType() {
        return Gadget.WATCH;
    }
}
