package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerResurrect;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class PlayerResurrect extends TaskEntry {

    public PlayerResurrect() {
        objective.add(Count.class);
    }

    @Override
    public ItemStack getItem() {
        Material material = MaterialControl.TOTEM_OF_UNDYING.parseMaterial();
        if (material == null) {
            return new ItemBuilder(Material.BEDROCK).name("§f复活").lore("", "§c不兼容当前版本").flags(ItemFlag.values()).build();
        } else {
            return new ItemBuilder(material).name("§f复活").lore("", "§7点击选择").flags(ItemFlag.values()).build();
        }
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerResurrect.class;
    }
}
