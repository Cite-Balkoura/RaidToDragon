package fr.grimtown.RaidToDragon.plugin;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.config.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Logger {

    public static void log(final CommandSender target, final String message) {
        log(target, Level.INFO, message);
    }

    public static void log(final CommandSender target, final String message, final Exception error) {
        log(target, Level.SEVERE, message, error);
    }

    public static void log(final CommandSender target, final Level logLevel, final String message) {
        if (target instanceof Player) {
            target.sendMessage(message);
            if (Config.get().hasDebugMode())
                RaidPlugin.get().getLogger().log(logLevel, "Log from " + target.getName() + " >> " + message);
        } else
            RaidPlugin.get().getLogger().log(logLevel, message);
    }

    public static void log(final CommandSender target, final Level logLevel, final String message, final Exception error) {
        if (target instanceof Player) {
            target.sendMessage(message);
            if (Config.get().hasDebugMode())
                RaidPlugin.get().getLogger().log(logLevel, "Log from " + target.getName() + " >> " + message, error);
            else
                error.printStackTrace();
        } else
            RaidPlugin.get().getLogger().log(logLevel, message, error);
    }

    public static void debug(final String message) {
        RaidPlugin.get().getServer().getOnlinePlayers()
                .stream().filter(Permissions.DEBUG::hasPermission)
                .forEach(player -> player.sendMessage(message));
        debugConsole(message);
    }

    public static void debugConsole(final String message) {
        if (Config.get().hasDebugMode())
            RaidPlugin.get().getLogger().log(Level.INFO, message);
    }

}
