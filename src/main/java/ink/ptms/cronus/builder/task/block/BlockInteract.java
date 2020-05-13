package ink.ptms.cronus.builder.task.block;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Action;
import ink.ptms.cronus.builder.task.data.Block;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.Location;
import ink.ptms.cronus.builder.task.data.enums.BlockFace;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.block.TaskBlockInteract;
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
public class BlockInteract extends TaskEntry {

    public BlockInteract() {
        objective.add(Location.class);
        objective.add(Count.class);
        objective.add(Block.class);
        objective.add(BlockFace.class);
        objective.add(Action.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.STONE.parseMaterial()).name("§f方块交互").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskBlockInteract.class;
    }
}
