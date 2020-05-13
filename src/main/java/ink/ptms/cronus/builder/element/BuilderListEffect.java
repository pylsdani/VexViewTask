package ink.ptms.cronus.builder.element;

import ink.ptms.cronus.internal.program.effect.EffectNull;
import ink.ptms.cronus.internal.program.effect.EffectParser;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-21 9:39
 */
public class BuilderListEffect extends BuilderList {

    public BuilderListEffect(String display, List<String> list) {
        super(display, list);
    }

    @Override
    protected void editString(Player player, String display, String origin, EditTask edit, Candidate candidate) {
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                toggle = true;
                player.closeInventory();
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入新的" + display + ". ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(origin)).hoverText("§7点击").clickSuggest(Utils.NonNull(origin))
                        .send(player);
                if (candidate != null) {
                    Map<String, String> candidateMap = candidate.run();
                    if (!candidateMap.isEmpty()) {
                        TellrawJson json = TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7候选: §f");
                        int i = 1;
                        for (Map.Entry<String, String> entry : candidateMap.entrySet()) {
                            json.append("§f" + entry.getKey()).hoverText("§f" + entry.getValue()).clickSuggest(TLocale.Translate.setUncolored(entry.getValue()));
                            if (i++ < candidateMap.size()) {
                                json.append("§8, ");
                            }
                        }
                        json.send(player);
                    }
                }
                return this;
            }

            @Override
            public boolean after(String s) {
                if (EffectParser.parse(s) instanceof EffectNull) {
                    error(player, "动作格式错误.");
                    return true;
                }
                edit.run(s);
                open(player, page, close);
                return false;
            }

            @Override
            public void cancel() {
                open(player, page, close);
            }
        });
    }
}
