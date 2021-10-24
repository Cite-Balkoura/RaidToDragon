package fr.grimtown.RaidToDragon.commands;

import fr.grimtown.RaidToDragon.config.Messages;
import fr.grimtown.RaidToDragon.logs.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<CommandAnnotation, GrimTownCommands> commands;

    public CommandManager() {
        this.commands = new HashMap<>();
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender commandSender, final @NotNull Command bukkitCommand, final @NotNull String label, final String[] args) {
        try {
            final String argsToString = String.join(" ", args);
            CommandAnnotation commandToExec = null;
            for (final CommandAnnotation command : this.commands.keySet()) {
                if (commandToExec == null && argsToString.startsWith(command.command())) {
                    commandToExec = command;
                    continue;
                }
                if (commandToExec == null)
                    continue;
                if (argsToString.startsWith(command.command()) && commandToExec.command().length() < command.command().length())
                    commandToExec = command;
            }
            if (commandToExec == null) {
                Logger.log(commandSender, Messages.ERROR_COMMAND_UNKNOWN.get("/" + label + " " + argsToString));
                return false;
            }
            if (commandToExec.permission().isBlank() || commandSender.hasPermission(commandToExec.permission())) {
                final ArrayList<String> arguments = Arrays.stream(argsToString.replaceFirst(commandToExec.command() + " ?", "").split(" ")).map(argument -> argument.replace("\\_", "")).collect(Collectors.toCollection(ArrayList::new));
                if (arguments.size() >= 1 && arguments.get(0).isBlank())
                    arguments.remove(0);
                if (arguments.size() < commandToExec.arguments()) {
                    Logger.log(commandSender, Messages.ERROR_COMMAND_ARGUMENT.get(String.valueOf(commandToExec.arguments() - arguments.size()), "/" + label + " " + commandToExec.command()));
                    return false;
                }
                if (commandToExec.executor() == CommandAnnotation.ExecutorType.CONSOLE && commandSender instanceof Player) {
                    Logger.log(commandSender, Messages.ERROR_COMMAND_EXECUTOR_CONSOLE.get("/" + label + " " + commandToExec.command()));
                    return false;
                }
                if (commandToExec.executor() == CommandAnnotation.ExecutorType.PLAYER && !(commandSender instanceof Player)) {
                    Logger.log(commandSender, Messages.ERROR_COMMAND_EXECUTOR_PLAYER.get("/" + label + " " + commandToExec.command()));
                    return false;
                }
                return this.commands.get(commandToExec).run(commandSender, arguments);
            } else {
                Logger.log(commandSender, Messages.ERROR_COMMAND_PERMISSION.get(commandToExec.permission(), "/" + label + " " + commandToExec.command()));
            }
        } catch (Exception e) {
            Logger.log(commandSender, Messages.ERROR_UNKNOWN.get(), e);
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        final List<String> result = new ArrayList<>();
        try {
            for (final GrimTownCommands command : this.commands.values())
                result.addAll(command.tabCompleter(commandSender, Arrays.asList(args)));
        } catch (Exception e) {
            Logger.log(commandSender, Messages.ERROR_UNKNOWN.get(), e);
        }
        return result;
    }

    public void registerCommands(final GrimTownCommands... grimtownCommands) {
        for (final GrimTownCommands grimtownCommand : grimtownCommands) {
            final CommandAnnotation annotation = grimtownCommand.getClass().getAnnotation(CommandAnnotation.class);
            if (annotation == null)
                continue;
            if (this.commands.keySet().stream().anyMatch(command -> command.command().equalsIgnoreCase(annotation.command())))
                continue;

            this.commands.put(annotation, grimtownCommand);
        }
    }

}

