package de.teamapp.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.teamapp.messdaten.Messung;
import de.teamapp.spiele.Spiel;
import de.teamapp.messdaten.MessdatenService;
import de.teamapp.spiele.SpielService;
import de.teamapp.ui.components.Card;
import de.teamapp.ui.components.DistanzBalken;
import de.teamapp.ui.components.RueckmeldungSummary;
import de.teamapp.ui.components.StatCard;
import de.teamapp.ui.layout.MainLayout;

import java.util.List;

/** Figma Frame 1: "Team Dashboard" (Trainer-Sicht). */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Team Dashboard")
public class TeamDashboardView extends VerticalLayout {

    private final SpielService spielService;
    private final MessdatenService messdatenService;

    private final StatCard laufdistanz = new StatCard("Laufdistanz", "–");
    private final StatCard sprints = new StatCard("Sprints", "–");
    private final StatCard herzfrequenz = new StatCard("Herzfrequenz", "–");
    private final VerticalLayout balken = new VerticalLayout();
    private final Span hinweis = new Span();
    private final RueckmeldungSummary rueckmeldungen = new RueckmeldungSummary();

    // Services werden von Spring per Konstruktor injiziert (lose Kopplung)
    public TeamDashboardView(SpielService spielService, MessdatenService messdatenService) {
        this.spielService = spielService;
        this.messdatenService = messdatenService;
        addClassName("screen");

        // 1) Spielauswahl: Dropdown + Pfeil-Button
        ComboBox<Spiel> spielAuswahl = new ComboBox<>();
        spielAuswahl.setItems(spielService.letzteSpiele());
        spielAuswahl.setItemLabelGenerator(s -> "Letztes Spiel - " + s.gegner());
        spielAuswahl.setWidthFull();
        Button weiter = new Button(VaadinIcon.ARROW_RIGHT.create());
        weiter.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        weiter.addClickListener(e -> Notification.show("Spieldetails folgen"));
        HorizontalLayout auswahlZeile = new HorizontalLayout(spielAuswahl, weiter);
        auswahlZeile.setWidthFull();
        auswahlZeile.setFlexGrow(1, spielAuswahl);

        // 2) Durchschnittswerte: drei StatCards nebeneinander
        HorizontalLayout kacheln = new HorizontalLayout(laufdistanz, sprints, herzfrequenz);
        kacheln.setWidthFull();
        kacheln.addClassName("stat-row");
        Card schnitt = new Card("Durchschnittswerte:");
        schnitt.add(kacheln);

        // 3) Laufdistanz pro Spieler: Balken-Liste
        Button uebersicht = new Button("Übersicht öffnen");
        uebersicht.addThemeVariants(ButtonVariant.LUMO_SMALL);
        balken.setPadding(false);
        balken.setSpacing(false);
        hinweis.addClassName("hinweis");
        Card distanz = new Card("Laufdistanz pro Spieler:").withAction(uebersicht);
        distanz.add(balken, hinweis);

        // 4) Rückmeldungen
        Card rueck = new Card("Rückmeldungen:");
        rueck.add(rueckmeldungen);

        // 5) Runder "+ Spiel anlegen"-Button unten
        Button spielAnlegen = new Button("Spiel anlegen", VaadinIcon.PLUS.create());
        spielAnlegen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        spielAnlegen.addClassName("fab");
        spielAnlegen.addClickListener(e -> Notification.show("Dialog 'Spiel anlegen' folgt"));
        Div fabZeile = new Div(spielAnlegen);
        fabZeile.addClassName("fab-row");

        add(auswahlZeile, schnitt, distanz, rueck, fabZeile);

        spielAuswahl.addValueChangeListener(e -> { if (e.getValue() != null) zeige(e.getValue()); });
        spielAuswahl.setValue(spielService.letztesSpiel());
    }

    /** Lädt alle Werte für das gewählte Spiel aus den Services. */
    private void zeige(Spiel spiel) {
        laufdistanz.setWert(String.format("%.0fkm", messdatenService.schnittLaufdistanz(spiel)));
        sprints.setWert(String.format("%.0f", messdatenService.schnittSprints(spiel)));
        herzfrequenz.setWert(String.format("%.0f", messdatenService.schnittHerzfrequenz(spiel)));

        List<Messung> m = messdatenService.messungen(spiel);
        double max = m.stream().mapToDouble(Messung::laufdistanzKm).max().orElse(1);
        balken.removeAll();
        m.forEach(x -> balken.add(new DistanzBalken(x.spieler().name(), x.laufdistanzKm(), max)));

        int ohne = MessdatenService.KADERGROESSE - m.size();
        hinweis.setText("Für " + ohne + " von " + MessdatenService.KADERGROESSE
                + " sind keine Messdaten verfügbar");

        rueckmeldungen.setRueckmeldung(spielService.rueckmeldungen(spiel));
    }
}
