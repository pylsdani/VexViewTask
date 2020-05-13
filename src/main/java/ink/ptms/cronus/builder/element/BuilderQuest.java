package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.condition.MatchEntry;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.time.Time;
import ink.ptms.cronus.database.data.time.TimeType;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.program.ProgramLoader;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Files;
import io.izzel.taboolib.util.Times;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.inventory.ClickType;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 19:50
 */
public class BuilderQuest extends CronusCommand {

    protected String id;
    protected String display;
    protected List<String> bookTag = Lists.newArrayList();
    protected String label;
    protected String timeout;
    protected String cooldown;
    protected List<String> actionAccept = Lists.newArrayList();
    protected List<String> actionAcceptFail = Lists.newArrayList();
    protected List<String> actionSuccess = Lists.newArrayList();
    protected List<String> actionFailure = Lists.newArrayList();
    protected List<String> actionCooldown = Lists.newArrayList();
    protected MatchEntry conditionAccept;
    protected MatchEntry conditionFailure;
    protected BuilderStageList stageList;

    public BuilderQuest(String id) {
        this.id = id;
    }

    public BuilderQuest(File file) {
        YamlConfiguration yaml = Files.loadYaml(file);
        for (String id : yaml.getKeys(false)) {
            import0(yaml.getConfigurationSection(id));
            return;
        }
    }

    public void import0(ConfigurationSection section) {
        id = section.getName();
        display = section.getString("display");
        label = section.getString("label");
        timeout = section.getString("timeout");
        cooldown = section.getString("cooldown");
        if (section.contains("booktag")) {
            bookTag = section.getStringList("booktag");
        }
        if (section.contains("action.accept")) {
            actionAccept = section.getStringList("action.accept");
        }
        if (section.contains("action.accept")) {
            actionAcceptFail = section.getStringList("action.accept-fail");
        }
        if (section.contains("action.success")) {
            actionSuccess = section.getStringList("action.success");
        }
        if (section.contains("action.failure")) {
            actionFailure = section.getStringList("action.failure");
        }
        if (section.contains("action.cooldown")) {
            actionCooldown = section.getStringList("action.cooldown");
        }
        if (section.contains("condition.accept")) {
            conditionAccept = new MatchEntry(section.get("condition.accept"));
        }
        if (section.contains("condition.failure")) {
            conditionFailure = new MatchEntry(section.get("condition.failure"));
        }
        if (section.contains("stage")) {
            stageList = new BuilderStageList(section.getConfigurationSection("stage"));
        }
    }

