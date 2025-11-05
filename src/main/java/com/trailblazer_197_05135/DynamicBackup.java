package com.trailblazer_197_05135;

import org.bukkit.plugin.java.JavaPlugin;

public class DynamicBackup extends JavaPlugin {

    private BackupManager backupManager;
    private BackupScheduler scheduler;
    private BackupCommands commands;
    private ConfigManager config;

    @Override
    public void onEnable() {
        config = new ConfigManager(this);
        backupManager = new BackupManager(this, config);
        scheduler = new BackupScheduler(this, backupManager, config);
        commands = new BackupCommands(this, backupManager, config);

        getCommand("backup").setExecutor(commands);
        scheduler.start();
        getLogger().info("Dynamic BACKUP+ enabled!");
    }

    @Override
    public void onDisable() {
        scheduler.stop();
        if (backupManager.isBackingUp()) {
            backupManager.cancelBackup();
        }
        getLogger().info("Dynamic BACKUP+ disabled!");
    }
}