package ink.ptms.cronus.builder.task.data.entity;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.data.Entity;
import ink.ptms.cronus.service.selector.EntitySelector;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:56
 */
public class EntityVictim extends Entity {

    public EntityVictim(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "victim";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.EGG)
                .name("§7目标实体 (受伤者)")
                .lore(
                        "",
                        "§f" + (data == null ? "无" : Cronus.getCronusService().getService(EntitySelector.class).getSelectDisplay(String.valueOf(data)))
                ).build();
    }

}
