package Puk3p.bungeevotemonthly.util;

import Puk3p.bungeevotemonthly.manager.VoteManager;

public class VoteData {
    private int count;
    private String lastVoteTime;

    public VoteData(int count, String lastVoteTime) {
        this.count = count;
        this.lastVoteTime = lastVoteTime;
    }

    public int getCount() {
        return count;
    }

    public String getLastVoteTime() {
        return lastVoteTime;
    }

    public void incrementVote() {
        this.count++;
        this.lastVoteTime = VoteManager.getCurrentTimestamp();
    }
}
