package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.location.LocationFrom;
import ink.ptms.cronus.builder.task.data.location.LocationTo;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerPortalExit;
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
public class PlayerPortalExit extends TaskEntry {

    public PlayerPortalExit() {
        objective.add(Count.class);
        objective.add(LocationFrom.class);
        objective.add(LocationTo.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.ENDER_PEARL.parseMaterial()).name("§f离开地狱门").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerPortalExit.class;
    }
}
