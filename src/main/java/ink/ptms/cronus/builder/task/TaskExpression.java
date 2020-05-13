package ink.ptms.cronus.builder.task;

import com.google.common.collect.Lists;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.util.Sxpression;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-30 13:37
 */
public abstract class TaskExpression extends TaskText {

    public TaskExpression(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(getMaterial()).name("§7" + getDisplay()).lore("", "§f" + (data == null ? "无" : new Sxpression(data).translate())).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                player.closeInventory();
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入" + getDisplay() + "表达式. ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(data)).hoverText("§7点击").clickSuggest(Utils.NonNull(data))
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7规则: ")
                        .send(player);
                TellrawJson.create()
                        .append("§7§l[§f§lCronus§7§l] §7- ")
                        .append(formatItem("[symbol] [number]"))
                        .hoverText(String.join("\n", Lists.newArrayList(
                                "§7表达式",
                                "§7根据表达式判断目标内容",
                                "",
                                "§7示范:",
                                "§f> 1 §8(数值大于 1)",
                                "§f< 5 §8(数值小于 5)",
                                "",
                                "§7符号:",
                                "§f> §8(大于)",
                                "§f>= §8(大于等于)",
                                "§f< §8(小于)",
                                "§f<= §8(小于等于)",
                                "§f== §8(等于)",
                                "§f≈≈ §8(等于并忽略大小写)"
                        ))).clickSuggest("[symbol] [number]").send(player);
                return this;
            }

            @Override
            public boolean after(String s) {
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

    protected String formatItem(String example) {
        return "§8" + example.replaceAll("[.:,;=]", "§f$0§8").replaceAll("\\[(.+?)]", "§8[§7$1§8]§8");
    }
}
