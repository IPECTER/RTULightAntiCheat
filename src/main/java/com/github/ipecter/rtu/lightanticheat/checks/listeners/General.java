package com.github.ipecter.rtu.lightanticheat.checks.listeners;

import com.github.ipecter.rtu.lightanticheat.LACPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class General implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        LACPlayerM.add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        LACPlayer.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        final LACPlayer lacPlayer = LACPlayer.get(event.getPlayer());
        if (lacPlayer == null)
            return;
        lacPlayer.lastClickTime = System.currentTimeMillis();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        final Entity caught = event.getCaught();
        if (caught == null || caught.getType() != EntityType.PLAYER)
            return;
        final LACPlayer lacPlayer = LACPlayer.get((Player) caught);
        if (lacPlayer == null)
            return;
        lacPlayer.lastPreventTime = System.currentTimeMillis();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        final PlayerTeleportEvent.TeleportCause cause = event.getCause();
        if (cause != PlayerTeleportEvent.TeleportCause.ENDER_PEARL &&
                cause != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT &&
                cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL &&
                cause != PlayerTeleportEvent.TeleportCause.END_PORTAL &&
                cause != PlayerTeleportEvent.TeleportCause.END_GATEWAY)
            return;
        final LACPlayer lacPlayer = LACPlayer.get(event.getPlayer());
        if (lacPlayer == null)
            return;
        lacPlayer.lastTeleportTime = System.currentTimeMillis();
    }

}
