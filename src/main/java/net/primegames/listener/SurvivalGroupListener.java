package net.primegames.listener;

import net.primegames.core.event.player.CorePlayerLoadedEvent;
import net.primegames.survival.group.SurvivalGroup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class SurvivalGroupListener implements Listener {

    @EventHandler
    public void onGroupLoaded(CorePlayerLoadedEvent event) {
        SurvivalGroup.addGroupsFromTiers(event.getPlayer(), event.getPlayerData().getGroupTiers());
    }

}
