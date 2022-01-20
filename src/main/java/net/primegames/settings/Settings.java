package net.primegames.settings;

import net.primegames.data.VoteReward;
import net.primegames.data.VoteSite;

import java.util.List;

public abstract class Settings {


    public abstract VoteReward getVoteReward();

    public abstract List<VoteSite> getVoteSites();
}
