package ink.ptms.cronus.builder.task.data.expression;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskExpression;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:34
 */
public class ExpressionPage extends TaskExpression {

    public ExpressionPage(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getDisplay() {
        return "书本页数";
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getKey() {
        return "page";
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
