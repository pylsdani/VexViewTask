package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.builder.element.condition.MatchEntry;
import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.NumberConversions;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-22 21:49
 */
public class BuilderTask extends BuilderQuest {

    private BuilderTaskList list;
    private Player player;
    private String type;
    private TaskEntry typeEntry;
    private Map<String, Object> data = Maps.newHashMap();
    private List<String> actionNext = Lists.newArrayList();
    private List<String> actionRestart = Lists.newArrayList();
    private MatchEntry conditionNext;
    private MatchEntry conditionRestart;
    private double guideDistance = 2;
    private Location guideTarget;
    private List<String> guideText = Lists.newArrayList(id, "距离 {distance}m");
    private String status;
    private boolean toggle;

    public BuilderTask(String id) {
        super(id);
    }

    public BuilderTask(ConfigurationSection section) {
        super(section.getName());
        if (section.contains("type")) {
            typeEntry = Builders.fromType(type = section.getString("type"));
        }
        if (section.contains("data")) {
            data = section.getConfigurationSection("data").getValues(false);
        }
        if (section.contains("action.next")) {
            actionNext = section.getStringList("action.next");
        }
        if (section.contains("action.restart")) {
            actionRestart = section.getStringList("action.restart");
        }
        if (section.contains("action.success")) {
            actionSuccess = section.getStringList("action.success");
        }
        if (section.contains("condition")) {
            conditionNext = new MatchEntry(section.get("condition"));
        }
        if (section.contains("restart")) {
            conditionRestart = new MatchEntry(section.get("restart"));
        }
        if (section.contains("guide.distance")) {
            guideDistance = section.getDouble("guide.distance");
        }
        if (section.contains("guide.target")) {
            guideTarget = BukkitParser.toLocation(section.getString("guide.target"));
        }
        if (section.contains("guide.text")) {
            guideText = section.getStringList("guide.text");
        }
        if (section.contains("status")) {
            status = section.getString("status");
        }
    }

    public void export(ConfigurationSection section) {
        section.set("type", type);
        if (!data.isEmpty()) {
            section.set("data", data.entrySet().stream().filter(entry -> Objects.nonNull(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b)));
        }
        if (!actionNext.isEmpty()) {
            section.set("action.next", actionNext);
        }
        if (!actionRestart.isEmpty()) {
            section.set("action.restart", actionRestart);
        }
        if (!actionSuccess.isEmpty()) {
            section.set("action.success", actionSuccess);
        }
        if (conditionNext != null) {
            conditionNext.save(section, "condition");
        }
        if (conditionRestart != null) {
            conditionRestart.save(section, "restart");
        }
        if (guideTarget != null) {
            section.set("guide.distance", guideDistance);
            section.set("guide.target", Utils.fromLocation(guideTarget.toBukkit()));
            section.set("guide.text", guideText);
        }
        if (status != null) {
            section.set("status", status);
        }
    }

    @Override
    public void open(Player player) {
        open(player, list);
    }

