package github.saukiya.propaganda.ui.sub;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.plotsquared.core.player.PlotPlayer;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class TeleportTypeUI extends UIHolder {
    public TeleportTypeUI(Player player, Data data) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l选择传送类型");
        IntStream.range(0, 9).forEach((i) -> map.put(i, this.randomGlass()));
        IntStream.range(36, 45).forEach((i) -> this.map.put(i, this.randomGlass()));
        if (Bukkit.getPluginManager().isPluginEnabled("Residence")) {
            int resSize = ResidenceApi.getPlayerManager().getResidencePlayer(player.getName()).getResList().size();
            this.map.put(Bukkit.getPluginManager().isPluginEnabled("PlotSquared") ? 20 : 22, new Button(Material.GRASS)
                    .name(resSize > 0 ? "&a领地传送" : "&c你没有领地!")
                    .lore("领地数量: " + resSize).process(() -> {
                        if (resSize > 0) {
                            new TeleportResUI(player, data).open();
                        }

                    }));
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
            int plotSize = PlotPlayer.from(player).getPlots().size();
            this.map.put(Bukkit.getPluginManager().isPluginEnabled("Residence") ? 24 : 22, new Button(Material.ACACIA_FENCE)
                    .name(plotSize > 0 ? "&6地皮传送" : "&c你没有地皮!")
                    .lore("地皮数量: " + plotSize).process(() -> {
                        if (plotSize > 0) {
                            new TeleportPlotUI(player, data).open();
                        }

                    }));
        }
        this.map.put(31, new Button(Material.BARRIER)
                .name("&c不传送坐标")
                .process(() -> {
            data.setCommand(null);
            if (data.getEndTime() != 0L) {
                new EditUI(player, data).open();
            } else {
                new EditColorUI(player, data).open();
            }
        }));
    }
}
