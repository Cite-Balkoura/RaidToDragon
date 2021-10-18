package fr.grimtown.RaidToDragon.plugin;

import fr.grimtown.RaidToDragon.commands.CommandManager;
import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.config.Messages;
import fr.grimtown.RaidToDragon.config.Permissions;
import fr.grimtown.RaidToDragon.entities.GameManager;
import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.GameTeam;
import fr.grimtown.RaidToDragon.listeners.ListenerManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class RaidPlugin extends JavaPlugin {

    public static RaidPlugin get() {
        return JavaPlugin.getPlugin(RaidPlugin.class);
    }

    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private GameManager gameManager;

    private NamespacedKey watchKey;
    private ItemStack watch;
    private ShapedRecipe watchRecipe;

    private NamespacedKey compassKey;
    private ItemStack compass;
    private ShapedRecipe compassRecipe;

    @Override
    public void onEnable() {
        /*
          Generate all configs
         */
        Config.get().init(this.getDataFolder().getAbsolutePath(), "config.yml");
        Messages.init(this.getDataFolder().getAbsolutePath(), "messages.yml");
        Permissions.init(this.getDataFolder().getAbsolutePath(), "permissions.yml");

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

        this.watchRecipe = new ShapedRecipe(this.watchKey, this.watch);

        this.watchRecipe.shape(" B ", "RGR", "III");
        this.watchRecipe.setIngredient('B', Material.BELL);
        this.watchRecipe.setIngredient('R', Material.REDSTONE);
        this.watchRecipe.setIngredient('G', Material.RAW_GOLD_BLOCK);
        this.watchRecipe.setIngredient('I', Material.RAW_IRON_BLOCK);

        this.getServer().addRecipe(this.watchRecipe);


        this.compassKey = new NamespacedKey(this, "indic_compass");
        this.compass = new ItemStack(Material.COMPASS, 1);
        if (this.compass.getItemMeta() != null) {
            final CompassMeta compassMeta = (CompassMeta) this.compass.getItemMeta();
            compassMeta.setDisplayName(Config.get().getIndicCompassName());
            compassMeta.setLore(Arrays.asList(Config.get().getIndicCompassLore()));
            compassMeta.setLodestoneTracked(false);
            this.compass.setItemMeta(compassMeta);
        }
        this.compassRecipe = new ShapedRecipe(this.compassKey, this.compass);

        this.compassRecipe.shape("RAR", "III", "CCC");
        this.compassRecipe.setIngredient('R', Material.REDSTONE);
        this.compassRecipe.setIngredient('A', Material.ARROW);
        this.compassRecipe.setIngredient('I', Material.RAW_IRON_BLOCK);
        this.compassRecipe.setIngredient('C', Material.RAW_COPPER_BLOCK);

        this.getServer().addRecipe(this.compassRecipe);

        /*
          Create managers
         */
        this.commandManager = new CommandManager();
        this.getCommand("raidtodragon").setExecutor(this.commandManager);
        this.getCommand("raidtodragon").setTabCompleter(this.commandManager);

        this.listenerManager = new ListenerManager();
        this.getServer().getPluginManager().registerEvents(this.listenerManager, this);

        this.gameManager = new GameManager();

        this.getServer().getOnlinePlayers().forEach(this::register);
        this.gameManager.start();
    }

    @Override
    public void onDisable() {
        this.getServer().removeRecipe(this.watchKey);
        this.getServer().removeRecipe(this.compassKey);
        this.gameManager.stop();
    }

    public void register(final Player player) {
        final GamePlayer gamePlayer = new GamePlayer(player.getUniqueId());
        final GameTeam gameTeam = new GameTeam(new GamePlayer[] { gamePlayer });
        gamePlayer.setTeam(gameTeam);
        this.getGameManager().getPlayers().add(gamePlayer);
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
}
