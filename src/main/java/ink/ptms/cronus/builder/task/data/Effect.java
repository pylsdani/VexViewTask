package ink.ptms.cronus.builder.task.data;

import com.google.common.collect.Lists;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-30 13:37
 */
public class Effect extends TaskData {

    public Effect(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "effect";
    }

    @Override
    public ItemStack getItem() {
        List<String> lore = Lists.newArrayList("");
        if (data == null) {
            lore.add("§f无");
        } else {
            BukkitParser.toPotionEffect(data).getPoints().stream().map(e -> "§f" + e.asString()).forEach(lore::add);
        }
        return new ItemBuilder(MaterialControl.POTION.parseMaterial()).name("§7目标效果").lore(lore).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                player.closeInventory();
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入效果判断规则. ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(data)).hoverText("§7点击").clickSuggest(Utils.NonNull(data))
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7规则: ")
                        .send(player);
                TellrawJson.create()
                        .append("§7§l[§f§lCronus§7§l] §7- ")
                        .append(formatItem("[effect][expression]+"))
                        .hoverText(String.join("\n", Lists.newArrayList(
                                "§7模糊匹配",
                                "§7根据已有的规则进行物品判断",
                                "",
                                "§7示范:",
                                "§fspeed > 1 §8(速度大于 1 级)",
                                "§fspeed > 1,slow > 2 §8(速度大于 1 级或缓慢大于 2 级)",
                                "",
                                "§7附魔:",
                                "§f使用指令 §8/ct potions §8获取所有效果名称"
                        ))).clickSuggest("[effect][expression]+").send(player);
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

    @Override
    public Object defaultValue() {
        return null;
    }

    protected String formatItem(String example) {
        return "§8" + example.replaceAll("[.:,;=]", "§f$0§8").replaceAll("\\[(.+?)]", "§8[§7$1§8]§8");
    }
}
