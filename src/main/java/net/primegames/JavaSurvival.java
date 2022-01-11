package net.primegames;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.primegames.listener.MineTickerListener;
import net.primegames.listener.SurvivalGroupListener;
import net.primegames.plugin.PrimePlugin;
import net.primegames.server.GameMode;
import net.primegames.server.GameServerSettings;
import net.primegames.server.GameServerStatus;
import org.bukkit.plugin.PluginManager;

public final class JavaSurvival extends PrimePlugin {

    @Getter
    private static JavaSurvival instance;
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

    @Override
    public GameServerSettings getServerSettings() {
        return new GameServerSettings(getConfig().getString("server-name"), GameMode.SURVIVAL, GameServerStatus.ALPHA, getConfig().getString("server-ip"), getConfig().getString("server-icon"));
    }
}
