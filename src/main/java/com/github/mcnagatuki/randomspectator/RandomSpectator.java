package com.github.mcnagatuki.randomspectator;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class RandomSpectator extends JavaPlugin {
    public static RandomSpectator plugin;
    public Config config;
    private BukkitTask task;
    private boolean running = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        config = new Config();
        this.getCommand("randspec").setExecutor(new CommandManager());
    }

    public void start() {
        if (running) return;
        running = true;
        task = new Task().runTaskTimer(this, 0, 20);
    }

    public void stop() {
        running = false;
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
