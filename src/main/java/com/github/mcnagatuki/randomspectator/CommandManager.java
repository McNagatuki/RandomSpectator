package com.github.mcnagatuki.randomspectator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CommandManager implements CommandExecutor, TabCompleter {
    private final String prefixAccept = ChatColor.GREEN + "[RandomSpectator]" + ChatColor.RESET + " ";
    private final String prefixReject = ChatColor.RED + "[RandomSpectator]" + ChatColor.RESET + " ";

    private boolean same(String a, String b) {
        return a.equals(b);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0)
            return false;

        // help
        if (same(args[0], "help")) {
            final String[] HELP_MESSAGE = {
                    "-------------- [ " + ChatColor.GREEN + "RandomSpectator Plugin" + ChatColor.RESET + " ] ---------------",
                    "/randspec help : ヘルプ表示",
                    "/randspec start : プラグインを有効化",
                    "/randspec stop : プラグインを無効化",
                    "/randspec time < long > : 切り替わる時間 (ticks)",
                    "/randspec ratio < double > : スペクテイターになる確率",
                    "/hotitem loadconfig : コンフィグの読み出し（リロード）",
                    "-----------------------------------------------------",
            };
            Stream.of(HELP_MESSAGE).forEach(sender::sendMessage);
            return true;
        }

        // start
        if (same(args[0], "start")) {
            RandomSpectator.plugin.start();
            sender.sendMessage(prefixAccept + "Plugin is started.");
            return true;
        }

        // stop
        if (same(args[0], "stop")) {
            RandomSpectator.plugin.stop();
            sender.sendMessage(prefixAccept + "Plugin is stopped.");
            return true;
        }

        // time <long>
        if (args.length == 2 && same(args[0], "time")) {
            try {
                long value = Long.parseLong(args[1]);

                if (RandomSpectator.plugin.config.setTime(value)) {
                    sender.sendMessage(String.format("%s\"time\" が %s に設定されました。", prefixAccept, args[1]));
                    return true;
                }
            } catch (Exception ignore) {
                // do nothing
            }

            sender.sendMessage(prefixReject + " 不正な引数です。");
            return false;
        }

        // ratio <double>
        if (args.length == 2 && same(args[0], "ratio")) {
            try {
                double value = Double.parseDouble(args[1]);
                if (RandomSpectator.plugin.config.setRatio(value)) {
                    sender.sendMessage(String.format("%s\"ratio\" が %s に設定されました。", prefixAccept, args[1]));
                    return true;
                }
            } catch (Exception ignore) {
                // do nothing
            }

            sender.sendMessage(prefixReject + "不正な引数です。");
            return false;
        }

        // loadconfig
        if (same(args[0], "loadconfig")) {
            if (RandomSpectator.plugin.config.loadConfig()) {
                sender.sendMessage(prefixAccept + "Config is loaded.");
                return true;
            }
            sender.sendMessage(prefixReject + "Failed to load config.");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Stream.of("help", "loadconfig", "start", "stop", "time", "ratio")
                    .filter(e -> e.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        // time
        if (args.length == 2 && same(args[0], "time")) {
            if (args[1].length() == 0) {
                String suggestion = String.valueOf(RandomSpectator.plugin.config.getTime());
                return Collections.singletonList(suggestion);
            }
        }

        // ratio
        if (args.length == 2 && same(args[0], "ratio")) {
            if (args[1].length() == 0) {
                String suggestion = String.valueOf(RandomSpectator.plugin.config.getRatio());
                return Collections.singletonList(suggestion);
            }
        }

        return null;
    }
}