package ink.ptms.cronus.builder.task;

import com.google.common.collect.Lists;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:10
 */
public abstract class TaskEntry {

    protected List<Class<? extends TaskData>> objective = Lists.newArrayList();

    public List<Class<? extends TaskData>> getObjective() {
        return objective;
    }

    abstract public ItemStack getItem();

    abstract public Class<? extends QuestTask> getTask();

    public String getKey() {
        return getTask().getAnnotation(Task.class).name();
    }
}
