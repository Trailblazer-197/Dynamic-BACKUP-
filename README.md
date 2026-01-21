# Dynamic BACKUP+

A Minecraft plugin for asynchronous, lag-free backups with scheduling, retention, and restore capabilities.

## Overview

Dynamic BACKUP+ ensures that your Minecraft server’s worlds, configurations, and player data are always protected — without ever causing lag or interruptions.
Designed with performance and simplicity in mind, it performs automated or manual backups asynchronously while keeping storage usage efficient and organized.

Whether you’re running a large network or a small SMP, Dynamic BACKUP+ guarantees your data stays safe and recoverable at all times.

## Key Features
### 🧩 Asynchronous Backups

All backup operations run on separate threads, keeping your server lag-free during compression and file copying. Dynamic BACKUP+ never blocks the main server thread, ensuring smooth gameplay even during large backups.

### 📅 Automated Scheduling

Set up automatic backups at fixed intervals or specific times of day. Once configured, your server will handle its own backups — no admin input required. Missed schedules are skipped intelligently to prevent overlap.

### 🗂 Retention System

Manage storage efficiently by limiting the number of saved backups. Dynamic BACKUP+ automatically removes the oldest backups once the limit is reached, ensuring your disk never gets overloaded.

### 📁 Selective Backup Configuration

Easily choose which worlds, plugin folders, or data files to include or exclude. You have complete control over what gets saved — from essential player data to critical server configurations.

### 🪄 Manual Commands

Need to back up right now? Simple admin commands allow you to create, cancel, restore, or list backups at any time. No restarts or console-only tools required.

### 🔔 Detailed Notifications

Stay informed during every step of the backup process. Dynamic BACKUP+ provides clear console messages, in-game admin alerts, and optional Discord webhook notifications for completed or failed backups.

### 💾 Storage Management

Backups are automatically compressed and stored in a designated directory with timestamped names for easy identification. You can also set maximum total storage limits to prevent space exhaustion.

## Commands
**Command	Description**

**/backup now**	Start a full backup immediately.

**/backup list**	Show all stored backups.

**/backup restore <id>**	Restore a previous backup safely.

**/backup cancel**	Stop a running backup.
## Why Choose Dynamic BACKUP+

- Fully asynchronous and performance-friendly

- Automatic cleanup and retention control

- Configurable and transparent backup process

- Built for simplicity, reliability, and safety

- Perfect for any type of Minecraft server
## For BEST PERFORMANCE
Use with [Dynamic PERFORMANCE+](https://modrinth.com/project/dynamic-performance+).

