package ink.ptms.cronus.builder.task.item;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.item.ItemResult;
import ink.ptms.cronus.builder.task.data.item.ItemSource;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.item.TaskItemFurnace;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class ItemFurnace extends TaskEntry {

    public ItemFurnace() {
        objective.add(Count.class);
        objective.add(ItemSource.class);
        objective.add(ItemResult.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FURNACE).name("§f物品熔炼").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskItemFurnace.class;
    }
}
