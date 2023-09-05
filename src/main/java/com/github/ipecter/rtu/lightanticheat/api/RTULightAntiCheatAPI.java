package com.github.ipecter.rtu.lightanticheat.api;

import com.github.ipecter.rtu.lightanticheat.LACPlayer;
import org.bukkit.entity.Player;

public class RTULightAntiCheatAPI {

    public static boolean isApiBypass(Player player) {
        LACPlayer lacPlayer = LACPlayer.get(player);
        if (lacPlayer != null)
            return lacPlayer.isApiBypass;
        return false;
    }

    public static void setApiBypass(Player player, boolean bypass) {
        LACPlayer lacPlayer = LACPlayer.get(player);
        if (lacPlayer != null)
            lacPlayer.isApiBypass = bypass;
    }

}
