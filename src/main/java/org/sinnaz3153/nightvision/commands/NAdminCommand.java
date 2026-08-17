package org.sinnaz3153.nightvision.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.sinnaz3153.nightvision.Nightvision;
import org.sinnaz3153.nightvision.util.Formatter;

import java.util.List;

public class NAdminCommand {
    private final Nightvision plugin;private final Formatter formatter;public NAdminCommand(Nightvision plugin, Formatter formatter) {
        this.plugin = plugin;
        this.formatter = formatter;
    }

    public void register() {
        HelpHandler helpHandler = new HelpHandler(plugin, formatter);
        LiteralArgumentBuilder<CommandSourceStack> nAdminCommand = Commands.literal("nightvisionadmin")
                .executes(sender -> {
                    // permission check
                    if (!sender.getSource().getSender().hasPermission("snightvision.admin")) {
                        sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.no-permission")));
                        return 0;
                    }
                    helpHandler.sendHelp(sender.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("reload")
                        .executes(sender -> {
                            // permission check
                            if (!sender.getSource().getSender().hasPermission("snightvision.admin")) {
                                sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.no-permission")));
                                return 0;
                            }
                            plugin.reloadConfig();
                            sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.plugin-reloaded")));
                            plugin.getServer().getConsoleSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.plugin-reloaded")));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("help")
                        .executes(sender -> {
                            // permission check
                            if (!sender.getSource().getSender().hasPermission("snightvision.admin")) {
                                sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.no-permission")));
                                return 0;
                            }
                            helpHandler.sendHelp(sender.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                );
        // register the command
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(nAdminCommand.build(),
                    List.of("nvadmin", "snvadmin", "snightvisionadmin"));
        });
    }
}