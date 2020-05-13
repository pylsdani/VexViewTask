package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.entity.EntityBottle;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerThrowXpBottle;
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
public class PlayerThrowXpBottle extends TaskEntry {

    public PlayerThrowXpBottle() {
        objective.add(Count.class);
        objective.add(EntityBottle.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.EXPERIENCE_BOTTLE.parseMaterial()).name("§f经验瓶抛掷").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerThrowXpBottle.class;
    }
}
