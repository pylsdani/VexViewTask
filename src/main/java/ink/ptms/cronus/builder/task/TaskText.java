package ink.ptms.cronus.builder.task;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-30 13:37
 */
public abstract class TaskText extends TaskData {

    public TaskText(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    abstract public String getDisplay();

    abstract public Material getMaterial();

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(getMaterial()).name("§7" + getDisplay()).lore("", "§f" + (data == null ? "无" : data)).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                player.closeInventory();
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入" + getDisplay() + ". ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(data)).hoverText("§7点击").clickSuggest(Utils.NonNull(data))
                        .send(player);
                return this;
            }

            @Override
            public boolean after(java.lang.String s) {
                saveData(s);
                open();
                return false;
            }

            @Override
            public void cancel() {
                open();
            }
        });
    }
}
