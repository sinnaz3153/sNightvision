package org.sinnaz3153.nightvision;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NightvisionTabCompleter implements TabCompleter {
    private final Nightvision plugin;

    public NightvisionTabCompleter() {
        this.plugin = null;
    }

    public NightvisionTabCompleter(Nightvision plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("nightvision")) {
            if (args.length == 1) {
                if (sender.hasPermission("nightvision.use.other")) {
                    completions.addAll(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .collect(Collectors.toList()));
                }
                completions.add("help");
            }
        } else if (command.getName().equalsIgnoreCase("nightvisionadmin")) {
            if (args.length == 1 && sender.hasPermission("nightvision.admin")) {
                completions.add("reload");
                completions.add("help");
            }
        }

        return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
