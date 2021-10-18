package fr.grimtown.RaidToDragon.updaters.gadgets;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.config.Messages;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.plugin.Logger;
import fr.grimtown.RaidToDragon.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.Optional;

public class IndicCompass extends GadgetUpdater {

    private boolean found;

    public IndicCompass(final Player player, final long time, final double radius) {
        super(player, time, radius);
        this.found = false;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() > this.end) {
            this.cancel();
            this.gamePlayer.ifPresent(gamePlayer -> gamePlayer.getActiveGadgets().remove(this));
            if (!this.found)
                Logger.log(this.player, Messages.INFO_GADGET_COMPASS_ACTIVE_FAIL.get());
            return;
        }
        this.step++;

        double distance = Double.MAX_VALUE;
        double localDistance = -1;
        Optional<GamePlayer> gamePlayer = Optional.empty();
        for (final Player players : this.player.getWorld().getLivingEntities()
                .stream().filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .filter(players -> !GameAdapter.inSameTeam(this.player, players)).toList()
        ) {
            localDistance = players.getLocation().distanceSquared(this.player.getLocation());
            if (localDistance < distance && (Config.get().getIndicCompassRadius() == -1 || Config.get().getIndicCompassRadius() >= localDistance)) {
                distance = localDistance;
                gamePlayer = GameAdapter.adapt(players);
            }
        }
        if (localDistance == -1 || gamePlayer.isEmpty())
            return;
        this.found = true;
        gamePlayer.get().getTeam().setLastKnowPosition(System.currentTimeMillis());
        GameAdapter.adapt(gamePlayer.get()).ifPresent(opponent -> {
            this.player.setCompassTarget(opponent.getLocation());
            this.player.getInventory().forEach(itemStack -> {
                if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof CompassMeta compass) {
                    compass.setLodestoneTracked(true);
                    compass.setLodestone(opponent.getLocation());
                }
            });
        });
        Utils.sendActionBar(this.player, Messages.INFO_GADGET_COMPASS_ACTIVE_FOUND.get(Utils.truncate(Math.sqrt(distance), 2)));
    }

    @Override
    public Gadget getType() {
        return Gadget.COMPASS;
    }

    public boolean hasFound() {
        return this.found;
    }
}
