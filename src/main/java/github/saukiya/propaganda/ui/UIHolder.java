package github.saukiya.propaganda.ui;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.data.Data;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftSign;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public abstract class UIHolder implements InventoryHolder {

    public static Button[] glass = Stream.of(Material.BLACK_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE).map((m) -> new Button(m).name("§f")).toArray(Button[]::new);

    protected final Player player;
    protected final Data data;
    protected final Map<Integer, Button> map = new HashMap();
    protected final int size;
    protected final String title;

    public UIHolder(Player player, Data data, int size, String title) {
        this.player = player;
        this.data = data;
        this.size = size;
        this.title = title.replace('&', '§');
    }

    public static void edit(Player player, Sign sign) {
        CraftPlayer cPlayer = (CraftPlayer) player;
        CraftSign cSign = (CraftSign) sign;
        System.out.println("cSign: " + cSign.getSnapshotNBT());
//      TileEntitySign tileSign = new TileEntitySign();
        TileEntitySign tileSign = new TileEntitySign(cSign.getPosition(), cSign.getHandle());
        tileSign.a(cSign.getSnapshotNBT());
//      tileSign.isEditable = true;
        tileSign.h = true;
//      WorldServer worldServer = (WorldServer)cPlayer.getHandle().server.worldServer.get(cPlayer.getHandle().dimension);
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
//      worldServer.setTileEntity(cSign.getPosition(), tileSign);
        worldServer.a(tileSign);
        cPlayer.getHandle().a(tileSign);
    }

    public static void save(Player player, NBTTagCompound snapshot, Sign sign) {
//        CraftPlayer cPlayer = (CraftPlayer) player;
        CraftSign cSign = (CraftSign) sign;
//      TileEntitySign tileSign = new TileEntitySign();
        TileEntitySign tileSign = new TileEntitySign(cSign.getPosition(), cSign.getHandle());
        tileSign.a(snapshot);
//      WorldServer worldServer = (WorldServer)cPlayer.getHandle().server.worldServer.get(cPlayer.getHandle().dimension);
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
//      worldServer.setTileEntity(cSign.getPosition(), tileSign);
        worldServer.a(tileSign);
    }

    public void open() {
        Inventory inv = Bukkit.createInventory(this, this.size, this.title);

        for (Entry<Integer, Button> entry : this.map.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().display);
        }

        this.player.openInventory(inv);
    }

    public void process(InventoryClickEvent event) {
        Button button = this.map.get(event.getRawSlot());
        if (button != null && button.runnable != null) {
            this.player.playSound(this.player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
            button.runnable.run();
        }

    }

    public Button randomGlass() {
        return glass[Propaganda.getRandom().nextInt(glass.length)];
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    public static class Button {
        ItemStack display;
        Runnable runnable = null;

        public Button(ItemStack display) {
            this.display = display;
        }

        public Button(Material material) {
            this.display = new ItemStack(material);
        }

        public Button name(String name) {
            ItemMeta meta = this.display.getItemMeta();
            meta.setDisplayName("§a" + name.replace('&', '§'));
            this.display.setItemMeta(meta);
            return this;
        }

        public Button lore(String... arrayLore) {
            return this.lore(Arrays.asList(arrayLore));
        }

        public Button lore(List<String> loreList) {
            ItemMeta meta = this.display.getItemMeta();
            meta.setLore(loreList.stream().map((str) -> "§7" + str.replace('&', '§')).collect(Collectors.toList()));
            this.display.setItemMeta(meta);
            return this;
        }

        public Button process(Runnable runnable) {
            this.runnable = runnable;
            return this;
        }
    }
}
