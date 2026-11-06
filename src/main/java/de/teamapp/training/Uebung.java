package de.teamapp.training;

/** Eine Übung im Wochenplan. Veränderbar, damit der Trainer sie vor der Freigabe anpassen kann (FR-18). */
public class Uebung {
    private final String tag;
    private String name;
    private int dauerMin;
    private Intensitaet intensitaet;

    public Uebung(String tag, String name, int dauerMin, Intensitaet intensitaet) {
        this.tag = tag; this.name = name; this.dauerMin = dauerMin; this.intensitaet = intensitaet;
    }

    public String getTag() { return tag; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDauerMin() { return dauerMin; }
    public void setDauerMin(int dauerMin) { this.dauerMin = dauerMin; }
    public Intensitaet getIntensitaet() { return intensitaet; }
    public void setIntensitaet(Intensitaet intensitaet) { this.intensitaet = intensitaet; }
}
