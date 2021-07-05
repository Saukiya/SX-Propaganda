package github.saukiya.propaganda.data;

import github.saukiya.propaganda.Propaganda;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class DataManager {
    private final File file = new File(Propaganda.getInst().getDataFolder(), "Data.yml");
    @Getter
    private final Map<Location, Data> map = new HashMap();
    @Getter
    private final Map<Location, Player> tempMap = new HashMap();

    public DataManager() {
        this.loadData();
        Bukkit.getScheduler().runTaskTimer(Propaganda.getInst(), this::saveData, 12000L, 12000L);
    }

    public void loadData() {
        this.map.clear();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(this.file);

        for (String key : yml.getKeys(false)) {
            Data data = new Data(key, yml.getConfigurationSection(key));
            this.map.put(data.getLoc(), data);
        }

        Propaganda.getInst().getLogger().info("Load " + this.map.size() + " Data");
    }

    public void saveData() {
        YamlConfiguration yml = new YamlConfiguration();
        List<Location> removeList = new ArrayList();

        for (Entry<Location, Data> entry : this.map.entrySet()) {
            Data data = entry.getValue();
            if (data.getEndTime() != 0L) {
                if (System.currentTimeMillis() > data.getEndTime()) {
                    data.getLoc().getBlock().setType(Material.AIR);
                    Location loc = data.getLoc().clone().add(0.5D, 0.5D, 0.5D);
                    data.getLoc().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5, 0.2D, 0.2D, 0.2D, 0.0D);
                    data.getLoc().getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
                    removeList.add(data.getLoc());
                } else {
                    data.save(yml);
                }
            }
        }

        removeList.forEach(this.map::remove);

        try {
            yml.save(this.file);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

}
