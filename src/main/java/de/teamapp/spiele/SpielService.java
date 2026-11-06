package de.teamapp.spiele;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Anwendungsschicht: Spiele und Rückmeldungen.
 * Später: Daten aus einem Repository/einer Datenbank statt fester Werte.
 */
@Service
public class SpielService {

    private final List<Spiel> spiele = List.of(
            new Spiel(1, "FC Beispielstadt", false, LocalDate.now().minusDays(3)),
            new Spiel(2, "SV Testspiel", true, LocalDate.now().minusDays(10)));

    private int zusagen = 14, absagen = 2, offen = 3;

    /** Kader des Teams (Testdaten). */
    public List<Spieler> kader() {
        return List.of(new Spieler(1, "Max M."), new Spieler(2, "Jonas K."), new Spieler(3, "Tim S."));
    }

    public List<Spiel> letzteSpiele() { return spiele; }

    public Spiel letztesSpiel() { return spiele.get(0); }

    public Rueckmeldung rueckmeldungen(Spiel spiel) {
        return new Rueckmeldung(zusagen, absagen, offen);
    }

    public void zusagen(Spiel spiel) { if (offen > 0) { offen--; zusagen++; } }

    public void absagen(Spiel spiel) { if (offen > 0) { offen--; absagen++; } }
}
