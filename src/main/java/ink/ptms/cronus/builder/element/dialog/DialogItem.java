package ink.ptms.cronus.builder.element.dialog;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.version.MaterialControl;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.ClickType;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-30 11:05
 */
public class DialogItem {

    private Dialog dialog;
    private boolean toggle;

    public void open(Player player, Dialog dialog) {
        player.openInventory(MenuBuilder.builder(Cronus.getInst())
                .title("结构编辑 : 显示材质")
                .rows(3)
                .items("#########", "$$$$%$$$$", "####@####")
                .put('%', Items.isNull(dialog.getItem()) ? null : MaterialControl.fromItem(dialog.getItem()).parseItem())
                .put('#', new ItemBuilder(MaterialControl.BLACK_STAINED_GLASS_PANE.parseItem()).build())
                .put('$', new ItemBuilder(MaterialControl.BLUE_STAINED_GLASS_PANE.parseItem()).build())
                .put('@', new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem()).name("§c上级目录").lore("", "§7点击").build())
                .event(e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        int slot = e.castClick().getRawSlot();
                        if ((slot >= 0 && slot <= 12) || (slot >= 14 && slot <= 26)) {
                            e.castClick().setCancelled(true);
                        }
                        if (slot == 22) {
                            toggle = true;
                            ItemStack item = e.castClick().getInventory().getItem(13);
                            dialog.setItem(Items.isNull(item) ? null : MaterialControl.fromItem(item).parseItem());
                            dialog.open(player);
                        }
                    }
                }).close(e -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            ItemStack item = e.getInventory().getItem(13);
                            dialog.setItem(Items.isNull(item) ? null : MaterialControl.fromItem(item).parseItem());
                            dialog.open(player);
                        }, 1);
                    }
                }).build());
    }
}
