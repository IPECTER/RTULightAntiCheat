package com.github.ipecter.rtu.lightanticheat;

import com.github.ipecter.rtu.lightanticheat.commands.Command;
import com.github.ipecter.rtu.lightanticheat.listeners.PlayerJoin;
import com.github.ipecter.rtu.lightanticheat.managers.ConfigManager;
import com.github.ipecter.rtu.pluginlib.RTUPluginLib;
import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RTULightAntiCheat extends JavaPlugin {

    private String prefix = IridiumColorAPI.process("<GRADIENT:ffffff>[ RTULightAntiCheat ]</GRADIENT:a3a3a3> ");

    @Override
    public void onEnable() {
        try {
            RTUPluginLib.init(this);
            Bukkit.getLogger().info(RTUPluginLib.getTextManager().formatted(prefix + "&aEnable&f!"));
            ConfigManager.getInstance().initConfigFiles();
            registerEvent();
            setExecutor();
            loadDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(RTUPluginLib.getTextManager().formatted(prefix + "&cDisable&f!"));
    }

    protected void registerEvent() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    protected void setExecutor() {
        getCommand("rtucc").setExecutor(new Command());
    }

    private void loadDependencies() {
        loadPAPI();
    }

    private void loadPAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            RTUPluginLib.getDependencyManager().setUsePAPI(true);
        }
    }
}
