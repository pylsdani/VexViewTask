package ink.ptms.cronus.builder.task.total;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Item;
import ink.ptms.cronus.builder.task.data.expression.ExpressionExp;
import ink.ptms.cronus.builder.task.data.expression.ExpressionTotal;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.total.TaskPlayerExp;
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
public class PlayerExp extends TaskEntry {

    public PlayerExp() {
        objective.add(Item.class);
        objective.add(ExpressionTotal.class);
        objective.add(ExpressionExp.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.GOLD_NUGGET.parseMaterial()).name("§f经验变化").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerExp.class;
    }
}
