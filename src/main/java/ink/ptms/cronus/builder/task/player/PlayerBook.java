package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.text.TextContent;
import ink.ptms.cronus.builder.task.data.expression.ExpressionPage;
import ink.ptms.cronus.builder.task.data.text.TextTitle;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerBook;
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
public class PlayerBook extends TaskEntry {

    public PlayerBook() {
        objective.add(Count.class);
        objective.add(TextTitle.class);
        objective.add(TextContent.class);
        objective.add(ExpressionPage.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.BOOK.parseMaterial()).name("§f书本编写").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerBook.class;
    }
}
