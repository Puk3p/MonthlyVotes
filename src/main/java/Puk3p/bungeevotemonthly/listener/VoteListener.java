package Puk3p.bungeevotemonthly.listener;

import Puk3p.bungeevotemonthly.manager.VoteManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;

public class VoteListener implements Listener {
    private final VoteManager voteManager;

    public VoteListener(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        String playerName = vote.getUsername();
        voteManager.addVote(playerName);
        System.out.println("Jucătorul " + playerName + " a votat!");
    }
}