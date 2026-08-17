package org.sinnaz3153.nightvision.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.sinnaz3153.nightvision.Nightvision;
import org.sinnaz3153.nightvision.util.Formatter;

import java.util.List;

public class NightvisionCommand {
    private final Nightvision plugin;private final Formatter formatter;public NightvisionCommand(Nightvision plugin, Formatter formatter) {
        this.plugin = plugin;
        this.formatter = formatter;
    }

    public void register() {
        // commmand
        LiteralArgumentBuilder<CommandSourceStack> nightvisionCommand = Commands.literal("nightvision")
                .executes(sender -> {
                    if (!(sender.getSource().getSender() instanceof Player)) {
                        sender.getSource().getSender().sendMessage(formatter.format("&cThis command can only be used by players!"));
                        // This return is 1/ture because it is intended that console gets this when it runs the command.
                        // Permission failure is something unintended when running a command.
                        // Console not being able to run it is a part of the feature not a permissions matter
                        return 1;
                    }
                    // permission check
                    if (!sender.getSource().getSender().hasPermission("snightvision.use")) {
                        sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.no-permission")));
                        return 0;
                    }
                    toggleNightVision(sender.getSource().getSender(), (Player) sender.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("help")
                        .executes(sender -> {
                            HelpHandler helpHandler = new HelpHandler(plugin, formatter);
                            helpHandler.sendHelp(sender.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                // selector thingy
                .then(Commands.argument("players", ArgumentTypes.players())
                        .executes(sender -> {
                            final PlayerSelectorArgumentResolver targetResolver = sender.getArgument("players", PlayerSelectorArgumentResolver.class);
                            final List<Player> targets = targetResolver.resolve(sender.getSource());
                            // permission check
                            if (!sender.getSource().getSender().hasPermission("snightvision.use.other")) {
                                sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.no-permission")));
                                return 0;
                            }
                            if (targets.isEmpty()) {
                                // if argument didn't find player
                                sender.getSource().getSender().sendMessage(formatter.format(plugin.getConfig().getString("messages.player-not-found")));
                                return 0;
                            }
                            final CommandSender executer = sender.getSource().getSender();
                            for (final Player target : targets) {
                                toggleNightVision(executer, target);
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
        // register
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(nightvisionCommand.build(),
                    List.of("nv", "snv", "snightvision"));
        });
    }

    private void toggleNightVision(CommandSender sender, Player target) {
        boolean hasNightVision = target.hasPotionEffect(PotionEffectType.NIGHT_VISION);

        // remove NV if there
        if (hasNightVision) {
            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
            if (sender == target) {
                target.sendMessage(formatter.format(plugin.getConfig().getString("messages.nightvision-disabled")));
            } else {
                String message = plugin.getConfig().getString("messages.nightvision-disabled-other")
                        .replace("%player%", target.getName());
                sender.sendMessage(formatter.format(message));
                target.sendMessage(formatter.format(plugin.getConfig().getString("messages.nightvision-disabled") + " &7(by " + sender.getName() + ")")); // <- hard coded btw. You can't remove the "by" tag.
            }
        // add nv
        } else {
            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
                    plugin.getConfig().getInt("effect.duration", -1),
                    plugin.getConfig().getInt("effect.amplifier", 0),
                    plugin.getConfig().getBoolean("effect.ambient", false),
                    plugin.getConfig().getBoolean("effect.particles", false))
            );
            if (sender == target) {
                target.sendMessage(formatter.format(plugin.getConfig().getString("messages.nightvision-enabled")));
            } else {
                String message = plugin.getConfig().getString("messages.nightvision-enabled-other")
                        .replace("%player%", target.getName());
                sender.sendMessage(formatter.format(message));
                target.sendMessage(formatter.format(plugin.getConfig().getString("messages.nightvision-enabled") + " &7(by " + sender.getName() + ")"));
            }
        }
    }
}
