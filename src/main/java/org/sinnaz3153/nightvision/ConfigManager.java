package org.sinnaz3153.nightvision;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final String currentVersion = "1.2";
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void initializeConfig() {
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        } else {
            // Load existing config to check version
            config = YamlConfiguration.loadConfiguration(configFile);
            String version = config.getString("config-version", "1.0");

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
            File backupFile = new File(plugin.getDataFolder(), "config.yml.old");
            if (backupFile.exists()) {
                backupFile.delete();
            }
            configFile.renameTo(backupFile);

            // Save new default config
            configFile.delete();
            plugin.saveDefaultConfig();

            plugin.getLogger().info("Created backup of old config as config.yml.old");
            plugin.getLogger().info("Generated new config.yml with version " + currentVersion);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update config: " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}