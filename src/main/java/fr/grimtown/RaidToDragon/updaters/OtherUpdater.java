package fr.grimtown.RaidToDragon.updaters;

import fr.grimtown.RaidToDragon.entities.GameManager;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.updaters.gadgets.GadgetUpdater;
import fr.grimtown.RaidToDragon.updaters.gadgets.IndicCompass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class OtherUpdater extends BukkitRunnable {

    private final GameManager manager;

    public OtherUpdater(final GameManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void run() {
        Optional<Player> optionalPlayer;
        for (final GamePlayer gamePlayer : this.manager.getPlayers()) {
            if (gamePlayer.getActiveGadgets().stream()
                    .noneMatch(gadget ->
                            gadget.getType() == GadgetUpdater.Gadget.COMPASS && ((IndicCompass) gadget).hasFound())
            ) {
                optionalPlayer = GameAdapter.adapt(gamePlayer);
                optionalPlayer.ifPresent(player -> {
                    final Location location = player.getLocation().add((Math.random() - 0.5) * 64, 0, (Math.random() - 0.5) * 64);
                    player.setCompassTarget(location);
                });
            }
        }
    }

}
