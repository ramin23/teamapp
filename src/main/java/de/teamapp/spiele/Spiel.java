package de.teamapp.spiele;

import java.time.LocalDate;

public record Spiel(long id, String gegner, boolean auswaerts, LocalDate datum) {
    public String titel() {
        return (auswaerts ? "Auswärts bei " : "Heim gegen ") + gegner;
    }
}
