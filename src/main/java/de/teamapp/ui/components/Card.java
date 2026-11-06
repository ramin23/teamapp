package de.teamapp.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Umrandeter Kasten mit Überschrift und optionaler Aktion rechts oben.
 * Figma: "Durchschnittswerte", "Laufdistanz pro Spieler" + "Übersicht öffnen", "Letztes Spiel" + "bearb."
 */
public class Card extends VerticalLayout {

    private final HorizontalLayout header = new HorizontalLayout();

    public Card(String titel) {
        addClassName("card");
        setPadding(true);
        setSpacing(false);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        H3 h = new H3(titel);
        h.addClassName("card-title");
        header.add(h);
        add(header);
    }

    /** Fügt rechts im Kopf z.B. einen kleinen Button hinzu. */
    public Card withAction(Component action) {
        header.add(action);
        return this;
    }
}
