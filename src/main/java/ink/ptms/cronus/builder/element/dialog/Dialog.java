package ink.ptms.cronus.builder.element.dialog;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.builder.element.BuilderCondition;
import ink.ptms.cronus.builder.element.BuilderDialog;
import ink.ptms.cronus.builder.element.BuilderList;
import ink.ptms.cronus.builder.element.condition.MatchEntry;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.ClickType;
import io.izzel.taboolib.util.item.inventory.CloseTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author 坏黑
 * @Since 2019-06-28 21:38
 */
public class Dialog extends BuilderDialog {

    private List<String> text = Lists.newArrayList();
    private List<String> effect = Lists.newArrayList();
    private List<Dialog> reply = Lists.newArrayList();
    private MatchEntry condition;
    private Dialog conditionDialog;
    private Dialog dialog;
    private Dialog parent;
    private ItemStack item;
    private boolean inReply;
    private boolean toggle;
    private CloseTask closeTask = e -> {
    };

    public Dialog(boolean inReply, Dialog parent) {
        this(inReply, false, parent);
    }

    public Dialog(boolean inReply, boolean inCondition, Dialog parent) {
        super("");
        this.inReply = inReply;
        this.parent = parent;
        this.item = new ItemStack(inReply ? Material.PAPER : Material.BOOK);
    }

    public void import0(Map<String, Object> map) {
        if (map.get("text") instanceof List) {
            text = (List) map.get("text");
        }
        if (map.get("reply") instanceof List) {
            ((List) map.get("reply")).forEach(r -> {
                Dialog reply = new Dialog(!inReply, this);
                reply.import0((Map) r);
                this.reply.add(reply);
            });
        }
        if (map.get("effect") instanceof List) {
            effect = (List) map.get("effect");
        }
        if (map.containsKey("dialog")) {
            dialog = new Dialog(!inReply, this);
            dialog.import0((Map) map.get("dialog"));
        }
        if (map.containsKey("condition")) {
            condition = new MatchEntry(map.get("condition"));
        }
        if (map.containsKey("condition-dialog")) {
            conditionDialog = new Dialog(true, true, this);
            conditionDialog.import0((Map) map.get("condition-dialog"));
        }
        if (map.containsKey("item")) {
            item = MaterialControl.fromString(map.get("item")).parseItem();
        }
    }

