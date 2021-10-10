package fr.grimtown.RaidToDragon.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface GrimTownCommands {

    boolean run(final CommandSender sender, final ArrayList<String> arguments) throws Exception;

    ArrayList<String> tabCompleter(final CommandSender sender, final ArrayList<String> arguments);

}
