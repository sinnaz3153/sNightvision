package org.sinnaz3153.nightvision.util;

import net.md_5.bungee.api.ChatColor;
import org.sinnaz3153.nightvision.Nightvision;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    private final Nightvision plugin;public Formatter(Nightvision plugin) {this.plugin = plugin;}
    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public String format(String message) {
        message = plugin.getConfig().getString("prefix", "&c*MISSING-PREFIX* >>&f ") + message;

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
}
