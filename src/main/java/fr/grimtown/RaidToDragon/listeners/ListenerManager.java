package fr.grimtown.RaidToDragon.listeners;

import fr.grimtown.RaidToDragon.listeners.others.PiglinLogic;
import fr.grimtown.RaidToDragon.listeners.players.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ListenerManager implements Listener {

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        DeathLogic.run(event);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        JoinLogic.run(event);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        QuitLogic.run(event);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        PiglinLogic.run(event);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        GadgetLogic.run(event);
        ReviveLogic.run(event);
    }
}