    public void export() {
        File file = Files.file(new File(Cronus.getCronusLoader().getFolder(), "builder/" + id + ".yml"));
        YamlConfiguration yaml = Files.loadYaml(file);
        yaml.set(id + ".display", display);
        yaml.set(id + ".label", label);
        yaml.set(id + ".timeout", timeout);
        yaml.set(id + ".cooldown", cooldown);
        if (!bookTag.isEmpty()) {
            yaml.set(id + ".booktag", bookTag);
        }
        if (!actionAccept.isEmpty()) {
            yaml.set(id + ".action.accept", actionAccept);
        }
        if (!actionAcceptFail.isEmpty()) {
            yaml.set(id + ".action.accept-fail", actionAcceptFail);
        }
        if (!actionSuccess.isEmpty()) {
            yaml.set(id + ".action.success", actionSuccess);
        }
        if (!actionFailure.isEmpty()) {
            yaml.set(id + ".action.failure", actionFailure);
        }
        if (!actionCooldown.isEmpty()) {
            yaml.set(id + ".action.cooldown", actionCooldown);
        }
        ConfigurationSection section = yaml.contains(id + ".condition") ? yaml.getConfigurationSection(id + ".condition") : yaml.createSection(id + ".condition");
        if (conditionAccept != null) {
            conditionAccept.save(section, "accept");
        }
        if (conditionFailure != null) {
            conditionFailure.save(section, "failure");
        }
        if (stageList != null) {
            stageList.export(yaml.contains(id + ".stage") ? yaml.getConfigurationSection(id + ".stage") : yaml.createSection(id + ".stage"));
        }
        try {
            yaml.save(file);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void open(Player player) {
        if (stageList == null) {
            stageList = new BuilderStageList();
        }
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        player.openInventory(MenuBuilder.builder(Cronus.getInst())
                .title("任务构建 : " + id)
                .rows(6)
                .put('#', MaterialControl.BLACK_STAINED_GLASS_PANE.parseItem())
                .put('$', MaterialControl.BLUE_STAINED_GLASS_PANE.parseItem())
                .put('0', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务名称")
                        .lore("", (display == null ? "§f无" : "§f" + display))
                        .build())
                .put('1', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务类型")
                        .lore(toLore(bookTag))
                        .build())
                .put('2', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务标签")
                        .lore("", (label == null ? "§f无" : "§f" + label))
                        .build())
                .put('3', new ItemBuilder(MaterialControl.CLOCK.parseMaterial())
                        .name("§b任务冷却时间")
                        .lore("", "§f" + (cooldown == null ? "无" : displayCooldown()))
                        .build())
                .put('4', new ItemBuilder(MaterialControl.CLOCK.parseMaterial())
                        .name("§b任务超时时间")
                        .lore("", "§f" + (timeout == null ? "无" : displayTimeout()))
                        .build())
                .put('5', new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                        .name("§b任务接受动作")
                        .lore(toLore(actionAccept))
                        .build())
                .put('6', new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                        .name("§b任务接受动作 (条件不足)")
                        .lore(toLore(actionAcceptFail))
                        .build())
                .put('7', new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                        .name("§b任务完成动作")
                        .lore(toLore(actionSuccess))
                        .build())
                .put('8', new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                        .name("§b任务失败动作")
                        .lore(toLore(actionFailure))
                        .build())
                .put('9', new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                        .name("§b任务冷却动作")
                        .lore(toLore(actionCooldown))
                        .build())
                .put('A', new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .name("§b任务接受条件")
                        .lore(toLore(conditionAccept == null ? Lists.newArrayList() : conditionAccept.asList(0)))
                        .build())
                .put('B', new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .name("§b任务失败条件")
                        .lore(toLore(conditionFailure == null ? Lists.newArrayList() : conditionFailure.asList(0)))
                        .build())
                .put('C', new ItemBuilder(Material.BOOK)
                        .name("§b任务阶段")
                        .lore(toLore(stageList.getStages().stream().map(BuilderStage::getId).collect(Collectors.toList())))
                        .build())
                .put('%', new ItemBuilder(MaterialControl.WRITABLE_BOOK.parseMaterial())
                        .name("§a保存配置")
                        .lore("", "§7文件位置", "§8§nplugins/Cronus/quests/builder/" + id + ".yml")
                        .build())
                .items(
                        "#########",
                        "$01234  $",
                        "$56789  $",
                        "$AB     $",
                        "$   C   $",
                        "####%####")
                .event(e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                editString(e.getClicker(), "任务名称", display, r -> display = r);
                                break;
                            case 11:
                                new BuilderList("任务索引", bookTag).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getBookTag);
                                break;
                            case 12:
                                editString(e.getClicker(), "任务标签", label, r -> label = r);
                                break;
                            case 13:
                                editString(e.getClicker(), "任务冷却时间", cooldown, r -> cooldown = r);
                                normal(e.getClicker(), "可用：");
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("[number][time]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7延迟冷却",
                                                "§7任务完成或失败后一段时间内无法再次接受",
                                                "",
                                                "§7示范:",
                                                "§f1h §8(1小时后)",
                                                "§f1h30m §8(1小时30分钟后)",
                                                "",
                                                "§7单位:",
                                                "§fd §8(天)",
                                                "§fh §8(时)",
                                                "§fm §8(分)",
                                                "§fs §8(秒)"
                                        ))).clickSuggest("[number][time]").send(e.getClicker());
                                break;
                            case 14:
                                editString(e.getClicker(), "任务超时时间", timeout, r -> timeout = r);
                                normal(e.getClicker(), "可用：");
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("[number][time]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7延迟刷新",
                                                "§7任务接受起一段时间后超时",
                                                "",
                                                "§7示范:",
                                                "§f1h §8(1小时后)",
                                                "§f1h30m §8(1小时30分钟后)",
                                                "",
                                                "§7单位:",
                                                "§fd §8(天)",
                                                "§fh §8(时)",
                                                "§fm §8(分)",
                                                "§fs §8(秒)"
                                        ))).clickSuggest("[number][time]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("day:[hour]:[minute]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7每日刷新",
                                                "§7任务接受起每日固定时间超时",
                                                "",
                                                "§7示范:",
                                                "§fday:23:59 §8(每天 23时59分)",
                                                "§fday:00:00 §8(每天 00时00分)"
                                        ))).clickSuggest("day:[hour]:[minute]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("week:[day]:[hour]:[minute]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7每周刷新",
                                                "§7任务接受起每周固定日期超时",
                                                "",
                                                "§7示范:",
                                                "§fweek:0:23:59 §8(每周日 23时59分)",
                                                "§fweek:6:00:00 §8(每周六 00时00分)",
                                                "",
                                                "§7单位:",
                                                "§f0 §8(周日)",
                                                "§f1 §8(周一)",
                                                "§f2 §8(周二)",
                                                "§f3 §8(周三)",
                                                "§f4 §8(周四)",
                                                "§f5 §8(周五)",
                                                "§f6 §8(周六)"
                                        ))).clickSuggest("week:[day]:[hour]:[minute]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("month:[day]:[hour]:[minute]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7每月刷新",
                                                "§7任务接受起每月固定日期超时",
                                                "",
                                                "§7示范:",
                                                "§fmonth:1:23:59 §8(每月1日 23时59分)",
                                                "§fmonth:1:00:00 §8(每月1日 00时00分)"
                                        ))).clickSuggest("month:[day]:[hour]:[minute]").send(e.getClicker());
                                break;
                            case 19:
                                new BuilderListEffect("任务接受动作", actionAccept).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 20:
                                new BuilderListEffect("任务接受动作 (条件不足)", actionAcceptFail).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 21:
                                new BuilderListEffect("任务完成动作", actionSuccess).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 22:
                                new BuilderListEffect("任务失败动作", actionFailure).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 23:
                                new BuilderListEffect("任务冷却动作", actionCooldown).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 28: {
                                BuilderCondition condition = new BuilderCondition(conditionAccept, e.getClicker(), "任务接受条件");
                                condition.setCloseTask(c -> {
                                    conditionAccept = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 29: {
                                BuilderCondition condition = new BuilderCondition(conditionFailure, e.getClicker(), "任务失败条件");
                                condition.setCloseTask(c -> {
                                    conditionFailure = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 40:
                                stageList.open(e.getClicker(), 0, c -> open(e.getClicker()), Maps::newHashMap);
                                break;
                            case 49:
                                player.closeInventory();
                                normal(player, "正在导出...");
                                try {
                                    export();
                                    normal(player, "导出完成!");
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                    error(player, "导出失败: " + t.getMessage());
                                }
                                break;
                        }
                    }
                }).build());
    }

    protected String formatTime(String example) {
        return "§8" + example.replaceAll("[.:]", "§f$0§8").replaceAll("\\[(.+?)]", "§8[§7$1§8]§8");
    }

    protected String formatEffect(String example) {
        return "§7" + example.replaceAll("\\.", "§8$0§7").replaceAll("\\[(\\S+)]", "§e[§6$1§e]§7");
    }

    protected Map<String, String> getBookTag() {
        return Cronus.getCronusService().getRegisteredQuest().entrySet().stream().flatMap(entry -> entry.getValue().getBookTag().stream()).collect(Collectors.toMap(k -> k, k -> k, (a, b) -> b, Maps::newTreeMap));
    }

    protected Map<String, String> getEffect() {
        return ProgramLoader.getEffects().stream().collect(Collectors.toMap(effect -> effect.getClass().getSimpleName().substring("Effect".length()), e -> formatEffect(e.getExample()), (a, b) -> b, Maps::newTreeMap));
    }

    protected String displayCooldown() {
        return cooldown.equalsIgnoreCase("never") || cooldown.equals("-1") ? "永不" : getTimeDisplay(Utils.toTime(cooldown));
    }

    protected String displayTimeout() {
        Time parse = Time.parse(timeout);
        if (parse == null) {
            return "永不";
        } else if (parse.getType() == TimeType.DAY) {
            return "每天 " + parse.getHour() + "时" + parse.getMinute() + "分";
        } else if (parse.getType() == TimeType.WEEK) {
            return "每周" + getWeekDisplay(parse.getDay()) + " " + parse.getHour() + "时" + parse.getMinute() + "分";
        } else if (parse.getType() == TimeType.MONTH) {
            return "每月" + parse.getDay() + "日 " + parse.getHour() + "时" + parse.getMinute() + "分";
        } else {
            return getTimeDisplay(parse.getTime());
        }
    }

    protected String getTimeDisplay(long in) {
        Times t = new Times(in);
        String time = (t.getDays() > 0 ? t.getDays() + "天" : "") + (t.getHours() > 0 ? t.getHours() + "时" : "") + (t.getMinutes() > 0 ? t.getMinutes() + "分" : "") + (t.getSeconds() > 0 ? t.getSeconds() + "秒" : "");
        return time.isEmpty() ? "无" : time;
    }

    protected String getWeekDisplay(int day) {
        switch (day) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "八";
        }
    }

    protected List<String> toLore(List<String> list) {
        List<String> array = Lists.newArrayList("");
        array.addAll(list.isEmpty() ? Lists.newArrayList("§f无") : list.stream().map(k -> "§f" + k).collect(Collectors.toList()));
        boolean more = false;
        while (array.size() > 8) {
            array.remove(8);
            more = true;
        }
        if (more) {
            array.add("§f...");
        }
        return TLocale.Translate.setColored(array);
    }

    protected void editString(Player player, String display, String origin, EditTask edit) {
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                player.closeInventory();
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入新的" + display + ". ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(origin)).hoverText("§7点击").clickSuggest(Utils.NonNull(origin))
                        .send(player);
                return this;
            }

            @Override
            public boolean after(String s) {
                edit.run(s);
                open(player);
                return false;
            }

            @Override
            public void cancel() {
                open(player);
            }
        });
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public String getLabel() {
        return label;
    }

    public String getCooldown() {
        return cooldown;
    }

    public String getTimeout() {
        return timeout;
    }

    public List<String> getActionAccept() {
        return actionAccept;
    }

    public List<String> getActionAcceptFail() {
        return actionAcceptFail;
    }

    public List<String> getActionSuccess() {
        return actionSuccess;
    }

    public List<String> getActionFailure() {
        return actionFailure;
    }

    public List<String> getActionCooldown() {
        return actionCooldown;
    }

    public MatchEntry getConditionAccept() {
        return conditionAccept;
    }

    public MatchEntry getConditionFailure() {
        return conditionFailure;
    }

    public BuilderStageList getStageList() {
        return stageList;
    }

    public interface EditTask {

        void run(String in);
    }
}
