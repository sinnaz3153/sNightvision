package org.sinnaz3153.nightvision;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandHandler implements CommandExecutor {
    private final Nightvision plugin;

    public CommandHandler(Nightvision plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nightvision")) {
            return handleNightvisionCommand(sender, args);
        } else if (command.getName().equalsIgnoreCase("nightvisionadmin")) {
            return handleAdminCommand(sender, args);
        }
        return false;
    }

    private boolean handleNightvisionCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(plugin.format("&cThis command can only be used by players!"));
            return true;
        }

        if (args.length == 0) {
            Player player = (Player) sender;
            if (!player.hasPermission("nightvision.use")) {
                player.sendMessage(plugin.format(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }
            toggleNightVision(player, player);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        if (!sender.hasPermission("nightvision.use.other")) {
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        toggleNightVision(sender, target);
        return true;
    }

    private boolean handleAdminCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nightvision.admin")) {
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.plugin-reloaded")));
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void toggleNightVision(CommandSender sender, Player target) {
        boolean hasNightVision = target.hasPotionEffect(PotionEffectType.NIGHT_VISION);

        if (hasNightVision) {
            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
            if (sender == target) {
                target.sendMessage(plugin.format(plugin.getConfig().getString("messages.nightvision-disabled")));
            } else {
                String message = plugin.getConfig().getString("messages.nightvision-disabled-other")
                        .replace("%player%", target.getName());
                sender.sendMessage(plugin.format(message));
            }
        } else {
            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
                    plugin.getConfig().getInt("effect.duration", -1), 0, false, false));
            if (sender == target) {
                target.sendMessage(plugin.format(plugin.getConfig().getString("messages.nightvision-enabled")));
            } else {
                String message = plugin.getConfig().getString("messages.nightvision-enabled-other")
                        .replace("%player%", target.getName());
                sender.sendMessage(plugin.format(message));
            }
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.help.header")));
        if (sender.hasPermission("nightvision.use")) {
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.help.nightvision")));
        }
        if (sender.hasPermission("nightvision.use.other")) {
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.help.nightvision-player")));
        }
        if (sender.hasPermission("nightvision.admin")) {
            sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.help.admin-reload")));
        }
        sender.sendMessage(plugin.format(plugin.getConfig().getString("messages.help.footer")));
    }
}