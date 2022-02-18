package eu.bidulaxstudio.commandcreator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandCreator extends JavaPlugin {

    public List<Map<String, String>> commands = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        createBaseCommand();
        loadEvents();
        loadCommands();
    }

    @Override
    public void onDisable() {
        saveCommands();
    }

    public void createBaseCommand() {
        PluginCommand command = getCommand("commandcreator");
        List<String> aliases = new ArrayList<>();
        aliases.add("cc");
        command.setAliases(aliases);
        command.setExecutor(new BaseCommand(this));
        command.setTabCompleter(new BaseCommand(this));
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new MessageEvent(this), this);
    }

    public void loadCommands() {
        for (Map<?, ?> command : getConfig().getMapList("commands")) {
            commands.add((Map<String, String>) command);
        }
    }

    public void saveCommands() {
        getConfig().set("commands", commands);
        saveConfig();
    }

    public Map<String, String> convertCommand(String name, String permission, String value) {
        Map<String, String> command = new HashMap<>();
        command.put("name", name);
        command.put("permission", permission);
        command.put("value", value);
        return command;
    }

    public boolean addCommand(String name, String permission, String value) {
        for (Map<String, String> command : commands) {
            if (command.get("name").equalsIgnoreCase(name)) return false;
        }

        Map<String, String> newCommand = convertCommand(name, permission, value);
        commands.add(newCommand);

        return true;
    }

    public boolean removeCommand(String name) {
        boolean found = false;
        int index = 0;
        for (int i = 0; i < commands.size(); i++) {
            Map<String, String> command = commands.get(i);
            if (command.get("name").equalsIgnoreCase(name)) {
                found = true;
                index = i;
            }
        }
        if (!found) return false;

        commands.remove(index);

        return true;
    }

    public boolean useCommand(CommandSender sender, String name) {
        Map<String, String> command = getCommandInfos(name);

        if (command == null) {
            return false;
        } else if (!(sender.hasPermission("commandcreator.use") || sender.hasPermission(command.get("permission")) || command.get("permission").equalsIgnoreCase("default"))) {
            return false;
        }

        Bukkit.dispatchCommand(sender, command.get("value"));

        return true;
    }

    public Map<String, String> getCommandInfos(String name) {
        for (Map<String, String> command : commands) {
            if (command.get("name").equalsIgnoreCase(name)) return command;
        }

        return null;
    }

    public void sendPluginMessage(CommandSender target, String message) {
        target.sendMessage(ChatColor.GOLD + "CommandCreator: " + ChatColor.WHITE + message);
    }

    public void sendConfigMessage(CommandSender target, String configPath) {
        sendPluginMessage(target, getConfig().getString(configPath));
    }

}
