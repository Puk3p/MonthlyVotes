package Puk3p.bungeevotemonthly.manager;

import Puk3p.bungeevotemonthly.util.VoteData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class VoteManager {
    private final Map<String, VoteData> voteMap = new HashMap<>();
    private static final String FILE_PATH = "plugins/BungeeVoteMonthly/votes.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String currentMonth;

    public VoteManager() {
        this.currentMonth = getCurrentMonth();
        loadVotes();
    }

    public void addVote(String playerName) {
        VoteData voteData = voteMap.getOrDefault(playerName, new VoteData(0, getCurrentTimestamp()));
        voteData.incrementVote();
        voteMap.put(playerName, voteData);
        saveVotes();
    }

    public int getVotes(String playerName) {
        return voteMap.getOrDefault(playerName, new VoteData(0, getCurrentTimestamp())).getCount();
    }

    public String getLastVoteTime(String playerName) {
        return voteMap.getOrDefault(playerName, new VoteData(0, getCurrentTimestamp())).getLastVoteTime();
    }

    public Map<String, VoteData> getAllVotes() {
        return new HashMap<>(voteMap);
    }

    public Map<String, VoteData> getTop3() {
        return voteMap.entrySet()
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
                .limit(3)
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }



    public void resetVotes() {
        voteMap.clear();
        saveVotes();
    }

    public void saveVotes() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();

            Map<String, Object> data = new HashMap<>();
            data.put("month", currentMonth);
            data.put("votes", voteMap);

            try (Writer writer = new FileWriter(FILE_PATH)) {
                gson.toJson(data, writer);
            }

            System.out.println("💾 Voturile au fost salvate corect în votes.json!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Eroare la salvarea voturilor!");
        }
    }

    public void loadVotes() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.out.println("⚠️ Fișierul votes.json nu există. Se va crea unul nou.");
                return;
            }

            try (Reader reader = new FileReader(FILE_PATH)) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> data = gson.fromJson(reader, type);

                if (data == null || !data.containsKey("month") || !data.containsKey("votes")) {
                    System.out.println("⚠️ Fișier JSON invalid. Se va genera unul nou.");
                    saveVotes();
                    return;
                }

                String savedMonth = (String) data.get("month");
                if (savedMonth.equals(currentMonth)) {
                    Type voteType = new TypeToken<Map<String, VoteData>>() {}.getType();
                    Map<String, VoteData> votes = gson.fromJson(gson.toJson(data.get("votes")), voteType);

                    if (votes != null) {
                        voteMap.putAll(votes);
                    }
                    System.out.println("✅ Voturile au fost încărcate cu succes.");
                } else {
                    System.out.println("🔄 Lună nouă detectată. Resetăm voturile!");
                    voteMap.clear();
                    saveVotes();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Eroare la încărcarea voturilor!");
        }
    }

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    private String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }
}
