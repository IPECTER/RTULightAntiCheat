package com.github.ipecter.rtu.lightanticheat;

import com.github.ipecter.rtu.lightanticheat.commands.Command;
import com.github.ipecter.rtu.lightanticheat.listeners.General;
import com.github.ipecter.rtu.lightanticheat.listeners.PlayerJoin;
import com.github.ipecter.rtu.lightanticheat.managers.ConfigManager;
import com.github.ipecter.rtu.pluginlib.RTUPluginLib;
import com.github.ipecter.rtu.pluginlib.managers.TextManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RTULightAntiCheat extends JavaPlugin {

    public static final Component prefix = RTUPluginLib.getTextManager().colored("<gradient:cc1f1f:a3a3a3>[ RTUBloodEffect ]</gradient> ");
    private final TextManager textManager = RTUPluginLib.getTextManager();


    @Override
    public void onEnable() {
        try {
            RTUPluginLib.init(this);
            Bukkit.getLogger().info(textManager.toString(prefix.append(textManager.colored("<green>Enable</green>!"))));
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
        Bukkit.getLogger().info(textManager.toString(prefix.append(textManager.colored("<red>Disable</red>!"))));
    }

    private void registerEvent() {
        Bukkit.getPluginManager().registerEvents(new General(), this);
        Bukkit.getPluginManager().registerEvents(new Combat(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreaking(), this);
        Bukkit.getPluginManager().registerEvents(new Movement(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    private void loadDependencies() {
        loadPAPI();
    }

    private void setExecutor() {
        getCommand("rtube").setExecutor(new Command());
    }

    private void loadPAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            RTUPluginLib.getDependencyManager().setUsePAPI(true);
        }
    }
}
