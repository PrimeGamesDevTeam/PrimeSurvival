package net.primegames;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.primegames.listener.MineTickerListener;
import net.primegames.listener.SurvivalGroupListener;
import net.primegames.plugin.PrimePlugin;
import org.bukkit.plugin.PluginManager;

public final class PrimeSurvival extends PrimePlugin {

    @Getter
    private static PrimeSurvival instance;
    @Getter
    private LuckPerms luckPerms;

    @Override
    protected void onInternalLoad() {
        instance = this;
    }

    @Override
    protected void onInternalEnable() {
        saveDefaultConfig();
        luckPerms = LuckPermsProvider.get();
        registerEvents();
    }


    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new MineTickerListener(), this);
        manager.registerEvents(new SurvivalGroupListener(), this);
    }
}
