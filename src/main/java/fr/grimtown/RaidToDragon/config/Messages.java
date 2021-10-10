package fr.grimtown.RaidToDragon.config;

import fr.grimtown.RaidToDragon.utils.FileUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Messages {

    PREFIX_INFO                         ("&3&lRaid To Dragon &8&l»"),
    PREFIX_DEBUG                        ("&7&l&oRaid To Dragon &8&l&o»"),
    PREFIX_ERROR                        ("&c&lRaid To Dragon &4&l»"),

    ERROR_UNKNOWN                       ("%prefix_error% &cUne erreur est survenue. Merci de contacter un administrateur"),
    ERROR_COMMAND_UNKNOWN               ("%prefix_error% &cLa commande &4{0} &cest introuvable."),
    ERROR_COMMAND_ARGUMENT              ("%prefix_error% &cIl manque &4{0} &cargument(s) à la commande &4{1}&c."),
    ERROR_COMMAND_PERMISSION            ("%prefix_error% &cIl vous manque la permission &4{0} &cpour exécuter la commande &4{1}&c."),
    ERROR_COMMAND_EXECUTOR_CONSOLE      ("%prefix_error% &cSeule la console peut exécuter la commande &4{0}&c."),
    ERROR_COMMAND_EXECUTOR_PLAYER       ("%prefix_error% &cSeul un joueur peut exécuter la commande &4{0}&c."),

    ;

    private String message;

    Messages(final String message) {
        this.message = message;
    }

    public static void init(final String path, final String name) {
        final File file = new File(path, name);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (final Messages messages : Messages.values()) {
                final String pathToMessage = messages.name().replace("__", "-").replace("_", ".").toLowerCase();
                if (!FileUtil.addDefault(config, pathToMessage, messages.message)) {
                    messages.message = config.getString(pathToMessage);
                }
            }
            config.save(file);
        } catch (Exception ignored) {
        }
    }

    public String getFullRaw() {
        return this.message;
    }

    public String get(final String... arguments) {
        String result = this.message;
        for (int i = 0; i < arguments.length; i++)
            result = result.replace("{" + i + "}", arguments[i]);
        return ChatColor.translateAlternateColorCodes('&', result
                .replace("%prefix_info%", PREFIX_INFO.message)
                .replace("%prefix_debug%", PREFIX_DEBUG.message)
                .replace("%prefix_error%", PREFIX_ERROR.message)
                .replace("¤", " ")
        );
    }

    public String[] getSplit(final String... arguments) {
        return this.get(arguments).split("%nl%");
    }
}
