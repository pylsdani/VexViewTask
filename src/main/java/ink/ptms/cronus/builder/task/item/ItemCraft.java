package ink.ptms.cronus.builder.task.item;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.item.ItemMatrix;
import ink.ptms.cronus.builder.task.data.item.ItemResult;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.item.TaskItemCraft;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class ItemCraft extends TaskEntry {

    public ItemCraft() {
        objective.add(Count.class);
        objective.add(ItemMatrix.class);
        objective.add(ItemResult.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.CRAFTING_TABLE.parseMaterial()).name("§f物品合成").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskItemCraft.class;
    }
}
