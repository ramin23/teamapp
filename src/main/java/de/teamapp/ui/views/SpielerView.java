package de.teamapp.ui.views;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.teamapp.messdaten.Messung;
import de.teamapp.spiele.Spiel;
import de.teamapp.messdaten.MessdatenService;
import de.teamapp.spiele.SpielService;
import de.teamapp.training.TrainingsvorschlagService;
import de.teamapp.ui.components.Card;
import de.teamapp.ui.components.RueckmeldungSummary;
import de.teamapp.ui.components.StatCard;
import de.teamapp.ui.layout.MainLayout;

/** Figma Frame 2: "Spieler" (Spieler-Sicht). */
@Route(value = "spieler", layout = MainLayout.class)
@PageTitle("Spieler")
public class SpielerView extends VerticalLayout {

    private final SpielService spielService;
    private final RueckmeldungSummary rueckmeldungen = new RueckmeldungSummary();

    public SpielerView(SpielService spielService, MessdatenService messdatenService,
                       TrainingsvorschlagService trainingService) {
        this.spielService = spielService;
        addClassName("screen");
        Spiel naechstes = spielService.letztesSpiel();

        // 1) Kopfzeile: Avatar | "Gerät verbunden: 82%" | Plus
        Avatar avatar = new Avatar("B");
        Span geraet = new Span("Gerät verbunden: " + messdatenService.akkuStandWeste() + "%");
        geraet.addClassName("geraet-status"); // Heuristik 1: Sichtbarkeit des Systemstatus
        Button plus = new Button(VaadinIcon.PLUS_CIRCLE_O.create());
        plus.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        plus.setAriaLabel("Gerät hinzufügen");
        HorizontalLayout kopf = new HorizontalLayout(avatar, geraet, plus);
        kopf.setWidthFull();
        kopf.setAlignItems(FlexComponent.Alignment.CENTER);
        kopf.setFlexGrow(1, geraet);

        // 2) Nächstes Spiel: Zusagen/Absagen + Zusammenfassung
        Button zusagen = new Button("Zusagen", e -> { spielService.zusagen(naechstes); aktualisiere(naechstes); });
        Button absagen = new Button("Absagen", e -> { spielService.absagen(naechstes); aktualisiere(naechstes); });
        zusagen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        absagen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttons = new HorizontalLayout(zusagen, absagen);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        Card spielCard = new Card(naechstes.gegner());
        spielCard.add(buttons, rueckmeldungen);

        // 3) Letztes Spiel: 2x2 Raster aus StatCards
        Messung m = messdatenService.eigeneMessung(naechstes);
        Button bearbeiten = new Button("bearb.");
        bearbeiten.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Span untertitel = new Span(spielService.letzteSpiele().get(1).titel());
        untertitel.addClassName("hinweis");
        Div raster = new Div(
                new StatCard("Laufdistanz", String.format("%.0f km", m.laufdistanzKm())),
                new StatCard("Sprints", String.valueOf(m.sprints())),
                new StatCard("Höchstgeschw.", String.format("%.1f km/h", m.hoechstgeschwKmh()).replace('.', ',')),
                new StatCard("Ø Herzfrequenz", m.herzfrequenzBpm() + " bpm"));
        raster.addClassName("stat-grid");
        Card letztes = new Card("Letztes Spiel").withAction(bearbeiten);
        letztes.add(untertitel, raster);

        // 4) Aktionen
        Button aufzeichnen = new Button("Spiel aufzeichnen", e -> Notification.show("Aufzeichnung gestartet"));
        Button uebertragen = new Button("Neuste Daten übertragen", e -> Notification.show("Daten übertragen"));
        HorizontalLayout aktionen = new HorizontalLayout(aufzeichnen, uebertragen);
        aktionen.setWidthFull();
        aktionen.addClassName("aktionen");
        aktionen.setFlexGrow(1, aufzeichnen, uebertragen);

        // 5) Trainingsplan – nur sichtbar, wenn der Trainer ihn freigegeben hat (FR-17)
        Card plan = new Card("Mein Trainingsplan");
        trainingService.freigegebenFuer(spielService.kader().get(0)).ifPresentOrElse(
                v -> v.getUebungen().forEach(u -> plan.add(new Span(
                        u.getTag() + ": " + u.getName() + " · " + u.getDauerMin() + " min · " + u.getIntensitaet()))),
                () -> {
                    Span s = new Span("Noch kein freigegebener Plan vom Trainer.");
                    s.addClassName("hinweis");
                    plan.add(s);
                });

        add(kopf, spielCard, letztes, aktionen, plan);
        aktualisiere(naechstes);
    }

    private void aktualisiere(Spiel spiel) {
        rueckmeldungen.setRueckmeldung(spielService.rueckmeldungen(spiel));
    }
}
