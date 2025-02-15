package Puk3p.bungeevotemonthly.commands;

import Puk3p.bungeevotemonthly.manager.VoteManager;
import Puk3p.bungeevotemonthly.util.VoteData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import java.util.LinkedHashMap;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public class TopVotesCommand extends Command {
    private final VoteManager voteManager;
    private final Plugin plugin;

    public TopVotesCommand(VoteManager voteManager, Plugin plugin) {
        super("topvotes");
        this.voteManager = voteManager;
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.equals(plugin.getProxy().getConsole())) {
            sender.sendMessage(new TextComponent("§cAceastă comandă poate fi folosită doar din consola serverului!"));
            return;
        }

        Map<String, VoteData> sortedVotes = voteManager.getAllVotes()
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    VoteData vote1 = entry1.getValue();
                    VoteData vote2 = entry2.getValue();

                    int voteComparison = Integer.compare(vote2.getCount(), vote1.getCount());
                    if (voteComparison != 0) {
                        return voteComparison;
                    }

                    return vote1.getLastVoteTime().compareTo(vote2.getLastVoteTime());
                })
                .collect(LinkedHashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), Map::putAll);

        plugin.getLogger().info("===== [Top Voturi] =====");
        if (sortedVotes.isEmpty()) {
            plugin.getLogger().info("Niciun jucător nu a votat încă!");
        } else {
            int rank = 1;
            for (Map.Entry<String, VoteData> entry : sortedVotes.entrySet()) {
                plugin.getLogger().info("#" + rank + " " + entry.getKey() + " - " + entry.getValue().getCount() +
                        " voturi | Ultimul vot: " + entry.getValue().getLastVoteTime());
                rank++;
            }
        }
        plugin.getLogger().info("=======================");
    }


}