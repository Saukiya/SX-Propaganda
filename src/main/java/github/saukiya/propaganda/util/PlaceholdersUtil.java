package github.saukiya.propaganda.util;

import github.saukiya.propaganda.Propaganda;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholdersUtil extends PlaceholderExpansion {
    protected PlaceholdersUtil() {
    }

    public static void setup() {
        new PlaceholdersUtil().register();
    }

    public String getIdentifier() {
        return Propaganda.class.getSimpleName().toLowerCase();
    }

    public String getAuthor() {
        return Propaganda.getInst().getDescription().getAuthors().toString();
    }

    public String getVersion() {
        return Propaganda.getInst().getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String str) {
        return null;
    }
}
