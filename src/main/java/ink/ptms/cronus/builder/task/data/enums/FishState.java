package ink.ptms.cronus.builder.task.data.enums;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskEnum;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-23 16:29
 */
public class FishState extends TaskEnum {

    public FishState(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public void init(Object data) {
        selected.addAll(BukkitParser.toFishState(data).getData());
    }

    @Override
    public Enum[] values() {
        return PlayerFishEvent.State.values();
    }

    @Override
    public String getName() {
        return "鱼钩状态";
    }

    @Override
    public String getKey() {
        return "state";
    }

    @Override
    public Material getMaterial() {
        return MaterialControl.FISHING_ROD.parseMaterial();
    }
}
