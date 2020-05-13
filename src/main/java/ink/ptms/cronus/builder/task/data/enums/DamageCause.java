package ink.ptms.cronus.builder.task.data.enums;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskEnum;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-23 16:29
 */
public class DamageCause extends TaskEnum {

    public DamageCause(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public void init(Object data) {
        selected.addAll(BukkitParser.toDamageCause(data).getData());
    }

    @Override
    public Enum[] values() {
        return EntityDamageEvent.DamageCause.values();
    }

    @Override
    public String getName() {
        return "伤害类型";
    }

    @Override
    public String getKey() {
        return "cause";
    }

    @Override
    public Material getMaterial() {
        return MaterialControl.NAME_TAG.parseMaterial();
    }
}
