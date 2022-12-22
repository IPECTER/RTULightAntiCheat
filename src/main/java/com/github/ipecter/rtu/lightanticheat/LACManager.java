package com.github.ipecter.rtu.lightanticheat;

import com.github.ipecter.rtu.lightanticheat.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LACManager {

    private Map<UUID, LACPlayer> playerMap = new ConcurrentHashMap<>();
    private ConfigManager configManager = ConfigManager.getInstance();

    public void add(Player player) {
        final LACPlayer lacPlayer = new LACPlayer();
        playerMap.put(player.getUniqueId(), lacPlayer);
        lacPlayer.isGeyser = Bukkit.getPluginManager().isPluginEnabled("floodgate") && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        final long time = System.currentTimeMillis();
        lacPlayer.joinTime = time;
        lacPlayer.lastGroundTime = time;
        lacPlayer.lastNonGroundTime = time;
        lacPlayer.lastHitTime = time;
        lacPlayer.isBypass = player.hasPermission("rtulac.bypass") && !configManager.disableBypassPermission ||
                !configManager.java && !lacPlayer.isGeyser || !configManager.bedrock && lacPlayer.isGeyser;
    }

    public void remove(final UUID uuid) {
        playerMap.remove(uuid);
    }

    public LACPlayer get(UUID uuid) {
        return playerMap.get(uuid);
    }


    public final static LACManager getInstance() {
        return LACManager.InnerInstanceClass.instance;
    }

    private static class InnerInstanceClass {
        private static final LACManager instance = new LACManager();
    }

}
