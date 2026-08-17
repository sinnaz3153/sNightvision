package org.sinnaz3153.nightvision.commands;

import org.bukkit.command.CommandSender;
import org.sinnaz3153.nightvision.Nightvision;
import org.sinnaz3153.nightvision.util.Formatter;

public class HelpHandler {
    private final Nightvision plugin;private final Formatter formatter;public HelpHandler(Nightvision plugin, Formatter formatter) {
        this.plugin = plugin;
        this.formatter = formatter;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(formatter.format(plugin.getConfig().getString("messages.help.header")));
        if (sender.hasPermission("snightvision.use")) {
            sender.sendMessage(formatter.format(plugin.getConfig().getString("messages.help.nightvision")));
        }
        if (sender.hasPermission("snightvision.use.other")) {
            sender.sendMessage(formatter.format(plugin.getConfig().getString("messages.help.nightvision-player")));
        }
        if (sender.hasPermission("snightvision.admin")) {
            sender.sendMessage(formatter.format(plugin.getConfig().getString("messages.help.admin-reload")));
        }
        sender.sendMessage(formatter.format(plugin.getConfig().getString("messages.help.footer")));
    }
}
