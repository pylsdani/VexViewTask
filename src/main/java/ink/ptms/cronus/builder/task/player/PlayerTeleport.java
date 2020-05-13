package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.enums.TeleportCause;
import ink.ptms.cronus.builder.task.data.location.LocationFrom;
import ink.ptms.cronus.builder.task.data.location.LocationTo;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerTeleport;
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
public class PlayerTeleport extends TaskEntry {

    public PlayerTeleport() {
        objective.add(Count.class);
        objective.add(TeleportCause.class);
        objective.add(LocationFrom.class);
        objective.add(LocationTo.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.ENDER_EYE.parseMaterial()).name("§f传送").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerTeleport.class;
    }
}
