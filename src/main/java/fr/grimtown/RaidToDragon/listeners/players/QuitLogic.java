package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.utils.ItemSerializer;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitLogic {

    public static void run(final PlayerQuitEvent event) {
        event.setQuitMessage("");
        GameAdapter.adapt(event.getPlayer()).ifPresent(player -> {
                player.setLastKnownInventory(ItemSerializer.playerInventoryToBase64(event.getPlayer().getInventory()));
                player.setOnline(false);
        });
    }

}
