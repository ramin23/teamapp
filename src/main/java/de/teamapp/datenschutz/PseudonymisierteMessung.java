package de.teamapp.datenschutz;

/** Messwerte ohne Namen/E-Mail – nur diese Form darf an den KI-Dienst (NFR-04). */
public record PseudonymisierteMessung(String pseudonym, double laufdistanzKm, int sprints,
                                      double hoechstgeschwKmh, int herzfrequenzBpm) {}
