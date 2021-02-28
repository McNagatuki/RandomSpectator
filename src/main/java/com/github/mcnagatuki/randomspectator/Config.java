package com.github.mcnagatuki.randomspectator;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private RandomSpectator plugin;
    private long time = 30;
    private double ratio = 30;

    Config() {
        plugin = RandomSpectator.plugin;
        loadConfig(false);
    }

    public boolean loadConfig() {
        return loadConfig(true);
    }

    public boolean loadConfig(boolean isReload) {
        RandomSpectator plugin = this.plugin;

        plugin.saveDefaultConfig();

        if (isReload) {
            plugin.reloadConfig();
        }

        FileConfiguration config = plugin.getConfig();

        try {
            setTime(config.getLong("time"));
            setRatio(config.getDouble("ratio"));
        } catch (Exception ignore) {
            return false;
        }

        return true;
    }

    public long getTime() {
        return time;
    }

    public boolean setTime(long time) {
        if (time <= 0) return false;

        this.time = time;
        return true;
    }

    public double getRatio() {
        return this.ratio;
    }

    public boolean setRatio(double ratio) {
        if (ratio < 0) return false;
        if (ratio > 100) return false;

        this.ratio = ratio;
        return true;
    }
}