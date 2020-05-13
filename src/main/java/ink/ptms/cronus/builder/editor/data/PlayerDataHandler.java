package ink.ptms.cronus.builder.editor.data;

import com.google.common.collect.Maps;
import io.izzel.taboolib.module.inject.PlayerContainer;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-03-11 18:35
 */
public class PlayerDataHandler {

    @PlayerContainer
    private static Map<String, PlayerData> playerData = Maps.newConcurrentMap();

    public static PlayerData getPlayerData(Player player) {
        return playerData.computeIfAbsent(player.getName(), n -> new PlayerData(player.getName()));
    }

    public static PlayerData newData(Player player) {
        playerData.remove(player.getName());
        return getPlayerData(player);
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static Map<String, PlayerData> getPlayerData() {
        return playerData;
    }
}
