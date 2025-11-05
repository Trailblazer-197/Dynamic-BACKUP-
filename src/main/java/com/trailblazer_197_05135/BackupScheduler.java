package com.trailblazer_197_05135;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class BackupScheduler {

    private final DynamicBackup plugin;
    private final BackupManager backupManager;
    private final ConfigManager config;
    private BukkitTask task;

    public BackupScheduler(DynamicBackup plugin, BackupManager backupManager, ConfigManager config) {
        this.plugin = plugin;
        this.backupManager = backupManager;
        this.config = config;
    }

    public void start() {
        String type = config.getScheduleType();
        long intervalTicks;
        if ("interval".equals(type)) {
            int hours = config.getIntervalHours();
            intervalTicks = hours * 60L * 60L * 20L;
        } else if ("daily".equals(type)) {
            intervalTicks = 24L * 60L * 60L * 20L; // 24 hours
        } else {
            intervalTicks = 12L * 60L * 60L * 20L; // default 12 hours
        }
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!backupManager.isBackingUp()) {
                    backupManager.startBackup();
                }
            }
        }.runTaskTimer(plugin, 0, intervalTicks);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }
}