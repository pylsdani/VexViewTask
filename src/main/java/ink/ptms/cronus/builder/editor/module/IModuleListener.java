package ink.ptms.cronus.builder.editor.module;

import ink.ptms.cronus.builder.editor.EditorAPI;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.packet.TPacketListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-03-11 18:03
 */
@TListener
public class IModuleListener implements Listener {


    @TInject
    static TPacketListener listener = new TPacketListener() {
        @Override
        public boolean onReceive(Player player, Object packet) {
            if (packet.getClass().getSimpleName().equals("PacketPlayInChat")) {
                // 检查缓存
                SimpleReflection.checkAndSave(packet.getClass());
                // 编辑模式
                if (EditorAPI.isEditMode(player)) {
                    String message = String.valueOf(SimpleReflection.getFieldValue(packet.getClass(), packet, "a"));
                    EditorAPI.eval(player, message);
                    return message.startsWith("/");
                }
            }
            return true;
        }
    };

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (EditorAPI.isEditMode(e.getPlayer()) ) {
            e.setCancelled(true);
            EditorAPI.eval(e.getPlayer(), e.getMessage());
        }
    }

    @EventHandler
    public void onTab(PlayerChatTabCompleteEvent e) {
        if (EditorAPI.isEditMode(e.getPlayer())) {
            try {
                if (e.getChatMessage().startsWith(":")) {
                    e.getTabCompletions().clear();
                    if (e.getChatMessage().split(" ").length == 1 && !e.getChatMessage().endsWith(" ")) {
                        e.getTabCompletions().addAll(IModuleHandler.getModules().keySet().stream().filter(command -> command.toLowerCase().startsWith(e.getChatMessage().substring(1).toLowerCase())).map(command -> ":" + command).collect(Collectors.toList()));
                        return;
                    }
                    IModule module = IModuleHandler.getModules().get(e.getChatMessage().substring(1).split(" ")[0]);
                    if (module == null) {
                        e.getPlayer().sendMessage("§c[PurtmarsEditor] §7无效的命令.");
                    } else {
                        e.getTabCompletions().addAll(module.complete(e.getPlayer(), e.getChatMessage().substring(e.getChatMessage().split(" ")[0].length() + 1)));
                    }
                }
            } catch (Throwable ignored) {
            }
        }
    }
}
