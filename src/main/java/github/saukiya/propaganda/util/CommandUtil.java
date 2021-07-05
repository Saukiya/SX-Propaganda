package github.saukiya.propaganda.util;

import github.saukiya.propaganda.Propaganda;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class CommandUtil {
    public static void onPlayCommand(Player player, List<String> commandList) {
        int delay = 0;
        commandList = PlaceholderAPI.setPlaceholders(player, commandList);
        Iterator var3 = commandList.iterator();

        while (var3.hasNext()) {
            String cmd = (String) var3.next();
            String command = cmd.replace("%player%", player.getName());
            if (command.startsWith("[DELAY] ")) {
                delay += Integer.parseInt(command.substring(8));
            } else {
                Bukkit.getScheduler().runTaskLater(Propaganda.getInst(), () -> {
                    onPlayerCommand(player, command.replace('&', '§'));
                }, delay);
            }
        }

    }

    public static void onPlayerCommand(Player player, String command) {
        if (command.startsWith("[CONSOLE] ")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(10));
        } else if (command.startsWith("[MESSAGE] ")) {
            player.sendMessage(command.substring(10));
        } else if (command.startsWith("[CHAT] ")) {
            player.chat(command.substring(6));
        } else if (command.startsWith("[BC] ")) {
            Bukkit.broadcastMessage(command.substring(5));
        } else {
            String[] split;
            if (command.startsWith("[SOUND] ")) {
                split = command.substring(8).split(":");
                player.playSound(player.getLocation(), Sound.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
            } else if (command.startsWith("[TITLE] ")) {
                split = command.substring(8).split(":");
                player.sendTitle(split[0], split.length > 1 ? split[1] : null, 5, 30, 5);
            } else {
                Bukkit.dispatchCommand(player, command);
            }
        }

    }
}
