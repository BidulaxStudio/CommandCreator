package eu.bidulaxstudio.commandcreator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseCommand implements CommandExecutor, TabCompleter {

    private final CommandCreator main;

    public BaseCommand(CommandCreator main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            main.sendConfigMessage(sender, "messages.badUsage");
            return false;
        }

        switch (args[0]) {

            case "help": {
                String helpMessage = "Cheat sheet :\n";
                helpMessage += ChatColor.AQUA + "/cc help " + ChatColor.WHITE + "Shows this message !\n";
                helpMessage += ChatColor.AQUA + "/cc use <name> " + ChatColor.WHITE + "or " + ChatColor.AQUA + "#<name> " + ChatColor.WHITE + "Executes a personalized command.\n";
                if (sender.hasPermission("commandcreator.add")) helpMessage += ChatColor.YELLOW + "/cc add <name> <permission> <command> " + ChatColor.WHITE + "Adds a new command.\n";
                if (sender.hasPermission("commandcreator.remove")) helpMessage += ChatColor.YELLOW + "/cc remove <name> " + ChatColor.WHITE + "Removes a command.\n";
                if (sender.hasPermission("commandcreator.get")) helpMessage += ChatColor.YELLOW + "/cc list " + ChatColor.WHITE + "Lists all the personalized commands.\n";
                main.sendPluginMessage(sender, helpMessage);
                break;
            }

            case "add": {
                if (!sender.hasPermission("commandcreator.add")) {
                    main.sendConfigMessage(sender, "messages.noPermission");
                    return true;
                }
                if (args.length < 4) {
                    main.sendConfigMessage(sender, "messages.badUsage");
                    return false;
                }

                String commandName = args[1];
                String commandPermission = args[2];
                StringBuilder builder = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    builder.append(args[i] + " ");
                }
                String commandValue = builder.toString();

                if (main.addCommand(commandName, commandPermission, commandValue)) {
                    main.sendConfigMessage(sender, "messages.addedCommand");
                } else {
                    main.sendConfigMessage(sender, "messages.alreadyCommand");
                }

                break;
            }

            case "remove": {
                if (!sender.hasPermission("commandcreator.remove")) {
                    main.sendConfigMessage(sender, "messages.noPermission");
                    return true;
                }
                if (args.length < 2) {
                    main.sendConfigMessage(sender, "messages.badUsage");
                    return false;
                }

                String commandName = args[1];

                if (main.removeCommand(commandName)) {
                    main.sendConfigMessage(sender, "messages.removedCommand");
                } else {
                    main.sendConfigMessage(sender, "messages.noCommand");
                }

                break;
            }

            case "list": {
                if (!sender.hasPermission("commandcreator.get")) {
                    main.sendConfigMessage(sender, "messages.noPermission");
                    return true;
                }

                StringBuilder builder = new StringBuilder();

                int commands = 0;
                for (Map<String, String> existingCommand : main.commands) {
                    builder.append("\n - " + existingCommand.get("name"));
                    commands++;
                }

                main.sendPluginMessage(sender, commands + " commands:" + builder);

                break;
            }

            case "use": {
                if (args.length < 2) {
                    main.sendConfigMessage(sender, "messages.badUsage");
                    return false;
                }

                String commandName = args[1];

                if (!main.useCommand(sender, commandName)) {
                    main.sendConfigMessage(sender, "messages.notWorked");
                }

                break;
            }

            default: {
                main.sendConfigMessage(sender, "messages.badUsage");
                return false;
            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        switch (args.length) {

            case 1: {
                suggestions.add("help");
                suggestions.add("use");
                if (sender.hasPermission("commandcreator.add")) suggestions.add("add");
                if (sender.hasPermission("commandcreator.remove")) suggestions.add("remove");
                if (sender.hasPermission("commandcreator.get")) suggestions.add("list");
                break;
            }

            case 2: {

                switch (args[0]) {

                    case "use": {
                        for (Map<String, String> registeredCommand : main.commands) {
                            if (sender.hasPermission("commandcreator.use") || sender.hasPermission(registeredCommand.get("permission")) || registeredCommand.get("permission").equalsIgnoreCase("default")) {
                                suggestions.add(registeredCommand.get("name"));
                            }
                        }
                        break;
                    }

                    case "remove": {
                        for (Map<String, String> registeredCommand : main.commands) {
                            if (sender.hasPermission("commandcreator.remove")) {
                                suggestions.add(registeredCommand.get("name"));
                            }
                        }
                        break;
                    }

                }

                break;
            }

            case 3: {
                if (args[0].equals("add")) {
                    if (sender.hasPermission("commandcreator.add")) {
                        suggestions.add("op");
                        suggestions.add("default");
                    }
                }
                break;
            }

        }

        return suggestions;
    }

}
