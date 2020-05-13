package ink.ptms.cronus.builder.task.data.location;

import com.google.common.collect.Lists;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.data.Location;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-30 16:24
 */
public class LocationEnchant extends Location {

    public LocationEnchant(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public ItemStack getItem() {
        if (mode == ink.ptms.cronus.internal.bukkit.Location.Mode.POINT) {
            List<String> lore = Lists.newArrayList("");
            for (int i = 0; i < location.size() && i < 8; i++) {
                lore.add("§f" + Utils.fromLocation(location.get(i)));
            }
            if (location.size() > 8) {
                lore.add("§7...");
            }
            lore.addAll(Lists.newArrayList("§8§m                  ", "§7单项: §8左键", "§7区域: §8右键", "§7范围: §8中键"));
            return new ItemBuilder(Material.MAP).name("§7目标坐标 (附魔台)").lore(lore).build();
        } else {
            return new ItemBuilder(Material.MAP)
                    .name("§7目标坐标 (附魔台)")
                    .lore(
                            "",
                            "§f" + (data == null ? "无" : data),
                            "§8§m                  ",
                            "§7单项: §8左键",
                            "§7区域: §8右键",
                            "§7范围: §8中键"
                    ).build();
        }
    }

    @Override
    public String getKey() {
        return "enchant-block";
    }
}
