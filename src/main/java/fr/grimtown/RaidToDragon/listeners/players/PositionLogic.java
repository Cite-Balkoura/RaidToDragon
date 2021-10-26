package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerMoveEvent;

public class PositionLogic {

    public static void run(final PlayerMoveEvent event) {
        GameAdapter.adapt(event.getPlayer()).ifPresent(player -> {
            if (((LivingEntity) event.getPlayer()).isOnGround() | player.getLastGroundPosition() == null)
                player.setLastGroundPosition(event.getPlayer().getLocation());
        });
    }
}
