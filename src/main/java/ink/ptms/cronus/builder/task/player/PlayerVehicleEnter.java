package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.entity.EntityVehicle;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerVehicleEnter;
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
public class PlayerVehicleEnter extends TaskEntry {

    public PlayerVehicleEnter() {
        objective.add(Count.class);
        objective.add(EntityVehicle.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.MINECART.parseMaterial()).name("§f进入坐骑").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerVehicleEnter.class;
    }
}
