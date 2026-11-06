package de.teamapp.training;

import de.teamapp.datenschutz.PseudonymisierteMessung;

import java.util.List;

/**
 * Schnittstelle zum KI-Dienst (Infrastruktur).
 * Nur über dieses Interface wird die KI angesprochen -> Dienst austauschbar (NFR-06).
 * Bekommt ausschließlich pseudonymisierte Daten (NFR-04).
 */
public interface KiClient {
    List<Uebung> wochenplan(List<PseudonymisierteMessung> letzteSpiele);
}
