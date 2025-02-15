package Puk3p.bungeevotemonthly.commands;

import Puk3p.bungeevotemonthly.discord.DiscordNotifier;
import Puk3p.bungeevotemonthly.manager.VoteManager;
import Puk3p.bungeevotemonthly.util.VoteData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public class SendVotesToDiscordCommand extends Command {
    private final VoteManager voteManager;
    private final DiscordNotifier discordNotifier;
    private final Plugin plugin;

    public SendVotesToDiscordCommand(VoteManager voteManager, DiscordNotifier discordNotifier, Plugin plugin) {
        super("sendvotes");
        this.voteManager = voteManager;
        this.discordNotifier = discordNotifier;
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.equals(plugin.getProxy().getConsole())) {
            sender.sendMessage(new TextComponent("§cAceastă comandă poate fi folosită doar din consola serverului!"));
            return;
        }

        Map<String, VoteData> votes = voteManager.getAllVotes();

        if (votes.isEmpty()) {
            sender.sendMessage(new TextComponent("§cNu există voturi înregistrate până acum!"));
        } else {
            discordNotifier.sendVotesEmbed(votes);
            sender.sendMessage(new TextComponent("§aLista votanților a fost trimisă pe Discord!"));
        }
    }
}