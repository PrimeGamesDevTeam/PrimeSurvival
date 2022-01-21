package net.primegames.settings.presets;

import net.kyori.adventure.text.Component;
import net.primegames.data.VoteReward;
import net.primegames.data.VoteSite;
import net.primegames.settings.Settings;
import org.bukkit.Bukkit;

import java.util.List;

public class ClassicSettings extends Settings {


    @Override
    public VoteReward getVoteReward() {
        return player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "votereward " + player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr give to " + player.getName() + " vote 1");
            Component component = Component.text("§a" + player.getName() + " §7has claimed their vote reward!");
            Bukkit.broadcast(component);
            return true;
        };
    }

    @Override
    public List<VoteSite> getVoteSites() {
        return List.of(
                VoteSite.Builder.create()
                        .name("Minecraft PE Server List")
                        .voteLink("vote.primegames.in")
                        .checkUrl("https://minecraftpocket-servers.com/api/?object=votes&element=claim&key={ServerKey}&username={Username}")
                        .claimUrl("https://minecraftpocket-servers.com/api/?action=post&object=votes&element=claim&key={ServerKey}&username={Username}")
                        .checkTopUrl("https://minecraftpocket-servers.com/api/?object=servers&element=voters&key={ServerKey}&month={Period}&format=json")
                        .apiKey("J6aZcNY4vZbCNf5LufxENIo6jEU5qZlSK2")
                        .build());
    }
}
