package fr.grimtown.RaidToDragon.config;

import fr.grimtown.RaidToDragon.utils.FileUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Permissions {

    NONE                            (""),
    ADMIN                           ("grimtown.raidtodragon.admin"),
    DEBUG                           ("grimtown.raidtodragon.debug"),
    START                           ("grimtown.raidtodragon.start")

    ;

    private String permission;

    Permissions(final String permission) {
        this.permission = permission;
    }

    public static void init(final String path, final String name) {
        final File file = new File(path, name);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (final Permissions permissions : Permissions.values()) {
                if (permissions == Permissions.NONE)
                    continue;
                final String pathToMessage = permissions.name().replace("__", "-").replace("_", ".").toLowerCase();
                if (!FileUtil.addDefault(config, pathToMessage, permissions.permission)) {
                    permissions.permission = config.getString(pathToMessage);
                }
            }
            config.save(file);
        } catch (Exception ignored) {
        }
    }

    public String get() {
        return this.permission;
    }

    public boolean hasPermission(final CommandSender sender) {
        if (this.permission.equalsIgnoreCase(""))
            return true;
        return sender.hasPermission(Permissions.ADMIN.permission) || sender.hasPermission(this.permission);
    }

}
