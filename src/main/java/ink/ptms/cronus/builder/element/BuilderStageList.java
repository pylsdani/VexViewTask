package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.inventory.CloseTask;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 23:09
 */
public class BuilderStageList extends BuilderList {

    private List<BuilderStage> stages = Lists.newArrayList();

    public BuilderStageList(ConfigurationSection section) {
        super("任务阶段", Lists.newArrayList());
        section.getKeys(false).forEach(key -> stages.add(new BuilderStage(section.getConfigurationSection(key))));
        init();
    }

    public BuilderStageList() {
        super("任务阶段", Lists.newArrayList());
        init();
    }

    public void init() {
        // 左移
        this.defaultMoveLeft = index -> {
            BuilderStage element = stages.remove(index);
            stages.add(index - 1, element);
        };
        // 右移
        this.defaultMoveRight = index -> {
            BuilderStage element = stages.remove(index);
            stages.add(index + 1, element);
        };
        // 删除
        this.defaultDelete = index -> {
            stages.remove(index);
            open(player, page, close);
        };
        // 修改
        this.defaultEdit = index -> {
            // 新增
            if (list.get(index).equals("$append")) {
                // 事件
                Catchers.call(player, new Catchers.Catcher() {
                    @Override
                    public Catchers.Catcher before() {
                        toggle = true;
                        player.closeInventory();
                        TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入新的任务阶段名称. ")
                                .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                                .send(player);
                        return this;
                    }

                    @Override
                    public boolean after(String s) {
                        if (list.contains(s)) {
                            error(player, "任务阶段名称重复.");
                            return true;
                        } else {
                            stages.add(new BuilderStage(s));
                            open(player, page, close);
                            return false;
                        }
                    }

                    @Override
                    public void cancel() {
                        open(player, page, close);
                    }
                });
            } else {
                toggle = true;
                stages.get(index).open(player, this);
            }
        };
    }

    @Override
    public void open(Player player, int page, CloseTask close, Candidate candidate) {
        this.list = Lists.newArrayList(stages.stream().map(BuilderStage::getId).collect(Collectors.toList()));
        this.listOrigin = Lists.newArrayList();
        super.open(player, page, close, candidate);
    }

    public void export(ConfigurationSection section) {
        stages.forEach(builderStage -> builderStage.export(section.contains(builderStage.getId()) ? section.getConfigurationSection(builderStage.getId()) : section.createSection(builderStage.getId())));
    }

    public List<BuilderStage> getStages() {
        return stages;
    }
}
