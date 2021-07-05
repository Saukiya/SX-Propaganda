package github.saukiya.propaganda.ui.sub;

import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.IntStream;

public class TeleportPlotUI extends UIHolder {
    static Integer[] index = new Integer[]{22, 20, 24, 18, 26};
    static Material[] materials = new Material[]{Material.ACACIA_FENCE, Material.OAK_FENCE, Material.BIRCH_FENCE, Material.SPRUCE_FENCE, Material.DARK_OAK_FENCE};

    public TeleportPlotUI(Player player, Data data) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l选择传送的地皮");
        IntStream.range(0, 9).forEach((ix) -> map.put(ix, this.randomGlass()));
        IntStream.range(36, 45).forEach((ix) -> map.put(ix, this.randomGlass()));
        List<Plot> list = PlotPlayer.from(player).getPlots().stream().toList();

        for (int i = 0; i < list.size(); ++i) {
            Plot plot = list.get(i);
            String alias = plot.getAlias();
            int z = i + 1;
            this.map.put(index[i], new Button(materials[i])
                            .name("编号: " + plot.getId())
                            .lore(alias.length() > 0 ? "&6别名: " + alias : " ", "§c点击设置为传送点")
                            .process(() -> {
                                data.setCommand("p tp " + player.getName() + " " + z);
                                if (data.getEndTime() != 0L) {
                                    new EditUI(player, data).open();
                                } else {
                                    new EditColorUI(player, data).open();
                                }
                            })
            );
        }

    }
}
