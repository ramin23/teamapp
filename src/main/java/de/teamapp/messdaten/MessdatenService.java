package de.teamapp.messdaten;

import de.teamapp.spiele.Spiel;
import de.teamapp.spiele.Spieler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Anwendungsschicht: Messdaten der Westen.
 * Später: Anbindung an die Westen-Schnittstelle (Bluetooth/Hersteller-API).
 */
@Service
public class MessdatenService {

    public static final int KADERGROESSE = 22;

    public List<Messung> messungen(Spiel spiel) {
        return List.of(
                new Messung(new Spieler(1, "Max M."), spiel, 12.4, 25, 31.2, 165),
                new Messung(new Spieler(2, "Jonas K."), spiel, 10.0, 21, 29.8, 160),
                new Messung(new Spieler(3, "Tim S."), spiel, 9.3, 17, 28.1, 158))
                .stream()
                .sorted(Comparator.comparingDouble(Messung::laufdistanzKm).reversed())
                .toList();
    }

    public double schnittLaufdistanz(Spiel spiel) {
        return messungen(spiel).stream().mapToDouble(Messung::laufdistanzKm).average().orElse(0);
    }

    public double schnittSprints(Spiel spiel) {
        return messungen(spiel).stream().mapToInt(Messung::sprints).average().orElse(0);
    }

    public double schnittHerzfrequenz(Spiel spiel) {
        return messungen(spiel).stream().mapToInt(Messung::herzfrequenzBpm).average().orElse(0);
    }

    /** Messung des eingeloggten Spielers (hier fest: Spieler 1). */
    public Messung eigeneMessung(Spiel spiel) {
        return new Messung(new Spieler(1, "Max M."), spiel, 2.0, 23, 31.2, 164);
    }

    public int akkuStandWeste() { return 82; }

    /**
     * Die letzten (bis zu) n Messungen eines Spielers, neueste zuerst.
     * Testdaten: Tim hat absichtlich nur 2 Spiele, um FR-20 ("Zu wenige Messdaten") zu zeigen.
     */
    public List<Messung> letzteMessungen(Spieler spieler, int n) {
        int anzahl = spieler.id() == 3 ? 2 : 6;
        List<Messung> liste = new ArrayList<>();
        for (int i = 0; i < anzahl; i++) {
            Spiel spiel = new Spiel(100 + i, "Gegner " + (i + 1), i % 2 == 1, LocalDate.now().minusWeeks(i + 1));
            double km = 8.5 + ((spieler.id() * 7 + i * 3) % 40) / 10.0;
            liste.add(new Messung(spieler, spiel, km, 15 + (int) ((spieler.id() * 5 + i * 2) % 12),
                    27.0 + (i % 4), 150 + (int) ((spieler.id() * 3 + i * 4) % 20)));
        }
        return liste.stream().limit(n).toList();
    }
}
