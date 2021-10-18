package fr.grimtown.RaidToDragon.config;

import fr.grimtown.RaidToDragon.utils.FileUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Config {

    private static Config instance;

    public static Config get() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

    private boolean debug = true;

    private double regen_strength = 0.5D;
    private long regen_time = 5 * 60 * 60 * 1000L;
    private long regen_update = 15 * 20L;

    private String x$watch_name = "&3Montre à rayons X";
    private String x$watch_lore = "(>: new||line :<)";
    private long x$watch_time = 3 * 1000L;
    private long x$watch_update = 10L;
    private double x$watch_radius = 50D;

    private String indic$compass_name = "&3Boussole d'indication";
    private String indic$compass_lore = "(>: new||line :<)";
    private long indic$compass_time = 6 * 1000L;
    private long indic$compass_update = 10L;
    private double indic$compass_radius = -1D;

    private long other_update = 20L;

    private Config() {
    }

    public void init(final String folder, final String fileName) {
        final File file = new File(folder, fileName);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (final Field field : this.getClass().getDeclaredFields()) {
                final String path = field.getName().replace("_", ".").replace("$", "-");
                if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()))
                    continue;
                if (Modifier.isFinal(field.getModifiers())) {
                    config.set(path, field.get(this));
                    continue;
                }
                if (!FileUtil.addDefault(config, path, field.get(this)))
                    field.set(this, config.get(path));
            }
            config.save(file);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean hasDebugMode() {
        return debug;
    }

    public double getRegenStrength() {
        return this.regen_strength;
    }

    public long getRegenTime() {
        return this.regen_time;
    }

    public long getRegenUpdate() {
        return this.regen_update;
    }

    public String getXWatchName() {
        return ChatColor.translateAlternateColorCodes('&', this.x$watch_name);
    }

    public String[] getXWatchLore() {
        return this.x$watch_lore.split("\\|\\|");
    }

    public long getXWatchTime() {
        return this.x$watch_time;
    }

    public long getXWatchUpdate() {
        return this.x$watch_update;
    }

    public double getXWatchRadius() {
        return this.x$watch_radius;
    }

    public String getIndicCompassName() {
        return ChatColor.translateAlternateColorCodes('&', this.indic$compass_name);
    }

    public String[] getIndicCompassLore() {
        return this.indic$compass_lore.split("\\|\\|");
    }

    public long getIndicCompassTime() {
        return this.indic$compass_time;
    }

    public long getIndicCompassUpdate() {
        return this.indic$compass_update;
    }

    public double getIndicCompassRadius() {
        return this.indic$compass_radius;
    }

    public long getOtherUpdate() {
        return this.other_update;
    }
}
