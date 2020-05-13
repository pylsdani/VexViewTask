package ink.ptms.cronus.builder.task.damage;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.count.CountDamage;
import ink.ptms.cronus.builder.task.data.entity.EntityAttacker;
import ink.ptms.cronus.builder.task.data.enums.DamageCause;
import ink.ptms.cronus.builder.task.data.item.ItemWeapon;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDamaged;
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
public class PlayerDamaged extends TaskEntry {

    public PlayerDamaged() {
        objective.add(CountDamage.class);
        objective.add(EntityAttacker.class);
        objective.add(ItemWeapon.class);
        objective.add(DamageCause.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.IRON_CHESTPLATE.parseMaterial()).name("§f承受伤害").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerDamaged.class;
    }
}
