package Puk3p.bungeevotemonthly;

import Puk3p.bungeevotemonthly.commands.SendVotesToDiscordCommand;
import Puk3p.bungeevotemonthly.commands.TopVotesCommand;
import Puk3p.bungeevotemonthly.discord.DiscordNotifier;
import Puk3p.bungeevotemonthly.listener.VoteListener;
import Puk3p.bungeevotemonthly.manager.VoteManager;
import Puk3p.bungeevotemonthly.util.VoteData;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BungeeVoteMonthly extends Plugin {
    private VoteManager voteManager;
    private DiscordNotifier discordNotifier;

    @Override
    public void onEnable() {
        voteManager = new VoteManager();
        getProxy().getPluginManager().registerListener(this, new VoteListener(voteManager));

        try {
            String botToken = System.getenv("DISCORD_PLAY_VOTE_TOKEN");
            String channelId = "457051154971492370";

            if (botToken == null || botToken.isEmpty()) {
                throw new IllegalArgumentException("Token-ul Discord nu este setat!");
            }

            discordNotifier = new DiscordNotifier(botToken, channelId);
            getLogger().info("✅ Botul Discord a fost conectat cu succes!");
        } catch (LoginException | IllegalArgumentException e) {
            getLogger().severe("❌ Eroare la conectarea cu Discord: " + e.getMessage());
            discordNotifier = null;
        }

        getProxy().getPluginManager().registerCommand(this, new TopVotesCommand(voteManager, this));

        if (discordNotifier != null) {
            getProxy().getPluginManager().registerCommand(this, new SendVotesToDiscordCommand(voteManager, discordNotifier, this));
        } else {
            getLogger().warning("⚠️ Comanda /sendvotes nu a fost înregistrată deoarece botul Discord nu s-a conectat!");
        }
        getProxy().getScheduler().schedule(this, this::resetMonthlyVotes, getDaysUntilNextMonth(), TimeUnit.DAYS);
        getLogger().info("✅ BungeeVoteMonthly a fost activat!");

        scheduleMonthlyReset();
    }

    private void resetMonthlyVotes() {
        getLogger().info("Voturile au fost resetate!");
    }

    private long getDaysUntilNextMonth() {
        LocalDate now = LocalDate.now();
        LocalDate nextMonth = now.plusMonths(1).withDayOfMonth(1);
        return ChronoUnit.DAYS.between(nextMonth, now);
    }

    @Override
    public void onDisable() {
        getLogger().info("❌ BungeeVoteMonthly a fost dezactivat!");
    }

    private void scheduleMonthlyReset() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextReset = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(0);

        long secondsUntilReset = ChronoUnit.SECONDS.between(now, nextReset);

        getProxy().getScheduler().schedule(this, () -> {
            if (discordNotifier != null) {
                Map<String, VoteData> topVotes = voteManager.getTop3();
                discordNotifier.sendVotesEmbed(topVotes);
                getLogger().info("✅ Topul votanților a fost trimis pe Discord!");
            } else {
                getLogger().warning("⚠️ Nu s-a putut trimite topul votanților pe Discord deoarece botul nu este activ!");
            }

            voteManager.resetVotes();
            getLogger().info("🔄 Voturile au fost resetate pentru o nouă lună!");

            scheduleMonthlyReset();
        }, secondsUntilReset, TimeUnit.SECONDS);
    }
}
