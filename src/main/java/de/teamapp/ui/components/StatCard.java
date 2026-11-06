package de.teamapp.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

/**
 * Kleine Kennzahl-Kachel: Beschriftung oben, großer Wert darunter.
 * Figma: "Laufdistanz 10km", "Sprints 21", "Ø Herzfrequenz 164 bpm" ...
 */
public class StatCard extends Div {

    private final Span wert = new Span();

    public StatCard(String label, String wert) {
        addClassName("stat-card");
        Span labelSpan = new Span(label);
        labelSpan.addClassName("stat-label");
        this.wert.addClassName("stat-value");
        this.wert.setText(wert);
        add(labelSpan, this.wert);
    }

    public void setWert(String text) { wert.setText(text); }
}
