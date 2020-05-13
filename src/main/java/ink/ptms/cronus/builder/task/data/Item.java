package ink.ptms.cronus.builder.task.data;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.ClickType;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-30 13:37
 */
public class Item extends TaskData {

    private boolean toggle;

    public Item(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "item";
    }

    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        ink.ptms.cronus.internal.bukkit.ItemStack cronusItem = data == null ? null : BukkitParser.toItemStack(data);
        return new ItemBuilder(MaterialControl.APPLE.parseMaterial())
                .name("§7目标物品")
                .lore(
                        "",
                        "§f" + (data == null ? "无" : (cronusItem.getBukkitItem() == null ? cronusItem.asString() : "bukkit:" + Items.getName(cronusItem.getBukkitItem()))),
                        "§8§m                  ",
                        "§7物品导入: §8左键",
                        "§7模糊判断: §8右键"
                ).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        toggle = false;
        // 导入
        if (e.isLeftClick()) {
            ink.ptms.cronus.internal.bukkit.ItemStack cronusItem = data == null ? null : BukkitParser.toItemStack(data);
            player.openInventory(MenuBuilder.builder(Cronus.getInst())
                    .title("实例选择 : 物品导入")
                    .rows(3)
                    .items("#########", "$$$$%$$$$", "####@####")
                    .put('%', cronusItem != null && cronusItem.getBukkitItem() != null ? cronusItem.getBukkitItem() : null)
                    .put('#', new ItemBuilder(MaterialControl.BLACK_STAINED_GLASS_PANE.parseItem()).build())
                    .put('$', new ItemBuilder(MaterialControl.BLUE_STAINED_GLASS_PANE.parseItem()).build())
                    .put('@', new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem()).name("§c上级目录").lore("", "§7点击").build())
                    .event(c -> {
                        if (c.getClickType() == ClickType.CLICK) {
                            int slot = c.castClick().getRawSlot();
                            if ((slot >= 0 && slot <= 12) || (slot >= 14 && slot <= 26)) {
                                c.castClick().setCancelled(true);
                            }
                            if (slot == 22) {
                                toggle = true;
                                ItemStack item = c.castClick().getInventory().getItem(13);
                                saveData(Items.isNull(item) ? null : BukkitParser.fromItemStack(item));
                                open();
                            }
                        }
                    }).close(c -> {
                        if (!toggle) {
                            Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                                ItemStack item = c.getInventory().getItem(13);
                                saveData(Items.isNull(item) ? null : BukkitParser.fromItemStack(item));
                                open();
                            }, 1);
                        }
                    }).build());
        }
        // 模糊
        else if (e.isRightClick()) {
            Catchers.call(player, new Catchers.Catcher() {
                @Override
                public Catchers.Catcher before() {
                    player.closeInventory();
                    TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入物品判断规则. ")
                            .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                            .send(player);
                    TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                            .append("§f" + Utils.NonNull(data)).hoverText("§7点击").clickSuggest(Utils.NonNull(data))
                            .send(player);
                    TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7规则: ")
                            .send(player);
                    TellrawJson.create()
                            .append("§7§l[§f§lCronus§7§l] §7- ")
                            .append(formatItem("t=[type],n=[name],l=[lore],d=[damage],a=[amount]"))
                            .hoverText(String.join("\n", Lists.newArrayList(
                                    "§7模糊匹配",
                                    "§7根据已有的规则进行物品判断",
                                    "",
                                    "§7示范:",
                                    "§ft=diamond §8(材质为 diamond 的任何物品)",
                                    "§fn=abc,l=def §8(名称为 abc 描述为 def 的任何物品)",
                                    "",
                                    "§7单位:",
                                    "§ftype §8(材质, 别名: t)",
                                    "§fname §8(名称, 别名: n)",
                                    "§flore §8(描述, 别名: l)",
                                    "§fdamage §8(耐久/附加值, 别名: d)",
                                    "§famount §8(数量, 别名: a)"
                            ))).clickSuggest("t=[type],n=[name],l=[lore],d=[damage],a=[amount]").send(player);
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
    }

    @Override
    public Object defaultValue() {
        return null;
    }

    protected String formatItem(String example) {
        return "§8" + example.replaceAll("[.:,;=]", "§f$0§8").replaceAll("\\[(.+?)]", "§8[§7$1§8]§8");
    }
}
