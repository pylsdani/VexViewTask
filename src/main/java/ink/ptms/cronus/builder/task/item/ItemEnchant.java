package ink.ptms.cronus.builder.task.item;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.Enchant;
import ink.ptms.cronus.builder.task.data.Item;
import ink.ptms.cronus.builder.task.data.location.LocationEnchant;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.item.TaskItemEnchant;
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
public class ItemEnchant extends TaskEntry {

    public ItemEnchant() {
        objective.add(Count.class);
        objective.add(Item.class);
        objective.add(Enchant.class);
        objective.add(LocationEnchant.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.ENCHANTING_TABLE.parseMaterial()).name("§f物品附魔").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskItemEnchant.class;
    }
}
