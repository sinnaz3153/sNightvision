package org.sinnaz3153.nightvision;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// src/main/java/org/sinnaz3153/nightvision/Nightvision.java
public final class Nightvision extends JavaPlugin {
    private String prefix;
    private int duration;
    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private ConfigManager configManager;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        configManager = new ConfigManager(this);
        configManager.initializeConfig();
        loadConfig();

        // Register commands
        CommandHandler commandHandler = new CommandHandler(this);
        getCommand("nightvision").setExecutor(commandHandler);
        getCommand("nightvisionadmin").setExecutor(commandHandler);

        // Set tab completers
        NightvisionTabCompleter tabCompleter = new NightvisionTabCompleter();
        getCommand("nightvision").setTabCompleter(tabCompleter);
        getCommand("nightvisionadmin").setTabCompleter(tabCompleter);

        getServer().getConsoleSender().sendMessage(format(getConfig().getString("messages.plugin-enabled")));
    }

    private void updateAliases(PluginCommand command, String configPath) {
        List<String> aliases = getConfig().getStringList("commands." + configPath + ".aliases");
        if (!aliases.isEmpty()) {
            command.setAliases(aliases);
        }
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        loadConfig();

        // Update aliases on reload
        PluginCommand nvCommand = getCommand("nightvision");
        PluginCommand nvAdminCommand = getCommand("nightvisionadmin");

        if (nvCommand != null) {
            updateAliases(nvCommand, "nightvision");
        }
        if (nvAdminCommand != null) {
            updateAliases(nvAdminCommand, "nightvisionadmin");
        }
    }
    private void updateCommandAliases() {
        // Update nightvision command aliases
        PluginCommand nvCommand = getCommand("nightvision");
        if (nvCommand != null && getConfig().contains("commands.nightvision.aliases")) {
            List<String> aliases = getConfig().getStringList("commands.nightvision.aliases");
            nvCommand.setAliases(aliases);
        }

        // Update nightvisionadmin command aliases
        PluginCommand nvAdminCommand = getCommand("nightvisionadmin");
        if (nvAdminCommand != null && getConfig().contains("commands.nightvisionadmin.aliases")) {
            List<String> aliases = getConfig().getStringList("commands.nightvisionadmin.aliases");
            nvAdminCommand.setAliases(aliases);
        }
    }

    private void loadConfig() {
        prefix = getConfig().getString("prefix", "&#85f8f2NV&7 >>&f ");
        duration = getConfig().getInt("effect.duration", -1);
        if (duration > 0) {
            duration = duration * 20;
        } else {
            duration = Integer.MAX_VALUE;
        }
    }



    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(format(getConfig().getString("messages.plugin-disabled")));
    }

    public String getPrefix() {
        return prefix;
    }

    String format(String message) {
        message = prefix + message;

        // Convert HEX colors
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String color = matcher.group();
            String hex = color.substring(2); // Remove '&#'
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of("#" + hex).toString());
        }

        // Convert standard color codes
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void sendHelpMessage(CommandSender sender, boolean isAdmin) {
        sender.sendMessage(format(getConfig().getString("messages.help.header")));
        sender.sendMessage(format(getConfig().getString("messages.help.nightvision")));

        if (sender.hasPermission("nightvision.use.other")) {
            sender.sendMessage(format(getConfig().getString("messages.help.nightvision-player")));
        }

        if (isAdmin && sender.hasPermission("nightvision.admin")) {
            sender.sendMessage(format(getConfig().getString("messages.help.admin-reload")));
        }

        sender.sendMessage(format(getConfig().getString("messages.help.footer")));
    }

    private void toggleNightvision(Player target, CommandSender sender, boolean isSelfUse) {
        if (target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
            String message = isSelfUse ? "messages.nightvision-disabled" : "messages.nightvision-disabled-other";
            sender.sendMessage(format(getConfig().getString(message).replace("%player%", target.getName())));
        } else {
            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 0, false, false));
            String message = isSelfUse ? "messages.nightvision-enabled" : "messages.nightvision-enabled-other";
            sender.sendMessage(format(getConfig().getString(message).replace("%player%", target.getName())));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nightvision")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sendHelpMessage(sender, false);
                    return true;
                }
                if (!sender.hasPermission("nightvision.use")) {
                    sender.sendMessage(format(getConfig().getString("messages.no-permission")));
                    return true;
                }
                toggleNightvision((Player) sender, sender, true);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender, false);
                    return true;
                }
                if (!sender.hasPermission("nightvision.use.other")) {
                    sender.sendMessage(format(getConfig().getString("messages.no-permission")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(format(getConfig().getString("messages.player-not-found")));
                    return true;
                }
                toggleNightvision(target, sender, false);
                return true;
            }
            sendHelpMessage(sender, false);
            return true;
        } else if (command.getName().equalsIgnoreCase("nightvisionadmin")) {
            if (!sender.hasPermission("nightvision.admin")) {
                sender.sendMessage(format(getConfig().getString("messages.no-permission")));
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    loadConfig();
                    sender.sendMessage(format(getConfig().getString("messages.plugin-reloaded")));
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender, true);
                    return true;
                }
            }
            sendHelpMessage(sender, true);
            return true;
        }
        return false;
    }
    public int getEffectDuration() {
        return this.duration;
    }
}
