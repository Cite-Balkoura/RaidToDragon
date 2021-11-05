package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.libs.weakutilities.WeakHashSet;
import fr.grimtown.RaidToDragon.logs.ReviveLog;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ReviveLogic {

    private static final WeakHashSet<LightningStrike> STRIKES = new WeakHashSet<>();

    public static void run(final NPCRightClickEvent event) {
        final ItemStack item = event.getClicker().getInventory().getItemInMainHand();
        if (RaidPlugin.get().getTotem().isSimilar(item)) {
            final Player player = DeathLogic.NPC_MAP.get(event.getNPC());
            if (player == null || !GameAdapter.inSameTeam(player, event.getClicker()))
                return;
            GameAdapter.adapt(event.getClicker()).ifPresent(doctor -> {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() > 0)
                    event.getClicker().getInventory().setItemInMainHand(item);
                else
                    event.getClicker().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                event.getNPC().despawn(DespawnReason.REMOVAL);
                DeathLogic.NPC_MAP.remove(event.getNPC());
                new ReviveLog(player, event.getClicker());
                player.setSpectatorTarget(null);
                player.teleport(event.getNPC().getStoredLocation());
                player.setGameMode(GameMode.SURVIVAL);
                GameAdapter.adapt(player).ifPresent(gamePlayer -> gamePlayer.resurrect(doctor));
                event.getNPC().destroy();
                STRIKES.add(player.getWorld().strikeLightningEffect(player.getLocation().add(0, 10, 0)));
            });
        }
    }

    @EventHandler
    public static void run(final EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            if (event.getDamager() instanceof LightningStrike) {
                if (STRIKES.remove(event.getDamager()))
                    event.setCancelled(true);
            }
        }
    }
}
