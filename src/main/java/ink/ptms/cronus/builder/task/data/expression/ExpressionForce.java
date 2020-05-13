package ink.ptms.cronus.builder.task.data.expression;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskExpression;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:34
 */
public class ExpressionForce extends TaskExpression {

    public ExpressionForce(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getDisplay() {
        return "力度";
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_POWDER;
    }

    @Override
    public String getKey() {
        return "force";
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
