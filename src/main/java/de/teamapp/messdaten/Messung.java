package de.teamapp.messdaten;

import de.teamapp.spiele.Spiel;
import de.teamapp.spiele.Spieler;

/** Messdaten der Weste für einen Spieler in einem Spiel. */
public record Messung(Spieler spieler, Spiel spiel,
                      double laufdistanzKm, int sprints,
                      double hoechstgeschwKmh, int herzfrequenzBpm) {}
