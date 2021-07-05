package github.saukiya.propaganda.ui.sub;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class TeleportResUI extends UIHolder {
    static Integer[] index = new Integer[]{22, 20, 24, 18, 26};
    static Material[] materials = new Material[]{Material.YELLOW_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.BLUE_TERRACOTTA};

    public TeleportResUI(Player player, Data data) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l选择传送的领地");
        IntStream.range(0, 9).forEach((ix) -> map.put(ix, this.randomGlass()));
        IntStream.range(36, 45).forEach((ix) -> map.put(ix, this.randomGlass()));
        List<ClaimedResidence> list = ResidenceApi.getPlayerManager().getResidencePlayer(player.getName()).getResList();

        for (int i = 0; i < list.size(); ++i) {
            ClaimedResidence residence = list.get(i);
            int subSize = residence.getSubzones().size();
            List<String> lore = subSize > 0 ? Arrays.asList("§6子领地数量: " + subSize, "§c点击选择子领地") : Arrays.asList(" ", "§c点击设置为传送点");
            this.map.put(index[i], new Button(materials[i])
                    .name("&a" + residence.getName())
                    .lore(lore)
                    .process(() -> {
                        if (subSize == 0) {
                            data.setCommand("res tp " + residence.getName());
                            if (data.getEndTime() != 0L) {
                                new EditUI(player, data).open();
                            } else {
                                new EditColorUI(player, data).open();
                            }
                        } else {
                            new TeleportResZoneUI(player, data, residence).open();
                        }

                    })
            );
        }

    }
}
