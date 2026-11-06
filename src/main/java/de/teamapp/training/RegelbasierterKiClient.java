package de.teamapp.training;

import de.teamapp.datenschutz.PseudonymisierteMessung;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Platzhalter für den echten KI-Dienst (EU-Sprachmodell).
 * Erzeugt den Plan mit einfachen Regeln, damit der Prototyp ohne KI-Zugang läuft.
 * Später: neue Klasse z.B. MistralKiClient implements KiClient – sonst ändert sich nichts.
 */
@Component
public class RegelbasierterKiClient implements KiClient {

    @Override
    public List<Uebung> wochenplan(List<PseudonymisierteMessung> spiele) {
        double km = spiele.stream().mapToDouble(PseudonymisierteMessung::laufdistanzKm).average().orElse(0);
        double sprints = spiele.stream().mapToInt(PseudonymisierteMessung::sprints).average().orElse(0);
        boolean ausdauerSchwach = km < 10;
        boolean sprintSchwach = sprints < 20;
        return List.of(
                new Uebung("Mo", "Regeneration: lockerer Lauf", 30, Intensitaet.LEICHT),
                new Uebung("Di", ausdauerSchwach ? "Intervalllauf 6×4 min" : "Tempodauerlauf", ausdauerSchwach ? 45 : 35, Intensitaet.HOCH),
                new Uebung("Mi", "Kraft & Stabilität", 40, Intensitaet.MITTEL),
                new Uebung("Do", sprintSchwach ? "Sprints 10×30 m" : "Antritte mit Ball", 30, sprintSchwach ? Intensitaet.HOCH : Intensitaet.MITTEL),
                new Uebung("Fr", "Mobilität & Dehnen", 20, Intensitaet.LEICHT));
    }
}
