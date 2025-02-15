package Puk3p.bungeevotemonthly.discord;

import Puk3p.bungeevotemonthly.util.VoteData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscordNotifier {
    private final JDA jda;
    private final String channelId;
    private static final String iconURL= "https://cdn.discordapp.com/attachments/1147467499256938557/1322898549331071047/a_be31be745c02dab8a165276c44a58dc9.png?ex=67728cc8&is=67713b48&hm=c35c02d80aecd1e6c4215f9faf6ea0a0871bb41d7e0773009ff049c1638e0415&";


    public DiscordNotifier(String botToken, String channelId) throws LoginException {
        this.jda = JDABuilder.createDefault(botToken).build();
        this.jda.getPresence().setActivity(Activity.competing("Play.Mc-Ro.Ro!"));
        this.channelId = channelId;
    }

    public void sendVotesEmbed(Map<String, VoteData> votes) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            System.out.println("❌ Eroare: Canalul Discord nu a fost găsit!");
            return;
        }

        List<Map.Entry<String, VoteData>> sortedVotes = votes.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue().getCount(), a.getValue().getCount()))
                .toList();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🎉🏆 **TOP VOTANȚI - " + getCurrentMonth().toUpperCase() + "** 🏆🎉");
        embed.setColor(Color.GREEN);
        embed.setDescription("✨ Mulțumim tuturor celor care au votat! ✨\n");

        if (sortedVotes.isEmpty()) {
            embed.addField("❌ Niciun vot înregistrat", "Începe votarea acum! 🎉", false);
        } else {
            for (int i = 0; i < sortedVotes.size() && i < 3; i++) {
                String playerName = sortedVotes.get(i).getKey();
                VoteData voteData = sortedVotes.get(i).getValue();
                String placeEmoji = switch (i) {
                    case 0 -> "🥇";
                    case 1 -> "🥈";
                    case 2 -> "🥉";
                    default -> "🎖";
                };
                String prize = switch (i) {
                    case 0 -> "**💰 Premiu:** `20€` credit pe site-ul de donații!";
                    case 1 -> "**💰 Premiu:** `15€` credit pe site-ul de donații!";
                    case 2 -> "**💰 Premiu:** `10€` credit pe site-ul de donații!";
                    default -> "";
                };

                embed.addField(placeEmoji + " **LOCUL " + (i + 1) + "**",
                        "**👤 " + playerName + "**\n✅ `" + voteData.getCount() + " voturi`\n" + prize, false);
            }
        }

        embed.setThumbnail("https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExbDRmMzE1N3FqNm5sNndtaW1zOTBuamd3MHhrcGUxdTRheHc2cDFseiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/P4GxohAK1vaKI2yH8E/giphy.gif");
        embed.addField("\u200B", "\u200B", false);
        embed.addField("📢 **Câștigătorii sunt rugați să revendice premiul!**",
                "📩 Contactează `.callmegeorge` pe Discord\n🎟️ Sau creează un **ticket cu motivul `Întrebări`**.", false);

        embed.setFooter("© Play Squad | Îți mulțumim pentru dedicație!\n© Puk3p | .callmegeorge", iconURL);
        channel.sendMessageEmbeds(embed.build()).queue();
        channel.sendMessage("||@everyone||").queue();
    }





    private String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM", new Locale("ro")));
    }

}