package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.Entity;
import ink.ptms.cronus.builder.task.data.Item;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerShear;
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
public class PlayerShear extends TaskEntry {

    public PlayerShear() {
        objective.add(Count.class);
        objective.add(Entity.class);
        objective.add(Item.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.SHEARS.parseMaterial()).name("§f修剪羊毛").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerShear.class;
    }
}
