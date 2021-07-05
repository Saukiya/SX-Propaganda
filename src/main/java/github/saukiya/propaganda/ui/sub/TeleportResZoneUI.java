package github.saukiya.propaganda.ui.sub;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.IntStream;

public class TeleportResZoneUI extends UIHolder {

    static Integer[] index = new Integer[]{22, 20, 24, 18, 26};

    static Material[] materials = new Material[]{Material.YELLOW_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.BLUE_TERRACOTTA};

    public TeleportResZoneUI(Player player, Data data, ClaimedResidence residence) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l选择传送到&c&l" + residence.getName() + "&4&l的子领地");
        IntStream.range(0, 9).forEach((ix) -> map.put(ix, this.randomGlass()));
        IntStream.range(36, 45).forEach((ix) -> map.put(ix, this.randomGlass()));
        List<ClaimedResidence> list = residence.getSubzones();

        for (int i = 0; i < list.size(); ++i) {
            ClaimedResidence subZone = list.get(i);
            this.map.put(index[i], new Button(materials[i]).name("&a" + subZone.getName()).lore(" ", "§c点击设置为传送点").process(() -> {
                data.setCommand("res tp " + subZone.getName());
                if (data.getEndTime() != 0L) {
                    new EditUI(player, data).open();
                } else {
                    new EditColorUI(player, data).open();
                }

            }));
        }

        this.map.put(31, new Button(Material.BLACK_TERRACOTTA).name("&4使用主领地作为传送点").process(() -> {
            data.setCommand("res tp " + residence.getName());
            if (data.getEndTime() != 0L) {
                new EditUI(player, data).open();
            } else {
                new EditColorUI(player, data).open();
            }
        }));
    }
}
