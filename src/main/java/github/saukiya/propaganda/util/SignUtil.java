package github.saukiya.propaganda.util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SignUtil {

    public static void edit(Player player, Sign sign) {
        CraftPlayer cPlayer = (CraftPlayer) player;
        sign.setWaxed(false);
        CraftSign cSign = (CraftSign) sign;
        System.out.println("cSign: " + cSign.getSnapshotNBT());
//      TileEntitySign tileSign = new TileEntitySign();
        TileEntitySign tileSign = new TileEntitySign(cSign.getPosition(), cSign.getHandle());
        tileSign.a(cSign.getSnapshotNBT());
//      tileSign.isEditable = true;
//        tileSign.h = true;
//      WorldServer worldServer = (WorldServer)cPlayer.getHandle().server.worldServer.get(cPlayer.getHandle().dimension);
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
        worldServer.a(tileSign);
        cPlayer.getHandle().a(tileSign, true);
    }

    public static void save(Player player, NBTTagCompound snapshot, Sign sign) {
        CraftSign cSign = (CraftSign) sign;
        TileEntitySign tileSign = new TileEntitySign(cSign.getPosition(), cSign.getHandle());
        tileSign.a(snapshot);
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
        worldServer.a(tileSign);
    }
}
