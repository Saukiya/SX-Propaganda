package github.saukiya.propaganda.ui.sub;

import github.saukiya.propaganda.data.Data;
import github.saukiya.propaganda.ui.UIHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class CreateUI extends UIHolder {
    public CreateUI(Player player, Data data) {
        super(player, data, 45, "&8&l[&4&l宣传牌&8&l] &4&l创建宣传牌");
        IntStream.range(0, 9).forEach((i) -> this.map.put(i, this.randomGlass()));
        IntStream.range(36, 45).forEach((i) -> this.map.put(i, this.randomGlass()));
        this.map.put(20, new Button(Material.OAK_SIGN)
                .name("创建一个宣传牌?")
                .lore(
                        "注意事项:",
                        "· 牌子有效期30天, 超时则自动删除",
                        "· 每个人拥有的宣传牌数量有上限",
                        "· 宣传牌可以传送到地皮/领地",
                        "· 宣传牌可以随时修改内容并更新有效期",
                        " ",
                        "创建步骤:",
                        "· 传送>颜色>文本>结束")
                .process(() -> new TeleportTypeUI(player, data).open()));
        this.map.put(24, new Button(Material.BARRIER).name("&c退出创建").process(player::closeInventory));
    }
}
