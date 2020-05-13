package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.expression.ExpressionForce;
import ink.ptms.cronus.builder.task.data.item.ItemBow;
import ink.ptms.cronus.builder.task.data.entity.EntityProjectile;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerShoot;
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
public class PlayerShoot extends TaskEntry {

    public PlayerShoot() {
        objective.add(Count.class);
        objective.add(ItemBow.class);
        objective.add(EntityProjectile.class);
        objective.add(ExpressionForce.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.BOW.parseMaterial()).name("§f射击").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerShoot.class;
    }
}
