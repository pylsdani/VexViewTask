package ink.ptms.cronus.builder.task.block;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Block;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.Location;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.block.TaskBlockBreak;
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
public class BlockBreak extends TaskEntry {

    public BlockBreak() {
        objective.add(Location.class);
        objective.add(Count.class);
        objective.add(Block.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.WOODEN_PICKAXE.parseMaterial()).name("§f方块破坏").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskBlockBreak.class;
    }
}
