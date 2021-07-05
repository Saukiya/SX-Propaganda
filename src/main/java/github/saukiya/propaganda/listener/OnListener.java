package github.saukiya.propaganda.listener;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import github.saukiya.propaganda.ui.sub.CreateUI;
import github.saukiya.propaganda.ui.sub.EditUI;
import github.saukiya.propaganda.util.CommandUtil;
import github.saukiya.propaganda.util.Config;
import github.saukiya.propaganda.util.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnListener implements Listener {
    @EventHandler
    void on(PlayerJoinEvent event) {
        for (Data data : Propaganda.getDataManager().getMap().values()) {
            if (data.getOwner().equals(event.getPlayer().getUniqueId())) {
                data.updateTime();
            }
        }
    }

    @EventHandler
    void on(SignChangeEvent event) {
        Block block = event.getBlock();
        Data data = Propaganda.getDataManager().getMap().get(block.getLocation());
        if (data != null) {
            if (data.getEndTime() == 0L) {
                data.updateTime();
                Propaganda.getDataManager().saveData();
                Propaganda.getDataManager().getTempMap().remove(data.getLoc());
                event.getPlayer().sendTitle("§6创建成功", "§7下蹲可编辑信息", 5, 30, 5);
                event.getPlayer().sendMessage("§8[§4宣传§8] §7有效期至: " + TimeUtil.getSdf().format(data.getEndTime()));
                block.getWorld().spawnParticle(Particle.TOTEM, event.getPlayer().getLocation(), 30);
                block.getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
            } else {
                new EditUI(event.getPlayer(), data).open();
            }
        }

    }

    @EventHandler
    void on(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UIHolder uiHolder) {
            event.setCancelled(true);
            uiHolder.process(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasBlock() && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Location loc = Config.getSign(event.getClickedBlock().getLocation(), event.getBlockFace());
            if (loc != null) {
                event.setCancelled(true);
                Data data = Propaganda.getDataManager().getMap().get(loc);
                if (data != null) {
                    if (player.isSneaking() && (data.getOwner().equals(player.getUniqueId()) || player.isOp())) {
                        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                        new EditUI(player, data).open();
                    } else if (data.getCommand() != null) {
                        Bukkit.getScheduler().runTaskLater(Propaganda.getInst(), () -> CommandUtil.onPlayerCommand(player, data.getCommand()), 5L);
                    }
                } else if (!player.isSneaking()) {
                    if (loc.getBlock().getState() instanceof Sign) {
                        event.setCancelled(false);
                    } else {
                        player.playSound(player.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 1.0F, 1.0F);
                        player.sendTitle(" ", "§e蹲下点击即可创建宣传牌哦", 5, 20, 5);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c"));
                    }
                } else if (Propaganda.getDataManager().getMap().values().stream().filter((d) -> d.getOwner().equals(player.getUniqueId())).count() < Config.getMaxSize()) {
                    Player editPlayer = Propaganda.getDataManager().getTempMap().get(loc);
                    if (editPlayer != null && editPlayer.isOnline() && editPlayer.getOpenInventory().getTopInventory().getHolder() instanceof UIHolder uiHolder) {
                        if (uiHolder.getData().getLoc().equals(loc)) {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                            player.sendTitle("§c无法创建宣传牌", "§4" + editPlayer.getName() + " 正在编辑该区域中", 5, 20, 5);
                            return;
                        }
                    }

                    if (player.getLevel() < 30) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                        player.sendTitle("§c无法创建宣传牌", "§4角色等级必须大于30", 5, 20, 5);
                        return;
                    }

                    Propaganda.getDataManager().getTempMap().put(loc, player);
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                    new CreateUI(player, new Data(player.getUniqueId(), loc, event.getBlockFace())).open();
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                    player.sendTitle("§c无法创建宣传牌", "§4已使用宣传牌: " + Config.getMaxSize(), 5, 20, 5);
                }
            }
        }

    }
}
