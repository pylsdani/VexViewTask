package ink.ptms.cronus.builder.editor.module;

import com.google.common.collect.Maps;
import ink.ptms.cronus.builder.editor.Editor;
import io.izzel.taboolib.TabooLibLoader;
import io.izzel.taboolib.module.inject.TSchedule;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-03-11 18:02
 */
public class IModuleHandler {

    private static Map<String, IModule> modules = Maps.newHashMap();

    @TSchedule
    static void load() {
        TabooLibLoader.getPluginClasses(Editor.getInst()).ifPresent(classes -> {
            for (Class pluginClass : classes) {
                if (IModule.class.isAssignableFrom(pluginClass) && !IModule.class.equals(pluginClass)) {
                    try {
                        IModule module = (IModule) pluginClass.newInstance();
                        Arrays.stream(module.getCommand()).forEach(command -> modules.put(command, module));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static Map<String, IModule> getModules() {
        return modules;
    }
}
