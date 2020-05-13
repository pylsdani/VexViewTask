package ink.ptms.cronus.builder.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.internal.version.MaterialControl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:12
 */
public class Cache {

    public static final List<ItemStack> BLOCKS = Lists.newArrayList();
    public static final List<String> NOT_ITEM = Lists.newArrayList();
    public static final Map<Material, Integer> DAMAGEABLE = Maps.newHashMap();

    static {
        // 非物品
        NOT_ITEM.add("WATER");
        NOT_ITEM.add("LAVA");
        NOT_ITEM.add("PISTON_EXTENSION");
        NOT_ITEM.add("PISTON_MOVING_PIECE");
        NOT_ITEM.add("WALL_SIGN");
        NOT_ITEM.add("BREWING_STAND");
        NOT_ITEM.add("CAULDRON");
        NOT_ITEM.add("WALL_BANNER");
        NOT_ITEM.add("SPRUCE_DOOR");
        NOT_ITEM.add("BIRCH_DOOR");
        NOT_ITEM.add("JUNGLE_DOOR");
        NOT_ITEM.add("ACACIA_DOOR");
        NOT_ITEM.add("DARK_OAK_DOOR");
        NOT_ITEM.add("FROSTED_ICE");
        // 1.11-
        NOT_ITEM.add("BED_BLOCK");
        NOT_ITEM.add("STATIONARY_WATER");
        NOT_ITEM.add("STATIONARY_LAVA");
        NOT_ITEM.add("DOUBLE_STEP");
        NOT_ITEM.add("BURNING_FURNACE");
        NOT_ITEM.add("SIGN_POST");
        NOT_ITEM.add("WOODEN_DOOR");
        NOT_ITEM.add("IRON_DOOR_BLOCK");
        NOT_ITEM.add("GLOWING_REDSTONE_ORE");
        NOT_ITEM.add("CAKE_BLOCK");
        NOT_ITEM.add("REDSTONE_LAMP_ON");
        NOT_ITEM.add("WOOD_DOUBLE_STEP");
        NOT_ITEM.add("STANDING_BANNER");
        NOT_ITEM.add("DAYLIGHT_DETECTOR_INVERTED");
        NOT_ITEM.add("DOUBLE_STONE_SLAB2");
        NOT_ITEM.add("PURPUR_DOUBLE_SLAB");
        // 1.13+
        NOT_ITEM.add("CORAL_WALL_FAN");
        NOT_ITEM.add("COLUMN");
        NOT_ITEM.add("KELP_PLANT");
        NOT_ITEM.add("MOVING_PISTON");
        NOT_ITEM.add("PISTON_HEAD");
        NOT_ITEM.add("TALL_SEAGRASS");
        // 1.14+
        NOT_ITEM.add("BAMBOO_SAPLING");
        NOT_ITEM.add("POTTED_BAMBOO");
        NOT_ITEM.add("POTTED_CORNFLOWER");
        NOT_ITEM.add("POTTED_LILY_OF_THE_VALLEY");
        NOT_ITEM.add("POTTED_WITHER_ROSE");
        NOT_ITEM.add("SWEET_BERRY_BUSH");
        // 获取方块
        if (MaterialControl.isNewVersion()) {
            for (Material material : Material.values()) {
                if (material.isBlock() && !material.isTransparent() && MaterialControl.matchMaterialControl(material) != null && NOT_ITEM.stream().noneMatch(s -> material.name().endsWith(s))) {
                    try {
                        BLOCKS.add(new ItemStack(material));
                    } catch (Throwable ignored) {
                    }
                }
            }
        } else {
            // 附加值特例
            DAMAGEABLE.put(Material.STONE, 6);
            DAMAGEABLE.put(Material.DIRT, 2);
            DAMAGEABLE.put(Material.WOOD, 5);
            DAMAGEABLE.put(Material.SAND, 1);
            DAMAGEABLE.put(Material.LOG, 3);
            DAMAGEABLE.put(Material.SPONGE, 1);
            DAMAGEABLE.put(Material.SANDSTONE, 2);
            DAMAGEABLE.put(Material.WOOL, 15);
            DAMAGEABLE.put(Material.STEP, 7);
            DAMAGEABLE.put(Material.STAINED_GLASS, 15);
            DAMAGEABLE.put(Material.SMOOTH_BRICK, 3);
            DAMAGEABLE.put(Material.WOOD_STEP, 5);
            DAMAGEABLE.put(Material.QUARTZ_BLOCK, 2);
            DAMAGEABLE.put(Material.STAINED_CLAY, 15);
            DAMAGEABLE.put(Material.LOG_2, 1);
            DAMAGEABLE.put(Material.PRISMARINE, 2);
            DAMAGEABLE.put(Material.RED_SANDSTONE, 2);
            DAMAGEABLE.put(Material.SAPLING, 5);
            DAMAGEABLE.put(Material.LEAVES, 3);
            DAMAGEABLE.put(Material.LONG_GRASS, 2);
            DAMAGEABLE.put(Material.RED_ROSE, 8);
            DAMAGEABLE.put(Material.MONSTER_EGGS, 5);
            DAMAGEABLE.put(Material.COBBLE_WALL, 1);
            DAMAGEABLE.put(Material.ANVIL, 2);
            DAMAGEABLE.put(Material.STAINED_GLASS_PANE, 15);
            DAMAGEABLE.put(Material.LEAVES_2, 1);
            DAMAGEABLE.put(Material.CARPET, 15);
            DAMAGEABLE.put(Material.DOUBLE_PLANT, 5);
            DAMAGEABLE.put(Material.BED, 15);
            DAMAGEABLE.put(Material.SKULL, 5);
            DAMAGEABLE.put(Material.BANNER, 15);
            // 1.12
            try {
                DAMAGEABLE.put(Material.CONCRETE, 15);
                DAMAGEABLE.put(Material.CONCRETE_POWDER, 15);
            } catch (Throwable ignored) {
            }
            // 获取物品
            for (Material material : Material.values()) {
                if (material.isBlock() && !material.isTransparent() && MaterialControl.matchMaterialControl(material) != null && !NOT_ITEM.contains(material.name())) {
                    try {
                        if (DAMAGEABLE.containsKey(material)) {
                            int damage = DAMAGEABLE.get(material);
                            for (int i = 0; i < damage + 1; i++) {
                                // 木半砖特例
                                if (material == Material.STEP && i == 2) {
                                    continue;
                                }
                                BLOCKS.add(new ItemStack(material, 1, (short) i));
                            }
                        } else {
                            BLOCKS.add(new ItemStack(material));
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
    }

}
