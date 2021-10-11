package fr.grimtown.RaidToDragon.plugin;

import fr.grimtown.RaidToDragon.commands.CommandManager;
import fr.grimtown.RaidToDragon.entities.GameManager;
import fr.grimtown.RaidToDragon.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RaidPlugin extends JavaPlugin {

    public static RaidPlugin get() {
        return JavaPlugin.getPlugin(RaidPlugin.class);
    }

    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        this.commandManager = new CommandManager();
        this.getCommand("raidtodragon").setExecutor(this.commandManager);
        this.getCommand("raidtodragon").setTabCompleter(this.commandManager);

        this.listenerManager = new ListenerManager();
        this.getServer().getPluginManager().registerEvents(this.listenerManager, this);

        this.gameManager = new GameManager();
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
}
