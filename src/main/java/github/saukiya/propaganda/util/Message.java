package github.saukiya.propaganda.util;

import github.saukiya.propaganda.Propaganda;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public enum Message {
    ADMIN__NO_PERMISSION_CMD,
    ADMIN__NO_CMD,
    ADMIN__NO_FORMAT,
    ADMIN__NO_ONLINE,
    ADMIN__NO_CONSOLE,
    ADMIN__PLUGIN_RELOAD,
    COMMAND__RELOAD;

    private static YamlConfiguration messages;

    public static void loadMessage() {
        File file = new File(Propaganda.getInst().getDataFolder(), "Message.yml");
        if (!file.exists()) {
            Propaganda.getInst().getLogger().info("Create Message.yml");
            Propaganda.getInst().saveResource("Message.yml", true);
        }

        messages = YamlConfiguration.loadConfiguration(file);
    }

    public static String getMsg(Message loc, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', MessageFormat.format(messages.getString(loc.toString(), "Null Message: " + loc), args));
    }

    public static List<String> getStringList(Message loc, Object... args) {
        List<String> list = messages.getStringList(loc.toString());
        if (list.size() == 0) {
            return Collections.singletonList("Null Message: " + loc);
        } else {
            IntStream.range(0, list.size()).forEach((i) -> {
                String var10000 = list.set(i, MessageFormat.format(list.get(i), args).replace("&", "§"));
            });
            return list;
        }
    }

    public static void send(LivingEntity entity, Message loc, Object... args) {
        send(entity, getMsg(loc, args));
    }

    public static void send(LivingEntity entity, String msg) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (msg.startsWith("[ACTIONBAR]")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg.substring(11)));
            } else if (msg.startsWith("[TITLE]")) {
                String[] split = msg.substring(7).split(":");
                player.sendTitle(split[0], split.length > 1 ? split[1] : null, 5, 20, 5);
            } else {
                player.sendMessage(msg);
            }
        }

    }

    public static TextComponent getTextComponent(String msg, String command, String showText) {
        TextComponent tc = new TextComponent(msg);
        if (showText != null) {
            tc.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text("§7" + showText)));
        }

        if (command != null) {
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }

        return tc;
    }

    public static void send(CommandSender sender, TextComponent tc) {
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(tc);
        } else {
            sender.sendMessage(tc.getText());
        }

    }

    public static YamlConfiguration getMessages() {
        return messages;
    }

    public String toString() {
        return this.name().replace("__", ".");
    }
}
