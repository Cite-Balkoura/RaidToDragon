package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ReviveLogic {

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;
        final ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() != Material.TOTEM_OF_UNDYING)
            return;
        GameAdapter.adapt(event.getPlayer()).ifPresent(gamePlayer -> {
            // TODO : Open a GUI to select a player in player's team
        });
    }
}
