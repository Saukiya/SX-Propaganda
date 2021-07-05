package github.saukiya.propaganda.command.sub;

import github.saukiya.propaganda.command.SenderType;
import github.saukiya.propaganda.command.SubCommand;
import github.saukiya.propaganda.ui.UIHolder;
import github.saukiya.propaganda.util.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_17_R1.block.impl.CraftWallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DebugCommand extends SubCommand implements Listener {
    boolean debug = false;

    public DebugCommand() {
        super("debug");
        this.setType(SenderType.PLAYER);
        this.setHide();
    }

    public void onCommand(CommandSender sender, String[] args) {
        this.debug = !this.debug;
        sender.sendMessage(" §7Debug: §c" + this.debug);
    }

    @EventHandler
    void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (this.debug && event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location loc = Config.getSign(event.getClickedBlock().getLocation(), event.getBlockFace());
            if (loc != null) {
                Block block = loc.getBlock();
                player.sendMessage(" §7" + block.getType() + " - " + block.getState().getClass().getSimpleName() + " - " + block.getBlockData().getClass().getSimpleName());
                if (player.isSneaking()) {
                    if (loc.getBlock().getState() instanceof CraftSign) {
                        UIHolder.edit(player, (Sign) loc.getBlock().getState());
                    } else {
                        block.setType(Material.OAK_WALL_SIGN);
                        CraftWallSign craftWallSign = (CraftWallSign) block.getBlockData();
                        craftWallSign.setFacing(event.getBlockFace());
                        block.setBlockData(craftWallSign);
                    }
                }
            }
        }

    }
}
