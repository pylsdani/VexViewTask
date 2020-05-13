package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.builder.editor.EditorAPI;
import ink.ptms.cronus.builder.editor.data.PlayerData;
import ink.ptms.cronus.builder.editor.data.PlayerDataHandler;
import ink.ptms.cronus.builder.element.condition.MatchEntry;
import ink.ptms.cronus.internal.version.MaterialControl;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.inventory.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-18 23:09
 */
public class BuilderStage extends BuilderQuest {

    private List<String> actionRestart = Lists.newArrayList();
    private List<List<String>> content = Lists.newCopyOnWriteArrayList();
    private List<List<String>> contentCompeted = Lists.newCopyOnWriteArrayList();
    private List<String> contentGlobal = Lists.newArrayList();
    private MatchEntry conditionRestart;
    private Player player;
    private BuilderStageList list;
    private BuilderTaskList listTask;
    private boolean toggle;

    public BuilderStage(String id) {
        super(id);
    }

    public BuilderStage(ConfigurationSection section) {
        super(section.getName());
        if (section.contains("action.restart")) {
            actionRestart = section.getStringList("action.restart");
        }
        if (section.contains("action.success")) {
            actionSuccess = section.getStringList("action.success");
        }
        if (section.contains("content")) {
            ConfigurationSection content = section.getConfigurationSection("content");
            content.getKeys(false).forEach(index -> this.content.add(Lists.newCopyOnWriteArrayList(content.getStringList(index))));
        }
        if (section.contains("content-completed")) {
            ConfigurationSection content = section.getConfigurationSection("content-completed");
            content.getKeys(false).forEach(index -> this.contentCompeted.add(Lists.newCopyOnWriteArrayList(content.getStringList(index))));
        }
        if (section.contains("content-global")) {
            contentGlobal = section.getStringList("content-global");
        }
        if (section.contains("restart")) {
            conditionRestart = new MatchEntry(section.get("restart"));
        }
        if (section.contains("task")) {
            listTask = new BuilderTaskList(section.getConfigurationSection("task"));
        }
    }

    public void export(ConfigurationSection section) {
        if (!actionRestart.isEmpty()) {
            section.set("action.restart", actionRestart);
        }
        if (!actionSuccess.isEmpty()) {
            section.set("action.success", actionSuccess);
        }
        if (!content.isEmpty()) {
            int index = 0;
            for (List<String> content : content) {
                section.set("content.t" + index++, content);
            }
        }
        if (!contentCompeted.isEmpty()) {
            int index = 0;
            for (List<String> content : contentCompeted) {
                section.set("content-completed.t" + index++, content);
            }
        }
        if (!contentGlobal.isEmpty()) {
            section.set("content-global", contentGlobal);
        }
        if (conditionRestart != null) {
            conditionRestart.save(section, "restart");
        }
        if (listTask != null) {
            listTask.export(section.contains("task") ? section.getConfigurationSection("task") : section.createSection("task"));
        }
    }

    @Override
    public void open(Player player) {
        open(player, list);
    }

    public void open(Player player, BuilderStageList list) {
        if (listTask == null) {
            listTask = new BuilderTaskList();
        }
        this.toggle = false;
        this.player = player;
        this.list = list;
        Inventory inventory = Builders.normal("阶段编辑 : " + id,
                e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                toggle = true;
                                new BuilderListEffect("阶段开始动作", actionAccept).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 11:
                                toggle = true;
                                new BuilderListEffect("阶段完成动作", actionSuccess).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 12:
                                toggle = true;
                                new BuilderListEffect("阶段失败动作", actionFailure).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 13:
                                toggle = true;
                                new BuilderListEffect("阶段重置动作", actionRestart).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 19:
                                toggle = true;
                                new BuilderStageContent(player, "阶段笔记", content, this).open(0);
                                break;
                            case 20:
                                toggle = true;
                                new BuilderStageContent(player, "阶段笔记 (完成)", contentCompeted, this).open(0);
                                break;
                            case 21:
                                toggle = true;
                                player.closeInventory();
                                // 编辑模式
                                EditorAPI.openEdit(player, contentGlobal);
                                EditorAPI.eval(player, ":edit");
                                // 执行动作
                                PlayerData playerData = PlayerDataHandler.getPlayerData(player);
                                playerData.setSaveTask(() -> {
                                    contentGlobal.clear();
                                    contentGlobal.addAll(EditorAPI.getContent(player));
                                });
                                playerData.setCloseTask(() -> {
                                    open(player, list);
                                });
                                break;
                            case 28: {
                                toggle = true;
                                BuilderCondition condition = new BuilderCondition(conditionFailure, e.getClicker(), "阶段失败条件");
                                condition.setCloseTask(c -> {
                                    conditionFailure = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 40:
                                toggle = true;
                                listTask.open(e.getClicker(), 0, c -> open(e.getClicker()), Maps::newHashMap);
                                break;
                            case 49:
                                toggle = true;
                                list.open(player);
                                break;
                        }
                    }
                }, e -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            list.open(player);
                        }, 1);
                    }
                });
        inventory.setItem(10, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b阶段开始动作")
                .lore(toLore(actionAccept))
                .build());
        inventory.setItem(11, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b阶段完成动作")
                .lore(toLore(actionSuccess))
                .build());
        inventory.setItem(12, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b阶段失败动作")
                .lore(toLore(actionFailure))
                .build());
        inventory.setItem(13, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b阶段重置动作")
                .lore(toLore(actionRestart))
                .build());
        inventory.setItem(19, new ItemBuilder(MaterialControl.WRITABLE_BOOK.parseMaterial())
                .name("§b阶段笔记")
                .lore("", content.isEmpty() ? "§f无内容" : "§f...")
                .build());
        inventory.setItem(20, new ItemBuilder(MaterialControl.WRITABLE_BOOK.parseMaterial())
                .name("§b阶段笔记 (完成)")
                .lore("", contentCompeted.isEmpty() ? "§f无内容" : "§f...")
                .build());
        inventory.setItem(21, new ItemBuilder(MaterialControl.WRITABLE_BOOK.parseMaterial())
                .name("§b阶段笔记 (纵览)")
                .lore(toLore(contentGlobal))
                .build());
        inventory.setItem(28, new ItemBuilder(Material.TRIPWIRE_HOOK)
                .name("§b阶段重置条件")
                .lore(toLore(conditionRestart == null ? Lists.newArrayList() : conditionRestart.asList(0)))
                .build());
        inventory.setItem(40, new ItemBuilder(Material.PAPER)
                .name("§b阶段条目")
                .lore(toLore(listTask.getListOrigin()))
                .build());
        inventory.setItem(49, new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem())
                .name("§c上级目录")
                .lore("", "§7点击")
                .build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }

    public List<String> getActionRestart() {
        return actionRestart;
    }

    public List<List<String>> getContent() {
        return content;
    }

    public List<List<String>> getContentCompeted() {
        return contentCompeted;
    }

    public List<String> getContentGlobal() {
        return contentGlobal;
    }

    public MatchEntry getConditionRestart() {
        return conditionRestart;
    }

    public Player getPlayer() {
        return player;
    }

    public BuilderStageList getList() {
        return list;
    }

    public BuilderTaskList getListTask() {
        return listTask;
    }

    public boolean isToggle() {
        return toggle;
    }
}
