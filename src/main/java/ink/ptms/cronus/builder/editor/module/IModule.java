package ink.ptms.cronus.builder.editor.module;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.ArrayUtil;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-03-11 18:00
 */
public abstract class IModule {

    public static final String[] TITLE = {
            "§7┎§8┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄§7┒",
            "§7┖§8┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄§7┚"
    };

    public String[] array(String... var) {
        return ArrayUtil.asArray(var);
    }

    abstract public String[] getCommand();

    abstract public void eval(Player sender, String body);

    public List<String> complete(Player sender, String body) {
        return Lists.newArrayList();
    }

}