    public Map<String, Object> export0() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("text", text);
        map.put("item", Items.isNull(item) ? "air" : item.getType().name() + ":" + item.getDurability());
        if (!reply.isEmpty()) {
            map.put("reply", reply.stream().map(Dialog::export0).collect(Collectors.toList()));
        }
        if (!effect.isEmpty()) {
            map.put("effect", effect);
        }
        if (dialog != null) {
            map.put("dialog", dialog.export0());
        }
        if (condition != null) {
            map.put("condition", condition.toObject());
        }
        if (conditionDialog != null) {
            map.put("condition-dialog", conditionDialog.export0());
        }
        return map;
    }

    @Override
    public void open(Player player) {
        open(player, closeTask);
    }

    public void open(Player player, CloseTask closeTask) {
        this.toggle = true;
        this.closeTask = closeTask;
        Inventory inventory;
        inventory = Builders.normal("对话构建 : 结构编辑",
                e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                toggle = true;
                                new BuilderList("对话内容", text).open(e.getClicker(), 0, c -> open(e.getClicker()), Maps::newHashMap);
                                break;
                            case 11:
                                if (inReply && dialog == null) {
                                    toggle = true;
                                    new BuilderList("对话动作", effect).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                }
                                break;
                            case 12:
                                if (inReply && effect.isEmpty()) {
                                    if (e.castClick().isShiftClick()) {
                                        dialog = null;
                                        if (!Utils.isActionCooldown(player)) {
                                            open(player);
                                        }
                                    } else {
                                        if (dialog == null) {
                                            dialog = new Dialog(!inReply, this);
                                        }
                                        toggle = true;
                                        dialog.open(player, c -> open(player));
                                    }
                                }
                                break;
                            case 13:
                                if (!inReply) {
                                    toggle = true;
                                    new DialogReply(this, player).open(0);
                                }
                                break;
                            case 14:
                                if (inReply) {
                                    toggle = true;
                                    BuilderCondition condition = new BuilderCondition(this.condition, e.getClicker(), "特殊条件");
                                    condition.setCloseTask(c -> {
                                        this.condition = condition.getEntry();
                                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                    });
                                    condition.open(player, 0);
                                }
                                break;
                            case 15:
                                if (inReply) {
                                    if (e.castClick().isShiftClick()) {
                                        conditionDialog = null;
                                        if (!Utils.isActionCooldown(player)) {
                                            open(player);
                                        }
                                    } else {
                                        if (conditionDialog == null) {
                                            conditionDialog = new Dialog(true, true, this);
                                        }
                                        toggle = true;
                                        conditionDialog.open(player, c -> open(player));
                                    }
                                }
                                break;
                            case 16:
                                toggle = true;
                                if (e.castClick().isLeftClick()) {
                                    new DialogItem().open(player, this);
                                } else {
                                    item = new ItemStack(Material.AIR);
                                    open(player);
                                }
                                break;
                            case 49:
                                toggle = true;
                                closeTask.run(null);
                                break;
                        }
                    }
                }, e -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> closeTask.run(e), 1);
                    }
                });
        // 对话内容
        inventory.setItem(10, new ItemBuilder(Material.BOOK)
                .name("§b对话内容")
                .lore(toLore(text))
                .build());
        // 对话动作（无跳转）
        if (inReply && dialog == null) {
            inventory.setItem(11, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                    .name("§b对话动作")
                    .lore(toLore(effect))
                    .build());
        } else {
            inventory.setItem(11, new ItemBuilder(Material.BARRIER)
                    .name("§c禁用")
                    .lore("", "§4该设定无法使用.")
                    .build());
        }
        // 对话跳转（无动作）
        if (inReply && effect.isEmpty()) {
            inventory.setItem(12, new ItemBuilder(MaterialControl.MAP.parseMaterial())
                    .name("§b对话跳转")
                    .lore("", dialog == null ? "§f无" : "§f...", "§8§m                  ", "§7修改: §8点击", "§7删除: §8SHIFT+点击")
                    .build());
        } else {
            inventory.setItem(12, new ItemBuilder(Material.BARRIER)
                    .name("§c禁用")
                    .lore("", "§4该设定无法使用.")
                    .build());
        }
        // 对话回复
        if (!inReply) {
            inventory.setItem(13, new ItemBuilder(MaterialControl.PAPER.parseMaterial())
                    .name("§b对话回复")
                    .lore("", "§f共 " + reply.size() + " 项")
                    .build());
        } else {
            inventory.setItem(13, new ItemBuilder(Material.BARRIER)
                    .name("§c禁用")
                    .lore("", "§4该设定无法使用.")
                    .build());
        }
        // 特殊条件
        if (inReply) {
            inventory.setItem(14, new ItemBuilder(MaterialControl.TRIPWIRE_HOOK.parseMaterial())
                    .name("§b特殊条件")
                    .lore(toLore(condition == null ? Lists.newArrayList() : condition.asList(0)))
                    .build());
        } else {
            inventory.setItem(14, new ItemBuilder(Material.BARRIER)
                    .name("§c禁用")
                    .lore("", "§4该设定无法使用.")
                    .build());
        }
        // 特殊条件达成显示
        if (inReply) {
            inventory.setItem(15, new ItemBuilder(MaterialControl.TRIPWIRE_HOOK.parseMaterial())
                    .name("§b特殊条件达成显示")
                    .lore("", conditionDialog == null ? "§f无" : "§f...", "§8§m                  ", "§7修改: §8点击", "§7删除: §8SHIFT+点击")
                    .build());
        } else {
            inventory.setItem(15, new ItemBuilder(Material.BARRIER)
                    .name("§c禁用")
                    .lore("", "§4该设定无法使用.")
                    .build());
        }
        inventory.setItem(16, new ItemBuilder(Items.isNull(item) ? new ItemStack(Material.BARRIER) : item)
                .name("§b显示材质")
                .lore("", "§f" + (Items.isNull(item) ? "隐藏" : Items.getName(item)), "§8§m                  ", "§7修改: §8左键", "§7隐藏: §8右键")
                .build());
        inventory.setItem(49, new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem())
                .name("§c上级目录")
                .lore("", "§7点击")
                .build());
        inventory.setItem(40, getStructureItem());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        this.toggle = false;
    }

    public ItemStack getStructureItem() {
        Dialog top = this;
        while (top.parent != null) {
            top = top.parent;
        }
        return new ItemBuilder(Version.isAfter(Version.v1_9) ? Material.STRUCTURE_VOID : Material.NETHER_STAR).name("§b结构目录").lore(top.toStructure(this, 0)).build();
    }

    public List<String> toStructure(Dialog current, int index) {
        List<String> list = Lists.newArrayList();
        if (inReply) {
            list.add(toPrefix(index) + " §7回复 [§8" + toSimple(text.isEmpty() ? "-" : text.get(0)) + "§7] " + current(current));
            if (conditionDialog != null && condition != null) {
                list.add(toPrefix(index + 1) + " §7条件 [§8" + toSimple(condition.toSimple()) + "§7]");
                list.addAll(conditionDialog.toStructure(current, index + 2));
            }
            if (dialog != null) {
                list.addAll(dialog.toStructure(current, index + 1));
            }
            if (!effect.isEmpty()) {
                list.add(toPrefix(index + 1) + " §7动作 [§8" + toSimple(effect.isEmpty() ? "-" : effect.get(0)) + "§7]");
            }
        } else {
            list.add(toPrefix(index) + " §7对话 [§8" + toSimple(text.isEmpty() ? "-" : text.get(0)) + "§7] " + current(current));
            for (Dialog reply : reply) {
                list.addAll(reply.toStructure(current, index + 1));
            }
            return list;
        }
        return list;
    }

    public String toSimple(String in) {
        return Utils.toSimple(TLocale.Translate.setUncolored(TLocale.Translate.setColored(in)));
    }

    public String current(Dialog current) {
        return current == this ? " §f<-- 当前" : "";
    }

    public String toPrefix(int index) {
        return IntStream.range(0, index).mapToObj(i -> "§f  |").collect(Collectors.joining());
    }

    public List<String> getText() {
        return text;
    }

    public List<String> getDialogEffect() {
        return effect;
    }

    public List<Dialog> getReply() {
        return reply;
    }

    public MatchEntry getCondition() {
        return condition;
    }

    public Dialog getConditionDialog() {
        return conditionDialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isInReply() {
        return inReply;
    }

    public boolean isToggle() {
        return toggle;
    }

    public CloseTask getCloseTask() {
        return closeTask;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
