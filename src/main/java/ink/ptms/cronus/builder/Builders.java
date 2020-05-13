package ink.ptms.cronus.builder;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.TabooLibLoader;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.util.item.inventory.ClickTask;
import io.izzel.taboolib.util.item.inventory.CloseTask;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-18 20:09
 */
@TFunction(enable = "init")
public class Builders {

    private static List<TaskEntry> taskEntries = Lists.newArrayList();

    static void init() {
        TabooLibLoader.getPluginClasses(Cronus.getInst()).ifPresent(classes -> {
            for (Class pClass : classes) {
                // task
                if (pClass.isAnnotationPresent(Auto.class) && TaskEntry.class.isAssignableFrom(pClass)) {
                    try {
                        taskEntries.add((TaskEntry) pClass.newInstance());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    public static TaskEntry fromType(String in) {
        return taskEntries.stream().filter(taskEntry -> taskEntry.getKey().equals(in)).findFirst().orElse(null);
    }

    public static Inventory normal(String title, ClickTask click, CloseTask close) {
        return MenuBuilder.builder(Cronus.getInst())
                .title(title)
                .rows(6)
                .put('#', MaterialControl.BLACK_STAINED_GLASS_PANE.parseItem())
                .put('$', MaterialControl.BLUE_STAINED_GLASS_PANE.parseItem())
                .items(
                        "#########",
                        "$       $",
                        "$       $",
                        "$       $",
                        "$       $",
                        "#########")
                .event(click)
                .close(close)
                .build();
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static List<TaskEntry> getTaskEntries() {
        return taskEntries;
    }
}
