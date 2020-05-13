package ink.ptms.cronus.builder.task.data;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.internal.version.MaterialControl;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:24
 */
public class Action extends TaskData {

    public Action(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    public String getName() {
        switch (String.valueOf(data)) {
            case "LEFT":
                return "左键";
            case "RIGHT":
                return "右键";
            default:
                return "全部";
        }
    }

    @Override
    public String getKey() {
        return "action";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.LEVER.parseMaterial())
                .name("§7交互方式")
                .lore(
                        "",
                        "§f" + getName(),
                        "§8§m                  ",
                        "§7左键: §8左键",
                        "§7右键: §8右键",
                        "§7全部: §8中键"
                ).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        // 左键
        if (e.isLeftClick()) {
            data = "LEFT";
        }
        // 右键
        else if (e.isRightClick()) {
            data = "RIGHT";
        }
        // 中键
        else if (e.getClick().isCreativeAction()) {
            data = null;
        }
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
