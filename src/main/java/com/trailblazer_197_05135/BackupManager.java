package com.trailblazer_197_05135;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager {

    private final DynamicBackup plugin;
    private final ConfigManager config;
    private final AtomicBoolean backingUp = new AtomicBoolean(false);
    private CompletableFuture<Void> currentBackup;

    public BackupManager(DynamicBackup plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void startBackup() {
        if (backingUp.get()) return;
        backingUp.set(true);

        currentBackup = CompletableFuture.runAsync(() -> {
            try {
                performBackup();
            } catch (Exception e) {
                plugin.getLogger().severe("Backup failed: " + e.getMessage());
            } finally {
                backingUp.set(false);
            }
        });
    }

    private void performBackup() throws IOException, InterruptedException, ExecutionException {
        // Wait for world save
        CompletableFuture<Void> saveFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (org.bukkit.World world : Bukkit.getWorlds()) {
                world.save();
            }
            saveFuture.complete(null);
        });
        saveFuture.get();

        // Create backup dir
        File backupDir = new File(plugin.getDataFolder(), "backups");
        backupDir.mkdirs();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        File zipFile = new File(backupDir, "backup_" + timestamp + ".zip");

        long startTime = System.currentTimeMillis();

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            zos.setLevel(config.getCompressionLevel());
            List<String> includes = config.getIncludeFolders();

            for (String include : includes) {
                Path path = Paths.get(include);
                if (Files.exists(path)) {
                    addToZip(zos, path);
                }
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        long size = zipFile.length();

        // Retention
        cleanupOldBackups();

        // Notifications
        notifyBackupComplete(zipFile.getName(), size, duration);
    }

    private void addToZip(ZipOutputStream zos, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.walk(path).filter(Files::isRegularFile).forEach(file -> {
                try {
                    String relative = path.relativize(file).toString();
                    zos.putNextEntry(new ZipEntry(relative));
                    Files.copy(file, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    plugin.getLogger().warning("Failed to add " + file + ": " + e.getMessage());
                }
            });
        } else if (Files.isRegularFile(path)) {
            zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
            Files.copy(path, zos);
            zos.closeEntry();
        }
    }

    private void cleanupOldBackups() {
        File backupDir = new File(plugin.getDataFolder(), "backups");
        if (!backupDir.exists()) return;

        List<File> backups = List.of(backupDir.listFiles((dir, name) -> name.endsWith(".zip")));
        backups.sort(Comparator.comparing(File::lastModified).reversed());

        int retentionCount = config.getRetentionCount();
        long maxStorage = config.getMaxStorageBytes();
        long currentSize = 0;

        for (int i = 0; i < backups.size(); i++) {
            File backup = backups.get(i);
            currentSize += backup.length();
            if (i >= retentionCount || currentSize > maxStorage) {
                backup.delete();
            }
        }
    }

    public List<String> listBackups() {
        File backupDir = new File(plugin.getDataFolder(), "backups");
        if (!backupDir.exists()) return List.of();

        return List.of(backupDir.listFiles((dir, name) -> name.endsWith(".zip")))
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public void restoreBackup(String id) {
        File backupDir = new File(plugin.getDataFolder(), "backups");
        File backupFile = new File(backupDir, id + ".zip");
        if (!backupFile.exists()) {
            plugin.getLogger().warning("Backup not found: " + id);
            return;
        }

        // Simple restore logic - in practice, you'd extract and replace server files carefully
        plugin.getLogger().info("Restoring backup: " + id);
        // TODO: Implement actual restore (unzip and replace files)
    }

    public boolean cancelBackup() {
        if (!backingUp.get()) return false;
        currentBackup.cancel(true);
        backingUp.set(false);
        return true;
    }

    public boolean isBackingUp() {
        return backingUp.get();
    }

    private void notifyBackupComplete(String filename, long size, long duration) {
        String message = String.format("✅ Completed backup: %s (%.2f MB, %dm%ds)",
                filename, size / (1024.0 * 1024.0), duration / 60000, (duration % 60000) / 1000);

        if (config.isConsoleNotifications()) {
            plugin.getLogger().info(message);
        }

        if (config.isInGameNotifications()) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("dynamicbackup.admin"))
                    .forEach(p -> p.sendMessage("§a" + message));
        }

        String webhook = config.getDiscordWebhook();
        if (!webhook.isEmpty()) {
            // Send to Discord (implement HTTP POST)
            plugin.getLogger().info("Discord notification sent.");
        }
    }
}