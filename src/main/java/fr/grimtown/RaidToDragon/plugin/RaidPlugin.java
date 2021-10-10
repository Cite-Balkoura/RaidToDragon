package fr.grimtown.RaidToDragon.plugin;

import fr.grimtown.RaidToDragon.entities.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RaidPlugin extends JavaPlugin {

    public static RaidPlugin get() {
        return JavaPlugin.getPlugin(RaidPlugin.class);
    }

    private GameManager gameManager;

    @Override
    public void onEnable() {
        this.gameManager = new GameManager();
    }
}
