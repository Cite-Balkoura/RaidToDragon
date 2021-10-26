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

    private String gadgets_x$watch_name = "&3Montre à rayons X";
    private String gadgets_x$watch_lore = "(>: new||line :<)";
    private String gadgets_x$watch_color = "WHITE";
    private long gadgets_x$watch_time = 3 * 1000L;
    private long gadgets_x$watch_update = 10L;
    private double gadgets_x$watch_radius = 50D;

    private String gadgets_indic$compass_name = "&3Boussole d'indication";
    private String gadgets_indic$compass_lore = "(>: new||line :<)";
    private long gadgets_indic$compass_time = 6 * 1000L;
    private long gadgets_indic$compass_update = 10L;
    private double gadgets_indic$compass_radius = -1D;

    private String gadgets_totem_name = "&3Totem de résurrection";
    private String gadgets_totem_lore = "(>: new||line :<)";
    private int gadgets_totem_length = -1;
    private int gadgets_totem_particles = 3;
    private double gadgets_totem_cycle = 10D;
    private long gadgets_totem_update = 1L;

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
        return ChatColor.translateAlternateColorCodes('&', this.gadgets_x$watch_name);
    }

    public String[] getXWatchLore() {
        return this.gadgets_x$watch_lore.split("\\|\\|");
    }

    public String getXWatchColor() {
        return this.gadgets_x$watch_color;
    }

    public long getXWatchTime() {
        return this.gadgets_x$watch_time;
    }

    public long getXWatchUpdate() {
        return this.gadgets_x$watch_update;
    }

    public double getXWatchRadius() {
        return this.gadgets_x$watch_radius;
    }

    public String getIndicCompassName() {
        return ChatColor.translateAlternateColorCodes('&', this.gadgets_indic$compass_name);
    }

    public String[] getIndicCompassLore() {
        return this.gadgets_indic$compass_lore.split("\\|\\|");
    }

    public long getIndicCompassTime() {
        return this.gadgets_indic$compass_time;
    }

    public long getIndicCompassUpdate() {
        return this.gadgets_indic$compass_update;
    }

    public double getIndicCompassRadius() {
        return this.gadgets_indic$compass_radius;
    }

    public String getGadgetsTotemName() {
        return ChatColor.translateAlternateColorCodes('&', this.gadgets_totem_name);
    }

    public String[] getGadgetsTotemLore() {
        return this.gadgets_totem_lore.split("\\|\\|");
    }

    public int getTotemLength() {
        return this.gadgets_totem_length;
    }

    public int getTotemParticles() {
        return this.gadgets_totem_particles;
    }

    public double getTotemCycle() {
        return this.gadgets_totem_cycle;
    }

    public long getTotemUpdate() {
        return this.gadgets_totem_update;
    }

    public long getOtherUpdate() {
        return this.other_update;
    }
}
