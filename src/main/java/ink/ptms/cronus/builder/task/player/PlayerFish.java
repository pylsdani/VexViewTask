package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.enums.FishState;
import ink.ptms.cronus.builder.task.data.item.ItemFishingRod;
import ink.ptms.cronus.builder.task.data.entity.EntityCaught;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerFish;
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
public class PlayerFish extends TaskEntry {

    public PlayerFish() {
        objective.add(Count.class);
        objective.add(FishState.class);
        objective.add(EntityCaught.class);
        objective.add(ItemFishingRod.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.FISHING_ROD.parseMaterial()).name("§f钓鱼").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerFish.class;
    }
}
