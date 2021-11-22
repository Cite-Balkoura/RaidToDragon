package fr.grimtown.RaidToDragon.plugin;

import dev.morphia.Datastore;
import fr.grimtown.RaidToDragon.commands.CommandManager;
import fr.grimtown.RaidToDragon.commands.handle.StartCommand;
import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.config.Messages;
import fr.grimtown.RaidToDragon.config.Permissions;
import fr.grimtown.RaidToDragon.entities.GameManager;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.GameTeam;
import fr.grimtown.RaidToDragon.entities.adapters.GameAdapter;
import fr.grimtown.RaidToDragon.libs.citizens.trait.SleepingTrait;
import fr.grimtown.RaidToDragon.listeners.ListenerManager;
import fr.grimtown.RaidToDragon.listeners.players.DeathLogic;
import fr.grimtown.RaidToDragon.mongo.MongoConnect;
import fr.grimtown.RaidToDragon.utils.ItemSerializer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RaidPlugin extends JavaPlugin {

    public static RaidPlugin get() {
        return JavaPlugin.getPlugin(RaidPlugin.class);
    }

    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private GameManager gameManager;

    private static Datastore datastore;

    private NamespacedKey watchKey;
    private ItemStack watch;

    private NamespacedKey compassKey;
    private ItemStack compass;

    private NamespacedKey totemKey;
    private ItemStack totem;

    @Override
    public void onEnable() {
        /*
          Generate all configs
         */
        Config.get().init(this.getDataFolder().getAbsolutePath(), "config.yml");
        Messages.init(this.getDataFolder().getAbsolutePath(), "messages.yml");
        Permissions.init(this.getDataFolder().getAbsolutePath(), "permissions.yml");

        /*
          Init MongoClient (Morphia Datastore)
         */
        datastore = new MongoConnect().initDatastore(this.getConfig());

        /*
          Generate all crafts
         */
        this.watchKey = new NamespacedKey(this, "x_watch");
        this.watch = new ItemStack(Material.CLOCK);

        if (this.watch.getItemMeta() != null) {
            final ItemMeta watchMeta = this.watch.getItemMeta();
            watchMeta.setDisplayName(Config.get().getXWatchName());
            watchMeta.setLore(Arrays.asList(Config.get().getXWatchLore()));
            this.watch.setItemMeta(watchMeta);
        }

        final ShapedRecipe watchRecipe = new ShapedRecipe(this.watchKey, this.watch);

        watchRecipe.shape(" B ", "RGR", "III");
        watchRecipe.setIngredient('B', Material.BELL);
        watchRecipe.setIngredient('R', Material.REDSTONE);
        watchRecipe.setIngredient('G', Material.RAW_GOLD_BLOCK);
        watchRecipe.setIngredient('I', Material.RAW_IRON_BLOCK);

        this.getServer().addRecipe(watchRecipe);


        this.compassKey = new NamespacedKey(this, "indic_compass");
        this.compass = new ItemStack(Material.COMPASS, 1);
        if (this.compass.getItemMeta() != null) {
            final CompassMeta compassMeta = (CompassMeta) this.compass.getItemMeta();
            compassMeta.setDisplayName(Config.get().getIndicCompassName());
            compassMeta.setLore(Arrays.asList(Config.get().getIndicCompassLore()));
            compassMeta.setLodestoneTracked(false);
            this.compass.setItemMeta(compassMeta);
        }
        final ShapedRecipe compassRecipe = new ShapedRecipe(this.compassKey, this.compass);

        compassRecipe.shape("RAR", "III", "CCC");
        compassRecipe.setIngredient('R', Material.REDSTONE);
        compassRecipe.setIngredient('A', Material.ARROW);
        compassRecipe.setIngredient('I', Material.RAW_IRON_BLOCK);
        compassRecipe.setIngredient('C', Material.RAW_COPPER_BLOCK);

        this.getServer().addRecipe(compassRecipe);


        this.totemKey = new NamespacedKey(this, "totem");
        this.totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        if (this.totem.getItemMeta() != null) {
            final ItemMeta totemMeta = this.totem.getItemMeta();
            totemMeta.setDisplayName(Config.get().getGadgetsTotemName());
            totemMeta.setLore(Arrays.asList(Config.get().getGadgetsTotemLore()));
            this.totem.setItemMeta(totemMeta);
        }
        final ShapedRecipe totemRecipe = new ShapedRecipe(this.totemKey, this.totem);

        totemRecipe.shape(" G ", "CEC", " A ");
        totemRecipe.setIngredient('G', Material.GOLDEN_HELMET);
        totemRecipe.setIngredient('C', Material.LIGHTNING_ROD);
        totemRecipe.setIngredient('E', Material.ENDER_EYE);
        totemRecipe.setIngredient('A', Material.ARMOR_STAND);

        this.getServer().addRecipe(totemRecipe);

        /*
          Create managers
         */
        CitizensAPI.getTraitFactory().getRegisteredTraits()
                .stream().filter(traitInfo -> traitInfo.getTraitName().equalsIgnoreCase("sleepingRaidToDragon"))
                .collect(Collectors.toList())
                .forEach(traitInfo -> CitizensAPI.getTraitFactory().deregisterTrait(traitInfo));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(SleepingTrait.class).withName("sleepingRaidToDragon"));

        this.commandManager = new CommandManager();
        this.getCommand("raidtodragon").setExecutor(this.commandManager);
        this.getCommand("raidtodragon").setTabCompleter(this.commandManager);
        this.commandManager.registerCommands(new StartCommand());

        this.listenerManager = new ListenerManager();
        this.getServer().getPluginManager().registerEvents(this.listenerManager, this);

        this.gameManager = new GameManager();

        this.getServer().getOnlinePlayers().forEach(this::register);
        this.gameManager.start();
    }

    @Override
    public void onDisable() {

        DeathLogic.NPC_MAP.forEach((npc, player) -> {
            if (player != null && player.isOnline()) {
                player.setSpectatorTarget(null);
                player.setGameMode(GameMode.ADVENTURE);
            }
            npc.despawn();
            npc.destroy();
        });

        CitizensAPI.getTraitFactory().getRegisteredTraits()
                .stream().filter(traitInfo -> traitInfo.getTraitName().equalsIgnoreCase("sleepingRaidToDragon"))
                .collect(Collectors.toList())
                .forEach(traitInfo -> CitizensAPI.getTraitFactory().deregisterTrait(traitInfo));

        this.getServer().removeRecipe(this.watchKey);
        this.getServer().removeRecipe(this.compassKey);
        this.getServer().removeRecipe(this.totemKey);
        this.gameManager.stop();
    }

    public void register(final Player player) {
        if (this.gameManager.getPlayers().containsKey(player.getUniqueId())) {
            this.gameManager.getPlayers().get(player.getUniqueId()).setOnline(true);
            return;
        }
        if (!this.gameManager.isStarted())
            player.teleport(this.gameManager.getWorld().getSpawnLocation());
        final GamePlayer gamePlayer = new GamePlayer(player.getUniqueId());
        final GameTeam gameTeam = new GameTeam(new GamePlayer[] { gamePlayer });
        gamePlayer.setTeam(gameTeam);
        this.getGameManager().getPlayers().put(player.getUniqueId(), gamePlayer);
    }

    public void unregister(final Player player) {
        GameAdapter.adapt(player).ifPresent(gamePlayer -> {
            gamePlayer.setLastKnownInventory(ItemSerializer.playerInventoryToBase64(player.getInventory()));
            gamePlayer.setOnline(false);
        });
    }

    /**
     * Get the datastore init in {@link #onEnable()}
     * @return loaded datastore
     */
    public static Datastore getDatastore() {
        return datastore;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public ListenerManager getListenerManager() {
        return this.listenerManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public ItemStack getWatch() {
        return this.watch;
    }

    public ItemStack getCompass() {
        return this.compass;
    }

    public ItemStack getTotem() {
        return this.totem;
    }
}
