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
public class Enchant extends TaskData {

    public Enchant(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "enchant";
    }

    @Override
    public ItemStack getItem() {
        List<String> lore = Lists.newArrayList("");
        if (data == null) {
            lore.add("§f无");
        } else {
            BukkitParser.toEnchantment(data).getPoints().stream().map(e -> "§f" + e.asString()).forEach(lore::add);
        }
        return new ItemBuilder(MaterialControl.ENCHANTED_BOOK.parseMaterial()).name("§7目标附魔").lore(lore).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                player.closeInventory();
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入附魔判断规则. ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(data)).hoverText("§7点击").clickSuggest(Utils.NonNull(data))
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7规则: ")
                        .send(player);
                TellrawJson.create()
                        .append("§7§l[§f§lCronus§7§l] §7- ")
                        .append(formatItem("[enchant][expression]+"))
                        .hoverText(String.join("\n", Lists.newArrayList(
                                "§7模糊匹配",
                                "§7根据已有的规则进行物品判断",
                                "",
                                "§7示范:",
                                "§fdurability > 1 §8(耐久大于 1 级)",
                                "§fdurability > 1,damage_all > 2 §8(耐久大于 1 级或锋利大于 2 级)",
                                "",
                                "§7附魔:",
                                "§f使用指令 §8/ct enchants §8获取所有附魔名称"
                        ))).clickSuggest("[enchant][expression]+").send(player);
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
