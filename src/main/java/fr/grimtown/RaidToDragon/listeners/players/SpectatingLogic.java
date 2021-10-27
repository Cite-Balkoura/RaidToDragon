package fr.grimtown.RaidToDragon.listeners.players;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;

public class SpectatingLogic {

    public static void run(final PlayerStopSpectatingEntityEvent event) {
        GameAdapter.adapt(event.getPlayer()).ifPresent(player -> {
            if (player.isDead() && player.isRealPlayer())
                event.setCancelled(true);
        });
    }
}
