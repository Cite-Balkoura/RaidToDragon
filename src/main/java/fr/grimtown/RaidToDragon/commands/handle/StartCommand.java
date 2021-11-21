package fr.grimtown.RaidToDragon.commands.handle;

import fr.grimtown.RaidToDragon.commands.CommandAnnotation;
import fr.grimtown.RaidToDragon.commands.GrimTownCommands;
import fr.grimtown.RaidToDragon.config.Permissions;
import fr.grimtown.RaidToDragon.entities.GameManager;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CommandAnnotation(command = "start", permission = Permissions.START)
public class StartCommand implements GrimTownCommands {

    @Override
    public boolean run(final CommandSender sender, final ArrayList<String> arguments) throws Exception {
        final GameManager manager = RaidPlugin.get().getGameManager();
        if (manager.isStarted())
            return false;
        manager.start();
        return true;
    }

    @Override
    public @NotNull ArrayList<String> tabCompleter(CommandSender sender, List<String> arguments) {
        return new ArrayList<>();
    }

}
