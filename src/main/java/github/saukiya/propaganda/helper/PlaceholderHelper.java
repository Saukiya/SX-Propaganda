package github.saukiya.propaganda.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceholderHelper {

    static JavaPlugin plugin;

    static boolean enabled;

    public static void setup(JavaPlugin plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderHelper.plugin = plugin;
            try {
                new Placeholder().register();
                enabled = true;
                plugin.getLogger().info("PlaceholderHelper Enabled");
            } catch (Exception e) {
                plugin.getLogger().warning("Placeholder error");
                enabled = false;
            }
        } else {
            plugin.getLogger().info("PlaceholderHelper Disable");
        }
    }

    public static List<String> setPlaceholders(Player player, List<String> list) {
        if (list == null) return null;
        if (!enabled || player == null) return new ArrayList<>(list);

        return PlaceholderAPI.setPlaceholders(player, list);
    }

    public static String setPlaceholders(Player player, String text) {
        if (text == null) return null;
        if (!enabled || player == null) return text;

        return PlaceholderAPI.setPlaceholders(player, text);
    }

    static class Placeholder extends PlaceholderExpansion {

        @Override
        public String getIdentifier() {
            return plugin.getDescription().getName().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getAuthor() {
            return plugin.getDescription().getAuthors().toString();
        }

        @Override
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public String onPlaceholderRequest(Player player, String params) {
            return "N/A";
        }
    }
}
