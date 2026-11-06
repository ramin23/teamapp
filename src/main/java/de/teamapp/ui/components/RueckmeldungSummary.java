package de.teamapp.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.teamapp.spiele.Rueckmeldung;

/**
 * "14 Zusagen | 2 Absagen | 3 Offen" – kommt in BEIDEN Frames vor,
 * deshalb einmal als eigene Komponente gebaut und wiederverwendet.
 */
public class RueckmeldungSummary extends HorizontalLayout {

    private final Span zusagen = zahl(), absagen = zahl(), offen = zahl();

    public RueckmeldungSummary() {
        addClassName("rueckmeldung-summary");
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.AROUND);
        add(spalte(zusagen, "Zusagen"), spalte(absagen, "Absagen"), spalte(offen, "Offen"));
    }

    public void setRueckmeldung(Rueckmeldung r) {
        zusagen.setText(String.valueOf(r.zusagen()));
        absagen.setText(String.valueOf(r.absagen()));
        offen.setText(String.valueOf(r.offen()));
    }

    private static Span zahl() {
        Span s = new Span();
        s.addClassName("stat-value");
        return s;
    }

    private static Div spalte(Span zahl, String label) {
        Span l = new Span(label);
        l.addClassName("stat-label");
        Div d = new Div(zahl, l);
        d.addClassName("rueckmeldung-spalte");
        return d;
    }
}
