package ink.ptms.cronus.builder.task.item;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.Location;
import ink.ptms.cronus.builder.task.data.block.BlockAnvil;
import ink.ptms.cronus.builder.task.data.item.ItemResult;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.item.TaskItemRepair;
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
public class ItemRepair extends TaskEntry {

    public ItemRepair() {
        objective.add(Count.class);
        objective.add(Location.class);
        objective.add(BlockAnvil.class);
        objective.add(ItemResult.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.ANVIL.parseMaterial()).name("§f物品修复").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskItemRepair.class;
    }
}
