package net.primegames;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.primegames.listener.MineTickerListener;
import net.primegames.listener.SurvivalGroupListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class JavaSurvival extends JavaPlugin {

    @Getter
    private static JavaSurvival instance;
    @Getter
    private LuckPerms luckPerms;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        luckPerms = LuckPermsProvider.get();

        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new MineTickerListener(), this);
        manager.registerEvents(new SurvivalGroupListener(), this);
    }
}
