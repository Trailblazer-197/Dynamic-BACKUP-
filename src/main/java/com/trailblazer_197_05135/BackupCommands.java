package com.trailblazer_197_05135;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BackupCommands implements CommandExecutor {

    private final DynamicBackup plugin;
    private final BackupManager backupManager;
    private final ConfigManager config;

    public BackupCommands(DynamicBackup plugin, BackupManager backupManager, ConfigManager config) {
        this.plugin = plugin;
        this.backupManager = backupManager;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("dynamicbackup.admin")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§eUsage: /backup <now|list|restore|cancel> [id]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "now":
                if (backupManager.isBackingUp()) {
                    sender.sendMessage("§cBackup already in progress!");
                    return true;
                }
                backupManager.startBackup();
                sender.sendMessage("§aStarting backup...");
                break;
            case "list":
                List<String> backups = backupManager.listBackups();
                if (backups.isEmpty()) {
                    sender.sendMessage("§eNo backups found.");
                } else {
                    sender.sendMessage("§eBackups:");
                    for (String backup : backups) {
                        sender.sendMessage("§7- " + backup);
                    }
                }
                break;
            case "restore":
                if (args.length < 2) {
                    sender.sendMessage("§eUsage: /backup restore <id>");
                    return true;
                }
                backupManager.restoreBackup(args[1]);
                sender.sendMessage("§aRestoring backup: " + args[1]);
                break;
            case "cancel":
                if (backupManager.cancelBackup()) {
                    sender.sendMessage("§aBackup cancelled.");
                } else {
                    sender.sendMessage("§cNo backup in progress.");
                }
                break;
            default:
                sender.sendMessage("§eUnknown subcommand.");
        }
        return true;
    }
}