package github.saukiya.propaganda.data;

import github.saukiya.propaganda.Propaganda;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

@Getter
@Setter
public class Data {
    private UUID owner;
    private Location loc;
    private BlockFace face;
    private Long endTime = 0L;
    private String command = null;

    public Data(UUID owner, Location loc, BlockFace face) {
        this.owner = owner;
        this.loc = loc;
        this.face = face;
    }

    public Data(String key, ConfigurationSection config) {
        this.owner = UUID.fromString(config.getString("owner"));
        this.loc = config.getLocation("loc");
        this.endTime = Long.parseLong(key);
        this.command = config.getString("command");
        this.face = BlockFace.valueOf(config.getString("face"));
    }

    public void updateTime() {
        int temp = 0;

        while (true) {
            long time = System.currentTimeMillis() + 2592000000L + (long) temp;
            if (Propaganda.getDataManager().getMap().values().stream().noneMatch((data) -> data.getEndTime() == time)) {
                this.endTime = time;
                return;
            }

            ++temp;
        }
    }

    public void save(ConfigurationSection config) {
        config.set(this.endTime + ".owner", this.owner.toString());
        config.set(this.endTime + ".loc", this.loc);
        config.set(this.endTime + ".command", this.command);
        config.set(this.endTime + ".face", this.face.name());
    }
}
