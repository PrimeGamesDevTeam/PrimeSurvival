package net.primegames;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.primegames.listener.SurvivalGroupListener;
import net.primegames.plugin.PrimePlugin;
import net.primegames.settings.Settings;
import net.primegames.settings.presets.ClassicSettings;
import org.bukkit.plugin.PluginManager;

public final class PrimeSurvival extends PrimePlugin {

    @Getter
    private static PrimeSurvival instance;
    @Getter
    private LuckPerms luckPerms;
    @Getter PrimeVote vote;
    @Getter
    Settings settings;

    @Override
    protected void onInternalLoad() {
        instance = this;
        settings = new ClassicSettings();
    }

    @Override
    protected void onInternalEnable() {
        saveDefaultConfig();
        luckPerms = LuckPermsProvider.get();
        vote = new PrimeVote(this, settings.getVoteReward(), settings.getVoteSites());
        registerEvents();
    }


    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new SurvivalGroupListener(), this);
    }
}
