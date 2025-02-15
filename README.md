# BungeeVoteMonthly

BungeeVoteMonthly este un plugin pentru **BungeeCord** care gestionează voturile lunare ale jucătorilor și trimite automat topul votanților pe Discord. Acesta oferă un sistem de resetare lunară a voturilor și comenzi pentru vizualizarea celor mai activi votanți.

## 🛠 Caracteristici
- 📌 Stocare și gestionare automată a voturilor jucătorilor
- 🏆 Generare top votanți și notificare automată pe Discord
- 🔁 Resetare automată lunară a voturilor
- 🎮 Comenzi pentru verificarea și trimiterea topului votanților
- ⚙️ Integrare cu **Votifier** pentru înregistrarea voturilor

## 📥 Instalare
1. **Descărcare**: Adaugă fișierul `.jar` al pluginului în folderul `plugins` al serverului **BungeeCord**.
2. **Configurare**:
   - Setează variabila de mediu `DISCORD_PLAY_VOTE_TOKEN` cu tokenul botului Discord.
3. **Pornire**: Repornește serverul pentru a activa pluginul.

## 🔧 Configurare
Acest plugin utilizează variabile de mediu pentru configurare:

| Variabilă            | Descriere                                          |
|----------------------|--------------------------------------------------|
| `DISCORD_PLAY_VOTE_TOKEN` | Token-ul botului Discord pentru notificări  |

## 📜 Comenzi disponibile
| Comandă             | Descriere                                         |
|---------------------|-------------------------------------------------|
| `/topvotes`        | Afișează topul votanților din consola serverului |
| `/sendvotes`       | Trimite topul votanților pe canalul de Discord  |

**Nota**: Comenzile pot fi utilizate doar din consola serverului.

## 🖧 Integrare cu Discord
BungeeVoteMonthly trimite topul votanților pe un canal Discord specificat. Acesta utilizează **JDA** pentru a interacționa cu API-ul Discord și creează un **embed personalizat** cu votanții de top.

## 🏅 Resetarea voturilor
Voturile sunt resetate automat **în ultima zi a fiecărei luni, la ora 23:59**. Resetarea este programată folosind **scheduler-ul BungeeCord**.

## 📌 Dependențe
- **BungeeCord**
- **Votifier** (pentru înregistrarea voturilor)
- **JDA** (pentru interacțiunea cu Discord)

## 📧 Suport
Dacă întâmpini probleme sau ai sugestii, contactează dezvoltatorul **Puk3p** pe Discord.

**Mulțumim că folosești BungeeVoteMonthly! 🎉**

