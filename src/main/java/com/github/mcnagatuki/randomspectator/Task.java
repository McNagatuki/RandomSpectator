package com.github.mcnagatuki.randomspectator;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Task extends BukkitRunnable {
    private Random random;
    private RandomSpectator plugin;
    private Config config;
    private long time;

    private String survivalMessage = "次は" + ChatColor.GREEN + "サバイバル" + ChatColor.RESET + "です。";
    private String spectatorMessage = "次は" + ChatColor.RED + "スペクテイター" + ChatColor.RESET + "です。";

    Task() {
        plugin = RandomSpectator.plugin;
        config = plugin.config;
        random = new Random();
        time = plugin.config.getTime();
    }

    private boolean validatePlayer(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) return false;
        return true;
    }

    void setGameMode(double ratio) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!player.isOnline()) continue;

            player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 2f, 0.5f);

            if (player.getGameMode() == GameMode.CREATIVE) continue;

            int randVal = random.nextInt(100);
            if (randVal < ratio) {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
            }

            player.sendTitle(randVal < ratio ? spectatorMessage : survivalMessage, "", 5, 20, 8);
        }
    }

    void sendActionBar(boolean flag) {
        ChatColor color = flag ? ChatColor.GREEN : ChatColor.RED;
        String message = String.join("", new String[]{"残り時間 ", color + String.valueOf(time) + ChatColor.RESET, " 秒でゲームモードをシャッフルします"});
        plugin.getServer().getOnlinePlayers().stream().forEach(v -> v.sendActionBar(message));
    }

    void countDown() {
        String message = String.join("", new String[]{"あと ", ChatColor.RED + String.valueOf(time), " 秒"});
        plugin.getServer().getOnlinePlayers().stream().forEach(v -> {
            v.playSound(v.getLocation(), Sound.BLOCK_BELL_USE, 1.0f, 1.0f);
            v.sendTitle(message, "", 5, 20, 5);
        });
    }

    @Override
    public void run() {
        sendActionBar(time > 5);

        if (0 < time && time < 6) {
            countDown();
        }

        if (time == 0) {
            double ratio = config.getRatio();
            setGameMode(ratio);
            time = config.getTime();
        } else {
            --time;
        }
    }
}
