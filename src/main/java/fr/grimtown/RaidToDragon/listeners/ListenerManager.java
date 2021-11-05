package fr.grimtown.RaidToDragon.listeners;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.listeners.others.PiglinLogic;
import fr.grimtown.RaidToDragon.listeners.players.*;
import fr.grimtown.RaidToDragon.logs.Logger;
import fr.grimtown.RaidToDragon.logs.ReviveLog;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import fr.grimtown.RaidToDragon.utils.ReflectionUtils;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

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
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        ReviveLogic.run(event);
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
    public void onNPCRightClick(final NPCRightClickEvent event) {
        ReviveLogic.run(event);
    }

    @EventHandler
    public void onPlayerStopSpectatingEntity(final PlayerStopSpectatingEntityEvent event) {
        SpectatingLogic.run(event);
    }

    @EventHandler
    public void onEntityPortalEnter(final EntityPortalEnterEvent event) {
        if (event.getEntity() instanceof Player)
            WinLogic.run(event);
    }

    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        WinLogic.run(event);
    }
}
