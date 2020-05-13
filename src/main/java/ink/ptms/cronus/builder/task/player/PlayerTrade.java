package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.Item;
import ink.ptms.cronus.builder.task.data.item.ItemResult;
import ink.ptms.cronus.builder.task.data.item.ItemTrade1;
import ink.ptms.cronus.builder.task.data.item.ItemTrade2;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerTrade;
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
public class PlayerTrade extends TaskEntry {

    public PlayerTrade() {
        objective.add(Count.class);
        objective.add(Item.class);
        objective.add(ItemTrade1.class);
        objective.add(ItemTrade2.class);
        objective.add(ItemResult.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.EMERALD.parseMaterial()).name("§f村民交易").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerTrade.class;
    }
}
