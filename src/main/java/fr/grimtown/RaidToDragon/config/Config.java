package fr.grimtown.RaidToDragon.config;

import fr.grimtown.RaidToDragon.utils.FileUtil;
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
}
