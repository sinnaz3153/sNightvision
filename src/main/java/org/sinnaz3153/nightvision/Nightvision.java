package org.sinnaz3153.nightvision;

import org.bstats.bukkit.Metrics;
//import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import org.sinnaz3153.nightvision.Config.ConfigManager;
import org.sinnaz3153.nightvision.commands.NightvisionCommand;
import org.sinnaz3153.nightvision.commands.NAdminCommand;
import org.sinnaz3153.nightvision.util.Formatter;

public final class Nightvision extends JavaPlugin {
    private ConfigManager configManager;
    private Formatter formatter;

    @Override
    public void onEnable() {
        this.formatter = new Formatter(this);

        // bStats thing
        int pluginId = 31782;
        Metrics metrics = new Metrics(this, pluginId);

//        // Optional: Add custom charts
//        metrics.addCustomChart(
//                new SimplePie("chart_id", () -> "My value")
//        );
        // Actually stuff
        saveDefaultConfig();
        reloadConfig();

        configManager = new ConfigManager(this);
        configManager.initializeConfig();

        // Register commands
        new NightvisionCommand(this, formatter).register();
        new NAdminCommand(this, formatter).register();

        getServer().getConsoleSender().sendMessage(formatter.format(getConfig().getString("messages.plugin-enabled")));
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(formatter.format(getConfig().getString("messages.plugin-disabled")));
        saveConfig();
    }
}
