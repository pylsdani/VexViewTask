package ink.ptms.cronus.builder.task.data.text;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskText;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:34
 */
public class TextValue extends TaskText {

    public TextValue(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getDisplay() {
        return "变量 (值)";
    }

    @Override
    public Material getMaterial() {
        return Material.MAP;
    }

    @Override
    public String getKey() {
        return "value";
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
