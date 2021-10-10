package fr.grimtown.RaidToDragon.utils;

import org.bukkit.configuration.file.YamlConfiguration;

public class FileUtil {

    public static boolean addDefault(final YamlConfiguration config, final String path, Object value) {
        if (!config.contains(path)) {
            config.set(path, value);
            return true;
        }
        return false;
    }

}