    public void open(Player player, BuilderTaskList list) {
        this.toggle = false;
        this.player = player;
        this.list = list;
        Inventory inventory = Builders.normal("条目编辑 : " + id,
                e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                toggle = true;
                                new BuilderTaskType(this).open(player);
                                break;
                            case 11:
                                if (typeEntry != null) {
                                    toggle = true;
                                    new BuilderTaskData(player, this).open();
                                }
                                break;
                            case 12: {
                                toggle = true;
                                BuilderCondition condition = new BuilderCondition(conditionNext, e.getClicker(), "条目进行条件");
                                condition.setCloseTask(c -> {
                                    conditionNext = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 13: {
                                toggle = true;
                                BuilderCondition condition = new BuilderCondition(conditionRestart, e.getClicker(), "条目重置条件");
                                condition.setCloseTask(c -> {
                                    conditionRestart = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 14:
                                toggle = true;
                                editString(e.getClicker(), "任务状态显示", status, r -> status = r);
                                break;
                            case 19:
                                toggle = true;
                                new BuilderListEffect("条目进行动作", actionNext).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 20:
                                toggle = true;
                                new BuilderListEffect("条目完成动作", actionSuccess).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 21:
                                toggle = true;
                                new BuilderListEffect("条目重置动作", actionRestart).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 22:
                                if (e.castClick().isLeftClick()) {
                                    if (e.castClick().isShiftClick() && !Utils.isActionCooldown(e.getClicker())) {
                                        guideDistance = Math.min(NumberConversions.toDouble(guideDistance) + 1, 10);
                                    } else {
                                        guideDistance = Math.min(NumberConversions.toDouble(guideDistance) + 0.1, 10);
                                    }
                                } else if (e.castClick().isRightClick()) {
                                    if (e.castClick().isShiftClick() && !Utils.isActionCooldown(e.getClicker())) {
                                        guideDistance = Math.max(NumberConversions.toDouble(guideDistance) - 1, 0);
                                    } else {
                                        guideDistance = Math.max(NumberConversions.toDouble(guideDistance) - 0.1, 0);
                                    }
                                }
                                e.castClick().setCurrentItem(new ItemBuilder(Material.COMPASS)
                                        .name("§b引导显示距离")
                                        .lore("",
                                                "§f" + guideDistance,
                                                "§8§m                  ",
                                                "§7+0.1: §8左键",
                                                "§7-0.1: §8右键",
                                                "§7+1: §8SHIFT+左键",
                                                "§7-1: §8SHIFT+右键"
                                        ).build());
                                break;
                            case 23:
                                guideTarget = BukkitParser.toLocation(player.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5));
                                e.castClick().setCurrentItem(new ItemBuilder(Material.COMPASS)
                                        .name("§b引导目标位置")
                                        .lore("", "§f" + guideTarget.asString())
                                        .build());
                                break;
                            case 24:
                                toggle = true;
                                new BuilderList("引导显示内容", guideText).open(e.getClicker(), 0, c -> open(e.getClicker()), Maps::newHashMap);
                                break;
                            case 49:
                                toggle = true;
                                list.open(player);
                                break;
                        }
                    }
                },
                e -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            list.open(player);
                        }, 1);
                    }
                }
        );
        inventory.setItem(10, new ItemBuilder(Material.BOOK)
                .name("§b条目类型")
                .lore("", typeEntry == null ? "§f无" : "§f" + TLocale.Translate.setUncolored(Items.getName(typeEntry.getItem())))
                .build());
        inventory.setItem(11, new ItemBuilder(typeEntry == null ? Material.BARRIER : Material.PAPER)
                .name("§b条目要求")
                .lore("", data.isEmpty() ? "§f无" : "§f...")
                .build());
        inventory.setItem(12, new ItemBuilder(Material.TRIPWIRE_HOOK)
                .name("§b条目进行条件")
                .lore(toLore(conditionNext == null ? Lists.newArrayList() : conditionNext.asList(0)))
                .build());
        inventory.setItem(13, new ItemBuilder(Material.TRIPWIRE_HOOK)
                .name("§b条目重置条件")
                .lore(toLore(conditionRestart == null ? Lists.newArrayList() : conditionRestart.asList(0)))
                .build());
        inventory.setItem(14, new ItemBuilder(Material.NAME_TAG)
                .name("§b任务状态显示")
                .lore("", "§f" + (status == null ? "无" : status))
                .build());
        inventory.setItem(19, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b条目进行动作")
                .lore(toLore(actionNext))
                .build());
        inventory.setItem(20, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b条目完成动作")
                .lore(toLore(actionSuccess))
                .build());
        inventory.setItem(21, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b条目重置动作")
                .lore(toLore(actionRestart))
                .build());
        inventory.setItem(22, new ItemBuilder(Material.COMPASS)
                .name("§b引导显示距离")
                .lore("",
                        "§f" + guideDistance,
                        "§8§m                  ",
                        "§7+0.1: §8左键",
                        "§7-0.1: §8右键",
                        "§7+1: §8SHIFT+左键",
                        "§7-1: §8SHIFT+右键"
                ).build());
        inventory.setItem(23, new ItemBuilder(Material.COMPASS)
                .name("§b引导目标位置")
                .lore("", "§f" + (guideTarget == null ? "无" : guideTarget.asString()))
                .build());
        inventory.setItem(24, new ItemBuilder(Material.COMPASS)
                .name("§b引导显示内容")
                .lore(toLore(guideText))
                .build());
        inventory.setItem(49, new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem())
                .name("§c上级目录")
                .lore("", "§7点击")
                .build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }

    public Player getPlayer() {
        return player;
    }

    public BuilderTaskList getList() {
        return list;
    }

    public String getType() {
        return type;
    }

    public TaskEntry getTypeEntry() {
        return typeEntry;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public List<String> getActionNext() {
        return actionNext;
    }

    public List<String> getActionRestart() {
        return actionRestart;
    }

    public MatchEntry getConditionRestart() {
        return conditionRestart;
    }

    public double getGuideDistance() {
        return guideDistance;
    }

    public Location getGuideTarget() {
        return guideTarget;
    }

    public List<String> getGuideText() {
        return guideText;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeEntry(TaskEntry typeEntry) {
        this.typeEntry = typeEntry;
    }
}
