package fr.grimtown.RaidToDragon.listeners.players;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.config.Messages;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.plugin.Logger;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import fr.grimtown.RaidToDragon.updaters.gadgets.IndicCompass;
import fr.grimtown.RaidToDragon.updaters.gadgets.XWatch;
import fr.grimtown.RaidToDragon.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GadgetLogic {

    public static void run(final PlayerInteractEvent event) {
        if (RaidPlugin.get().getCompass().isSimilar(event.getItem())) {

            final int slot = event.getPlayer().getInventory().first(Material.COPPER_BLOCK);
            if (checkCombustible(event, slot)) {
                Logger.log(event.getPlayer(), Messages.ERROR_GADGET_COMBUSTIBLE.get("de cuivre"));
                return;
            }
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                GameAdapter.adapt(event.getPlayer()).ifPresent(gamePlayer -> {
                            if (gamePlayer.getTeam().getLastKnowPosition() == -1) {
                                Logger.log(event.getPlayer(), Messages.INFO_GADGET_COMPASS_PASSIVE_NOT__FOUND.get());
                            } else {
                                Logger.log(event.getPlayer(), Messages.INFO_GADGET_COMPASS_PASSIVE_FOUND.get(
                                        Utils.fromDuration(System.currentTimeMillis() - gamePlayer.getTeam().getLastKnowPosition())
                                ));
                            }
                        });
                return;
            }
            new IndicCompass(event.getPlayer(), Config.get().getIndicCompassTime(), Config.get().getIndicCompassRadius())
                    .runTaskTimer(RaidPlugin.get(), 0L, Config.get().getIndicCompassUpdate());

        } else if (RaidPlugin.get().getWatch().isSimilar(event.getItem())) {
            final int slot = event.getPlayer().getInventory().first(Material.GOLD_BLOCK);
            if (checkCombustible(event, slot)) {
                Logger.log(event.getPlayer(), Messages.ERROR_GADGET_COMBUSTIBLE.get("d'or"));
                return;
            }
            new XWatch(event.getPlayer(), Config.get().getXWatchTime(), Config.get().getXWatchRadius())
                    .runTaskTimer(RaidPlugin.get(), 0L, Config.get().getXWatchUpdate());
        }
    }

    private static boolean checkCombustible(final PlayerInteractEvent event, final int slot) {
        if (slot == -1) {
            return true;
        }
        final ItemStack combustible = event.getPlayer().getInventory().getItem(slot);
        assert combustible != null;
        combustible.setAmount(combustible.getAmount() - 1);
        if (combustible.getAmount() == 0)
            event.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
        else
            event.getPlayer().getInventory().setItem(slot, combustible);
        return false;
    }
}
