package ink.ptms.cronus.builder.task.other;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.text.TextPermission;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.other.TaskPermission;
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
public class Permission extends TaskEntry {

    public Permission() {
        objective.add(TextPermission.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.TRIPWIRE_HOOK.parseMaterial()).name("§f权限判断").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPermission.class;
    }
}
