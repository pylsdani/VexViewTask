package ink.ptms.cronus.builder.task.data.enums;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskEnum;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-23 16:29
 */
public class RegainReason extends TaskEnum {

    public RegainReason(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public void init(Object data) {
        selected.addAll(BukkitParser.toTeleportCause(data).getData());
    }

    @Override
    public Enum[] values() {
        return EntityRegainHealthEvent.RegainReason.values();
    }

    @Override
    public String getName() {
        return "治疗原因";
    }

    @Override
    public String getKey() {
        return "reason";
    }

    @Override
    public Material getMaterial() {
        return MaterialControl.GHAST_TEAR.parseMaterial();
    }
}
