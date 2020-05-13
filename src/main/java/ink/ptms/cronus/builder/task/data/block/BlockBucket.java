package ink.ptms.cronus.builder.task.data.block;

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
public class BlockBucket extends TaskData {

    public BlockBucket(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    public String getName() {
        switch (String.valueOf(data)) {
            case "WATER":
                return "水";
            case "LAVA":
                return "岩浆";
            default:
                return "全部";
        }
    }

    @Override
    public String getKey() {
        return "block";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.LEVER.parseMaterial())
                .name("§7液体类型")
                .lore(
                        "",
                        "§f" + getName(),
                        "§8§m                  ",
                        "§7左键: §8水",
                        "§7右键: §8岩浆",
                        "§7右键: §8全部"
                ).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        // 左键
        if (e.isLeftClick()) {
            data = "WATER";
        }
        // 右键
        else if (e.isRightClick()) {
            data = "LAVA";
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
