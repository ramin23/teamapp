package de.teamapp.training;

import de.teamapp.spiele.Spieler;

import java.util.List;

/**
 * Wochenvorschlag für einen Spieler.
 * basis = welche Messwerte zum Vorschlag geführt haben (FR-19).
 */
public class Trainingsvorschlag {
    private final Spieler spieler;
    private final List<Uebung> uebungen;
    private final String basis;
    private boolean freigegeben;

    public Trainingsvorschlag(Spieler spieler, List<Uebung> uebungen, String basis) {
        this.spieler = spieler; this.uebungen = uebungen; this.basis = basis;
    }

    public Spieler getSpieler() { return spieler; }
    public List<Uebung> getUebungen() { return uebungen; }
    public String getBasis() { return basis; }
    public boolean isFreigegeben() { return freigegeben; }
    void freigeben() { this.freigegeben = true; }
}
