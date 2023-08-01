package github.saukiya.propaganda.ui.sub;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import github.saukiya.propaganda.util.SignUtil;
import github.saukiya.propaganda.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class EditUI extends UIHolder {
    public EditUI(Player player, Data data) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l编辑信息 - " + TimeUtil.getSdf().format(data.getEndTime()));
        IntStream.range(0, 9).forEach((i) -> this.map.put(i, this.randomGlass()));
        IntStream.range(36, 45).forEach((i) -> this.map.put(i, this.randomGlass()));
        String signMaterial = data.getLoc().getBlock().getType().name();
        this.map.put(19, new Button(Material.ENDER_PEARL)
                .name("修改传送地点")
                .process(() -> new TeleportTypeUI(player, data).open()));
        this.map.put(21, new Button(Material.getMaterial(signMaterial.substring(0, signMaterial.length() - 9) + "SIGN"))
                .name("修改木牌颜色")
                .process(() -> new EditColorUI(player, data).open()));
        this.map.put(23, new Button(Material.PAPER)
                .name("修改木牌信息")
                .process(() -> SignUtil.edit(player, (Sign) data.getLoc().getBlock().getState())));
        this.map.put(25, new Button(Material.BARRIER)
                .name("&c删除宣传牌")
                .process(() -> {
                    data.getLoc().getBlock().setType(Material.AIR);
                    Propaganda.getDataManager().getMap().remove(data.getLoc());
                    Propaganda.getDataManager().saveData();
                    player.closeInventory();
                    player.sendTitle("§7已删除", " ", 5, 30, 5);
                    player.spawnParticle(Particle.EXPLOSION_LARGE, data.getLoc().add(0.5D, 0.5D, 0.5D), 2);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                }));
    }
}
