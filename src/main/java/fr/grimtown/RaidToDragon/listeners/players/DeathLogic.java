package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.libs.citizens.trait.SleepingTrait;
import fr.grimtown.RaidToDragon.logs.DeathLog;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import fr.grimtown.RaidToDragon.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.MountTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DeathLogic {

    public static final Map<NPC, Player> NPC_MAP = new WeakHashMap<>();
    public static final Map<UUID, NPC> UUID_MAP = new WeakHashMap<>();

    public static void run(final PlayerDeathEvent event) {
        new DeathLog(event.getEntity(),
                event.getEntity().getLastDamageCause() == null ?
                        EntityType.MARKER : event.getEntity().getLastDamageCause().getEntityType(),
                Optional.ofNullable(event.getEntity().getKiller()));
        // TODO : to complete later with fun messages
        GameAdapter.adapt(event.getEntity()).ifPresent(player -> {
            final Location location = event.getEntity().getLocation();
            location.setPitch(0.1f);
            player.kill();
            NPC npc = null;
            final boolean alone = Arrays.stream(player.getTeam().getPlayers()).noneMatch(GamePlayer::isAlive);
            if (!player.wasRevived() && !alone) {
                npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, String.valueOf(UUID.randomUUID()));
                npc.setName("");
                npc.setAlwaysUseNameHologram(false);
                npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                if (!npc.isSpawned())
                    npc.spawn(location, SpawnReason.PLUGIN);
                final SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);
                final String[] skin = Utils.getSkin(event.getEntity());
                trait.setSkinPersistent(event.getEntity().getUniqueId().toString(), skin[1], skin[0]);
                npc.setUseMinecraftAI(false);
                npc.setFlyable(true);
                npc.getNavigator().cancelNavigation();
                npc.addTrait(MountTrait.class);
                npc.getOrAddTrait(Gravity.class).setEnabled(true);
                final SleepingTrait sleep = npc.getOrAddTrait(SleepingTrait.class);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sleep.toSleep();
                        if (sleep.isSleeping())
                            cancel();
                    }
                }.runTaskTimer(RaidPlugin.get(), 0L, 2L);
                NPC_MAP.put(npc, event.getEntity());
                UUID_MAP.put(event.getEntity().getUniqueId(), npc);
            }
            final NPC finalNpc = npc;
            Bukkit.getScheduler().runTaskLater(RaidPlugin.get(), () -> {
                event.getEntity().spigot().respawn();
                event.getEntity().setGameMode(GameMode.SPECTATOR);
                if (finalNpc != null)
                    event.getEntity().setSpectatorTarget(finalNpc.getEntity());
                else // TODO : make player spectate other team mate or kick if all members of his team are dead
                    event.getEntity().teleport(location);
            }, 2L);
        });
    }
}
