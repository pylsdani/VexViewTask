package ink.ptms.cronus.builder.task.data.block;

import com.google.common.collect.Lists;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.data.Block;
import ink.ptms.cronus.internal.version.MaterialControl;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-23 16:16
 */
public class BlockAgainst extends Block {

    public BlockAgainst(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "block-against";
    }

    @Override
    public ItemStack getItem() {
        List<String> lore = Lists.newArrayList("");
        if (selected.isEmpty()) {
            lore.add("§f无");
        }
        for (int i = 0; i < selected.size() && i < 8; i++) {
            lore.add("§f" + Items.getName(selected.get(i)));
        }
        if (selected.size() > 8) {
            lore.add("§f...");
        }
        lore.add("§8§m                  ");
        lore.add("§7选择: §8左键");
        lore.add("§7导入: §8右键");
        lore.add("§7删除: §8中键");
        return new ItemBuilder(MaterialControl.GRASS_BLOCK.parseMaterial())
                .name("§7方块类型 (放置前)")
                .lore(lore).build();
    }
}
