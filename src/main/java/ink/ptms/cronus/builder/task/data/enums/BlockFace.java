package ink.ptms.cronus.builder.task.data.enums;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskEnum;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-23 16:29
 */
public class BlockFace extends TaskEnum {

    public BlockFace(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public void init(Object data) {
        selected.addAll(BukkitParser.toBlockFace(data).getData());
    }

    @Override
    public Enum[] values() {
        return org.bukkit.block.BlockFace.values();
    }

    @Override
    public String getName() {
        return "方块面向";
    }

    @Override
    public String getKey() {
        return "block-face";
    }

    @Override
    public Material getMaterial() {
        return MaterialControl.QUARTZ.parseMaterial();
    }
}
