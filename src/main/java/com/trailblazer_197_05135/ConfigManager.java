package com.trailblazer_197_05135;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {

    private final DynamicBackup plugin;
    private FileConfiguration config;

    public ConfigManager(DynamicBackup plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public List<String> getIncludeFolders() {
        return config.getStringList("backup_folders.include");
    }

    public int getRetentionCount() {
        return config.getInt("retention.max_backups", 5);
    }

    public long getMaxStorageBytes() {
        return (long) (config.getDouble("retention.max_size_gb", 5.0) * 1024 * 1024 * 1024);
    }

    public String getScheduleType() {
        return config.getString("schedule.type", "interval");
    }

    public int getIntervalHours() {
        return config.getInt("schedule.interval_hours", 12);
    }

    public String getDailyTime() {
        return config.getString("schedule.daily", "04:00");
    }

    public String getDiscordWebhook() {
        return config.getString("notifications.discord-webhook", "");
    }

    public boolean isConsoleNotifications() {
        return config.getBoolean("notifications.console", true);
    }

    public boolean isInGameNotifications() {
        return config.getBoolean("notifications.in-game", true);
    }

    public int getCompressionLevel() {
        return config.getInt("compression.level", 1);
    }
}