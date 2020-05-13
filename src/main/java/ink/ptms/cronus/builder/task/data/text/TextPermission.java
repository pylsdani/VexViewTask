package ink.ptms.cronus.builder.task.data.text;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskText;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:34
 */
public class TextPermission extends TaskText {

    public TextPermission(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getDisplay() {
        return "目标权限";
    }

    @Override
    public Material getMaterial() {
        return Material.TRIPWIRE_HOOK;
    }

    @Override
    public String getKey() {
        return "permission";
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
