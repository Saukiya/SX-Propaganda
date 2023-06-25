package github.saukiya.propaganda.util;

import github.saukiya.propaganda.Propaganda;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {
    @Getter
    private static YamlConfiguration config;
    private static final List<Box> boxList = new ArrayList();
    private static final int maxSize = 2;
    private static final int money = 0;

    public static void loadConfig() {
        File file = new File(Propaganda.getInst().getDataFolder(), "Config.yml");
        if (!file.exists()) {
            Propaganda.getInst().getLogger().info("Create Config.yml");
            Propaganda.getInst().saveResource("Config.yml", true);
        }

        config = YamlConfiguration.loadConfiguration(file);
        boxList.clear();

        for (String key : config.getConfigurationSection("list").getKeys(false)) {
            boxList.add(new Box(config.getConfigurationSection("list." + key)));
        }

    }

    public static boolean isSignBlock(Location loc) {
        for (Box box : boxList) {
            if (box.world.equals(loc.getWorld().getName())) {
                if (box.range(loc) || box.range(loc.clone().add(box.face.getDirection().multiply(-1)))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Location getSign(Location loc, BlockFace face) {
        for (Box box : boxList) {
            if (box.face == face && box.world.equals(loc.getWorld().getName())) {
                if (box.range(loc)) {
                    return loc.clone().add(face.getDirection());
                }

                if (box.range(loc.clone().add(face.getDirection().multiply(-1)))) {
                    return loc;
                }
            }
        }
        return null;
    }

    public static int getMaxSize() {
        return maxSize;
    }

    public static int getMoney() {
        return money;
    }

    static class Box {
        String world;
        int[] locX;
        int[] locY;
        int[] locZ;
        BlockFace face;

        Box(ConfigurationSection config) {
            this.world = config.getString("world");
            this.locX = config.getIntegerList("x").stream().sorted().mapToInt(Integer::valueOf).toArray();
            this.locY = config.getIntegerList("y").stream().sorted().mapToInt(Integer::valueOf).toArray();
            this.locZ = config.getIntegerList("z").stream().sorted().mapToInt(Integer::valueOf).toArray();
            this.face = BlockFace.valueOf(config.getString("face"));
        }

        boolean range(Location loc) {
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            return x >= this.locX[0] && x <= this.locX[1] && z >= this.locZ[0] && z <= this.locZ[1] && y >= this.locY[0] && y <= this.locY[1];
        }
    }
}
