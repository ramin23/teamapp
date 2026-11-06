package de.teamapp.ui.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

/** Eine Zeile "Max M. [=========] 12,4km" aus "Laufdistanz pro Spieler". */
public class DistanzBalken extends HorizontalLayout {

    public DistanzBalken(String name, double km, double maxKm) {
        addClassName("distanz-balken");
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        Span n = new Span(name);
        n.addClassName("distanz-name");

        ProgressBar bar = new ProgressBar(0, maxKm, km);
        bar.addClassName("distanz-bar");

        Span wert = new Span(String.format("%.1fkm", km).replace('.', ','));
        wert.addClassName("distanz-wert");

        add(n, bar, wert);
        setFlexGrow(1, bar);
    }
}
