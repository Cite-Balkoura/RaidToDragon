package fr.grimtown.RaidToDragon.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface GrimTownCommands {

    boolean run(final CommandSender sender, final ArrayList<String> arguments) throws Exception;

    @NotNull
    ArrayList<String> tabCompleter(final CommandSender sender, final List<String> arguments);

}
