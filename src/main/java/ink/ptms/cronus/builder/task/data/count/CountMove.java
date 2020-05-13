package ink.ptms.cronus.builder.task.data.count;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:24
 */
public class CountMove extends TaskData {

    public CountMove(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "count";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.COMPASS)
                .name("§7移动距离")
                .lore(
                        "",
                        "§f" + data,
                        "§8§m                  ",
                        "§7+1: §8左键",
                        "§7-1: §8右键",
                        "§7+10: §8SHIFT+左键",
                        "§7-10: §8SHIFT+右键"
                ).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        // 左键
        if (e.isLeftClick()) {
            // +10
            if (e.isShiftClick() && !Utils.isActionCooldown(e.getWhoClicked())) {
                data = NumberConversions.toInt(data) + 10;
            }
            // +1
            else {
                data = NumberConversions.toInt(data) + 1;
            }
        }
        // 右键
        else if (e.isRightClick()) {
            // -10
            if (e.isShiftClick() && !Utils.isActionCooldown(e.getWhoClicked())) {
                data = Math.max(NumberConversions.toInt(data) - 10, 1);
            }
            // -1
            else {
                data = Math.max(NumberConversions.toInt(data) - 1, 1);
            }
        }
    }

    @Override
    public Object defaultValue() {
        return 1;
    }
}
