package com.github.ipecter.rtu.lightanticheat.listeners;

import com.github.ipecter.rtu.lightanticheat.LACManager;
import com.github.ipecter.rtu.lightanticheat.LACPlayer;
import com.github.ipecter.rtu.lightanticheat.managers.ConfigManager;
import com.github.ipecter.rtu.pluginlib.RTUPluginLib;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {

    private LACManager lacManager = LACManager.getInstance();
    private ConfigManager configManager = ConfigManager.getInstance();

    @EventHandler
    public void onJoinPrefix(PlayerJoinEvent e) {
        if (!configManager.isEnablePlugin()) return;
        Player player = e.getPlayer();
        if (configManager.isMotd()) {
            player.sendMessage(RTUPluginLib.getTextManager().formatted(player, configManager.getTranslation("prefix") + "&fRTU LightAntiCheat developed by IPECTER & Vekster (Original)"));
        } else {
            if (player.isOp())
                player.sendMessage(RTUPluginLib.getTextManager().formatted(player, configManager.getTranslation("prefix") + "&fRTU LightAntiCheat developed by IPECTER & Vekster (Original)"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        lacManager.add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        lacManager.remove(event.getPlayer().getUniqueId());
    }
}

