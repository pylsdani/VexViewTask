package ink.ptms.cronus.builder.task.data;

import com.google.common.collect.Lists;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:24
 */
public class Location extends TaskData {

    protected ink.ptms.cronus.internal.bukkit.Location.Mode mode;
    protected List<org.bukkit.Location> location = Lists.newArrayList();
    protected org.bukkit.Location[] locationArea = new org.bukkit.Location[2];

    public Location(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "location";
    }

    @Override
    public ItemStack getItem() {
        if (mode == ink.ptms.cronus.internal.bukkit.Location.Mode.POINT) {
            List<String> lore = Lists.newArrayList("");
            for (int i = 0; i < location.size() && i < 8; i++) {
                lore.add("§f" + Utils.fromLocation(location.get(i)));
            }
            if (location.size() > 8) {
                lore.add("§7...");
            }
            lore.addAll(Lists.newArrayList("§8§m                  ", "§7单项: §8左键", "§7区域: §8右键", "§7范围: §8中键"));
            return new ItemBuilder(Material.MAP).name("§7目标坐标").lore(lore).build();
        } else {
            return new ItemBuilder(Material.MAP)
                    .name("§7目标坐标")
                    .lore(
                            "",
                            "§f" + (data == null ? "无" : data),
                            "§8§m                  ",
                            "§7单项: §8左键",
                            "§7区域: §8右键",
                            "§7范围: §8中键"
                    ).build();
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        // 左键
        if (e.isLeftClick()) {
            if (mode != ink.ptms.cronus.internal.bukkit.Location.Mode.POINT) {
                mode = ink.ptms.cronus.internal.bukkit.Location.Mode.POINT;
                location.clear();
            }
            Catchers.call(player, new LocationPoint(player, this));
        }
        // 右键
        else if (e.isRightClick()) {
            if (mode != ink.ptms.cronus.internal.bukkit.Location.Mode.AREA) {
                mode = ink.ptms.cronus.internal.bukkit.Location.Mode.AREA;
                location.clear();
            }
            Catchers.call(player, new LocationArea(player, this));
        }
        // 中键
        else if (e.getClick().isCreativeAction()) {
            if (mode != ink.ptms.cronus.internal.bukkit.Location.Mode.RANGE) {
                mode = ink.ptms.cronus.internal.bukkit.Location.Mode.RANGE;
                location.clear();
            }
            Catchers.call(player, new LocationRange(player, this));
        }
    }

    @Override
    public Object defaultValue() {
        return null;
    }

    public void save(Object in) {
        builderTaskData.getBuilderTask().getData().put(getKey(), in);
    }

    public static class LocationPoint implements Catchers.Catcher {

        private Player player;
        private Location location;

        public LocationPoint(Player player, Location location) {
            this.player = player;
            this.location = location;
        }

        @Override
        public Catchers.Catcher before() {
            player.closeInventory();
            TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7请用")
                    .append("§f下界之星").hoverText("§7点击获取").clickCommand("/give " + player.getName() + " nether_star")
                    .append("§7点击目标方块或输入坐标. ")
                    .append("§a(完成)").hoverText("§7点击").clickCommand("quit()")
                    .send(player);
            TellrawJson.create()
                    .append("§7§l[§f§lCronus§7§l] §7可用：")
                    .send(player);
            TellrawJson.create()
                    .append("§7§l[§f§lCronus§7§l] §7- ")
                    .append(format("[world],[x],[y],[z]"))
                    .hoverText(String.join("\n", Lists.newArrayList(
                            "§7坐标选择",
                            "",
                            "§7示范:",
                            "§fworld,0,80,0 §8(world 世界 x:0 y:80 z:0)",
                            "§fworld,0,80,0;world,0,90,0 §8(world 世界 x:0 y:80 z:0 和 world 世界 x:0 y:90 z:0)",
                            "",
                            "§7多坐标之间用 §f\";\" §7符号分隔"
                    ))).clickSuggest("[world],[x],[y],[z]").send(player);
            return this;
        }

        @Override
        public boolean after(String s) {
            location.save(s);
            location.builderTaskData.open();
            return false;
        }

        @Override
        public void cancel() {
            location.save(location.location.isEmpty() ? null : location.location.stream().map(l -> l.getWorld().getName() + "," + toString(l)).collect(Collectors.joining(";")));
            location.builderTaskData.open();
        }

        public String toString(org.bukkit.Location location) {
            return location.getX() + "," + location.getY() + "," + location.getZ();
        }

        public Player getPlayer() {
            return player;
        }

        public Location getLocation() {
            return location;
        }
    }

    public static class LocationArea implements Catchers.Catcher {

        private Player player;
        private Location location;

        public LocationArea(Player player, Location location) {
            this.player = player;
            this.location = location;
        }

        @Override
        public Catchers.Catcher before() {
            player.closeInventory();
            TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7请用")
                    .append("§f下界之星").hoverText("§7点击获取").clickCommand("/give " + player.getName() + " nether_star")
                    .append("§7点击区域两点或输入坐标. ")
                    .append("§a(完成)").hoverText("§7点击").clickCommand("quit()")
                    .send(player);
            TellrawJson.create()
                    .append("§7§l[§f§lCronus§7§l] §7可用：")
                    .send(player);
            TellrawJson.create()
                    .append("§7§l[§f§lCronus§7§l] §7- ")
                    .append(format("[world]:[x1],[y1],[z1]~[x2],[y2],[z2]"))
                    .hoverText(String.join("\n", Lists.newArrayList(
                            "§7坐标选择",
                            "",
                            "§7示范:",
                            "§fworld:0,80,0~10,80,10 §8(world 世界 x:0 y:80 z:0 至 x:10 y:80 z:10)",
                            "",
                            "§7左侧坐标为低点, 右侧坐标为高点"
                    ))).clickSuggest("[world]:[x1],[y1],[z1]~[x2],[y2],[z2]").send(player);
            return this;
        }

        @Override
        public boolean after(String s) {
            location.save(s);
            location.builderTaskData.open();
            return false;
        }

        @Override
        public void cancel() {
            location.save(location.locationArea[0] == null || location.locationArea[1] == null ? null : location.locationArea[0].getWorld().getName() + ":" + toString(location.locationArea[0]) + "~" + toString(location.locationArea[1]));
            location.builderTaskData.open();
        }

        public String toString(org.bukkit.Location location) {
            return location.getX() + "," + location.getY() + "," + location.getZ();
        }

        public Player getPlayer() {
            return player;
        }

        public Location getLocation() {
            return location;
        }
    }

    public static class LocationRange implements Catchers.Catcher {

        private Player player;
        private Location location;

        public LocationRange(Player player, Location location) {
            this.player = player;
            this.location = location;
        }

        @Override
        public Catchers.Catcher before() {
            player.closeInventory();
            TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7请在对话框中输入以§f你§7为中心的范围数字. ")
                    .append("§a(完成)").hoverText("§7点击").clickCommand("quit()")
                    .send(player);
            return this;
        }

        @Override
        public boolean after(String s) {
            org.bukkit.Location center = player.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5);
            location.save(player.getWorld().getName() + ":" + center.getX() + "," + center.getY() + "," + center.getZ() + " r:" + NumberConversions.toInt(s));
            location.builderTaskData.open();
            return false;
        }

        @Override
        public void cancel() {
            location.builderTaskData.open();
        }

        public Player getPlayer() {
            return player;
        }

        public Location getLocation() {
            return location;
        }
    }

    public ink.ptms.cronus.internal.bukkit.Location.Mode getMode() {
        return mode;
    }

    public List<org.bukkit.Location> getLocation() {
        return location;
    }

    public org.bukkit.Location[] getLocationArea() {
        return locationArea;
    }

    private static String format(String example) {
        return "§8" + example.replaceAll("[.:]", "§f$0§8").replaceAll("\\[(.+?)]", "§8[§7$1§8]§8");
    }
}
