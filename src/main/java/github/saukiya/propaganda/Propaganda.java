package github.saukiya.propaganda;

import github.saukiya.propaganda.command.MainCommand;
import github.saukiya.propaganda.data.DataManager;
import github.saukiya.propaganda.listener.OnListener;
import github.saukiya.propaganda.util.Config;
import github.saukiya.propaganda.util.Message;
import github.saukiya.propaganda.util.MoneyUtil;
import github.saukiya.propaganda.util.PlaceholdersUtil;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Propaganda extends JavaPlugin {
    @Getter
    private static Propaganda inst = null;
    @Getter
    private static final Random random = new Random();
    @Getter
    private static MainCommand mainCommand;
    @Getter
    private static DataManager dataManager;

    public void onLoad() {
        inst = this;
        Config.loadConfig();
        Message.loadMessage();
        mainCommand = new MainCommand();
    }

    public void onEnable() {
        Metrics metrics = new Metrics(this, 12019);
        long oldTimes = System.currentTimeMillis();
        String version = Bukkit.getBukkitVersion().split("-")[0];
        this.getLogger().info("ServerVersion: " + version);
        dataManager = new DataManager();
        PlaceholdersUtil.setup();
        MoneyUtil.setup();
        Bukkit.getPluginManager().registerEvents(new OnListener(), this);
        mainCommand.setup("sxpropaganda");
        this.getLogger().info("Loading Time: " + (System.currentTimeMillis() - oldTimes) + " ms");
        this.getLogger().info("Author: Saukiya QQ: 1940208750");
    }

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
    }
}
