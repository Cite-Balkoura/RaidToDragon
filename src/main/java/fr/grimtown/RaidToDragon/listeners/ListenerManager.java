package fr.grimtown.RaidToDragon.listeners;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.listeners.others.PiglinLogic;
import fr.grimtown.RaidToDragon.listeners.players.*;
import fr.grimtown.RaidToDragon.logs.ReviveLog;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        PositionLogic.run(event);
    }

    @EventHandler
    public void onInteractNPC(final NPCRightClickEvent event) {
        ReviveLogic.run(event);
    }

    // TODO : cancel leave spectate mode when player's dead and was not yet revived. Perhaps the event is already in paper so just analyse paper's code to reproduce the same behaviour here
}
