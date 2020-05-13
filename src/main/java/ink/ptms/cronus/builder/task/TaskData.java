package ink.ptms.cronus.builder.task;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:18
 */
public abstract class TaskData {

    protected Object data;
    protected Player player;
    protected BuilderTaskData builderTaskData;

    public TaskData(Player player, BuilderTaskData builderTaskData) {
        this.player = player;
        this.builderTaskData = builderTaskData;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void saveData(Object data) {
        builderTaskData.getBuilderTask().getData().put(getKey(), data);
    }

    public void open() {
        builderTaskData.open();
    }

    abstract public String getKey();

    abstract public ItemStack getItem();

    abstract public void onClick(InventoryClickEvent e);

    abstract public Object defaultValue();

}
