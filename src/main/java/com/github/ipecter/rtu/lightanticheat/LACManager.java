package com.github.ipecter.rtu.lightanticheat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LACManager {

    private Map<UUID, LACPlayer> playerMap = new ConcurrentHashMap<>();

    public void add(Player player) {
        final LACPlayer lacPlayer = new LACPlayer();
        playerMap.put(player.getUniqueId(), lacPlayer);
        lacPlayer.isGeyser = Bukkit.getPluginManager().isPluginEnabled("floodgate") && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        final long time = System.currentTimeMillis();
        lacPlayer.joinTime = time;
        lacPlayer.lastGroundTime = time;
        lacPlayer.lastNonGroundTime = time;
        lacPlayer.lastHitTime = time;
        lacPlayer.isBypass = player.hasPermission("rtulac.bypass") && !Config.disableBypassPermission ||
                !Config.java && !lacPlayer.isGeyser || !Config.bedrock && lacPlayer.isGeyser;
    }

    public void remove(final Player player) {
        playerMap.remove(player);
    }

    public LACPlayer get(Player player) {
        return playerMap.get(player);
    }

    public void remove(final UUID uuid) {
        playerMap.remove(uuid);
    }

    public LACPlayer get(UUID uuid) {
        return playerMap.get(uuid);
    }

}
