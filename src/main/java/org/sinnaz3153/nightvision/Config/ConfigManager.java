package org.sinnaz3153.nightvision.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.sinnaz3153.nightvision.Nightvision;
import org.sinnaz3153.nightvision.commands.NightvisionCommand;

import java.io.File;
import java.util.Random;

public class ConfigManager {
    private final Nightvision plugin;
    private final String currentVersion = "2"; // Update this as needed
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(Nightvision plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void initializeConfig() {
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        } else {
            // Load existing config to check version
            config = YamlConfiguration.loadConfiguration(configFile);
            String version = config.getString("config-version", "1");

            if (!version.equals(currentVersion)) {
                backupAndUpdateConfig();
            }
        }

        // Reload config
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    private void backupAndUpdateConfig() {
        try {
            // Create backup of old config
            // should technically never have a "file exists" Exception, in theory.
            // You never know what time of maschine people are running
            File backupFile = new File(plugin.getDataFolder(), "config.yml.old" + "." + (System.currentTimeMillis() / 100)+ "." + Random.from(new Random()).nextInt());
            configFile.renameTo(backupFile);

            // Save new default config
            configFile.delete();
            plugin.saveDefaultConfig();

            plugin.getLogger().info("Created backup of old config as " + backupFile.getName());
            plugin.getLogger().info("Generated new config.yml with version " + currentVersion);
            plugin.reloadConfig();
            plugin.saveConfig();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update config: " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() { return config; }
}