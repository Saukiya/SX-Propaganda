package github.saukiya.propaganda.ui.sub;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftSign;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class EditColorUI extends UIHolder {
    static Integer[] index = new Integer[]{22, 20, 24, 18, 26};
    static Material[] materials = new Material[]{Material.ACACIA_SIGN, Material.OAK_SIGN, Material.BIRCH_SIGN, Material.SPRUCE_SIGN, Material.DARK_OAK_SIGN};

    public EditColorUI(Player player, Data data) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l修改木牌颜色");
        IntStream.range(0, 9).forEach((ix) -> this.map.put(ix, this.randomGlass()));
        IntStream.range(36, 45).forEach((ix) -> this.map.put(ix, this.randomGlass()));

        for (int i = 0; i < index.length; ++i) {
            Material material = Material.getMaterial(materials[i].name().substring(0, materials[i].name().length() - 4) + "WALL_SIGN");
            this.map.put(index[i], new Button(materials[i]).lore(" ", "设置为该颜色").process(() -> {
                Block block = data.getLoc().getBlock();
                if (data.getEndTime() != 0L) {
                    NBTTagCompound snapshot = ((CraftSign) block.getState()).getSnapshotNBT();
                    block.setType(material);
                    WallSign wallSign = (WallSign) block.getBlockData();
                    wallSign.setFacing(data.getFace());
                    block.setBlockData(wallSign);
                    save(player, snapshot, (Sign) block.getState());
                    new EditUI(player, data).open();
                } else {
                    block.setType(material);
                    WallSign wallSignx = (WallSign) block.getBlockData();
                    wallSignx.setFacing(data.getFace());
                    block.setBlockData(wallSignx);
                    Propaganda.getDataManager().getMap().put(data.getLoc(), data);
                    Bukkit.getScheduler().runTaskLater(Propaganda.getInst(), () -> UIHolder.edit(player, (Sign) block.getState()), 5L);
                }

            }));
        }

    }
}
