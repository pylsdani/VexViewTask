package ink.ptms.cronus.builder.task.data.text;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskText;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:34
 */
public class TextCommand extends TaskText {

    public TextCommand(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getDisplay() {
        return "指令内容";
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getKey() {
        return "command";
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
