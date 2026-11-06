package de.teamapp.training;

import de.teamapp.datenschutz.PseudonymisierteMessung;
import de.teamapp.datenschutz.PseudonymisierungService;
import de.teamapp.messdaten.MessdatenService;
import de.teamapp.messdaten.Messung;
import de.teamapp.spiele.Spieler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Komponente "KI-Trainingsvorschlag" (FR-16 bis FR-20).
 * Ablauf: Messdaten holen -> pseudonymisieren -> KI fragen -> Trainer gibt frei.
 */
@Service
public class TrainingsvorschlagService {

    public static final int MAX_SPIELE = 5;  // FR-16
    public static final int MIN_SPIELE = 3;  // FR-20

    private final MessdatenService messdaten;
    private final PseudonymisierungService datenschutz;
    private final KiClient ki;
    private final Map<Long, Trainingsvorschlag> vorschlaege = new HashMap<>();

    public TrainingsvorschlagService(MessdatenService messdaten, PseudonymisierungService datenschutz, KiClient ki) {
        this.messdaten = messdaten;
        this.datenschutz = datenschutz;
        this.ki = ki;
    }

    /** Leer, wenn es zu wenige Spiele gibt (FR-20). */
    public Optional<Trainingsvorschlag> erstelle(Spieler spieler) {
        List<Messung> letzte = messdaten.letzteMessungen(spieler, MAX_SPIELE);
        if (letzte.size() < MIN_SPIELE) {
            return Optional.empty();
        }
        List<PseudonymisierteMessung> anonym = datenschutz.pseudonymisieren(letzte); // NFR-04
        List<Uebung> plan = ki.wochenplan(anonym);
        String basis = String.format("Basis: %d Spiele · Ø %.1f km · Ø %.0f Sprints · Ø %.0f bpm",
                letzte.size(),
                letzte.stream().mapToDouble(Messung::laufdistanzKm).average().orElse(0),
                letzte.stream().mapToInt(Messung::sprints).average().orElse(0),
                letzte.stream().mapToInt(Messung::herzfrequenzBpm).average().orElse(0));
        Trainingsvorschlag v = new Trainingsvorschlag(spieler, plan, basis.replace('.', ','));
        vorschlaege.put(spieler.id(), v);
        return Optional.of(v);
    }

    /** FR-17: erst nach Freigabe sieht der Spieler den Vorschlag. */
    public void freigeben(Trainingsvorschlag v) {
        v.freigeben();
    }

    public Optional<Trainingsvorschlag> freigegebenFuer(Spieler spieler) {
        return Optional.ofNullable(vorschlaege.get(spieler.id())).filter(Trainingsvorschlag::isFreigegeben);
    }
}
