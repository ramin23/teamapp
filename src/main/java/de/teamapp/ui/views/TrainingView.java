package de.teamapp.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.teamapp.spiele.SpielService;
import de.teamapp.spiele.Spieler;
import de.teamapp.training.Intensitaet;
import de.teamapp.training.Trainingsvorschlag;
import de.teamapp.training.TrainingsvorschlagService;
import de.teamapp.training.Uebung;
import de.teamapp.ui.components.Card;
import de.teamapp.ui.layout.MainLayout;

import java.util.ArrayList;
import java.util.List;

/** KI-Trainingsvorschlag für den Trainer (FR-16 bis FR-20). */
@Route(value = "training", layout = MainLayout.class)
@PageTitle("Trainingsvorschlag")
public class TrainingView extends VerticalLayout {

    private final TrainingsvorschlagService service;
    private final VerticalLayout ergebnis = new VerticalLayout();

    public TrainingView(TrainingsvorschlagService service, SpielService spielService) {
        this.service = service;
        addClassName("screen");

        ComboBox<Spieler> spielerAuswahl = new ComboBox<>("Spieler");
        spielerAuswahl.setItems(spielService.kader());
        spielerAuswahl.setItemLabelGenerator(Spieler::name);
        spielerAuswahl.setWidthFull();

        Button erstellen = new Button("Vorschlag erstellen", VaadinIcon.MAGIC.create());
        erstellen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        erstellen.setEnabled(false);
        spielerAuswahl.addValueChangeListener(e -> erstellen.setEnabled(e.getValue() != null));
        erstellen.addClickListener(e -> zeige(spielerAuswahl.getValue()));

        erstellen.setWidthFull();

        ergebnis.setPadding(false);
        add(spielerAuswahl, erstellen, ergebnis);
    }

    private void zeige(Spieler spieler) {
        ergebnis.removeAll();
        service.erstelle(spieler).ifPresentOrElse(
                v -> ergebnis.add(vorschlagCard(v)),
                () -> {   // FR-20
                    Card c = new Card("Zu wenige Messdaten");
                    Span s = new Span("Für " + spieler.name() + " gibt es weniger als "
                            + TrainingsvorschlagService.MIN_SPIELE + " Spiele mit Messdaten. "
                            + "Ein Vorschlag wäre nicht aussagekräftig.");
                    s.addClassName("hinweis");
                    c.add(s);
                    ergebnis.add(c);
                });
    }

    private Card vorschlagCard(Trainingsvorschlag v) {
        Span status = new Span("Entwurf – Spieler sieht ihn noch nicht");
        status.getElement().getThemeList().add("badge contrast");

        Card card = new Card("Woche für " + v.getSpieler().name());

        Span basis = new Span(v.getBasis());   // FR-19
        basis.addClassName("hinweis");

        // Pro Übung eine Zeile: Tag + Name, darunter Dauer und Intensität (FR-18: vor Freigabe änderbar)
        VerticalLayout liste = new VerticalLayout();
        liste.setPadding(false);
        liste.setSpacing(false);
        liste.addClassName("uebung-liste");
        List<IntegerField> dauerFelder = new ArrayList<>();
        List<Select<Intensitaet>> intensitaetFelder = new ArrayList<>();
        for (Uebung u : v.getUebungen()) {
            Span tag = new Span(u.getTag());
            tag.addClassName("uebung-tag");
            Span name = new Span(u.getName());
            name.addClassName("uebung-name");

            IntegerField dauer = new IntegerField();
            dauer.setValue(u.getDauerMin());
            dauer.setSuffixComponent(new Span("min"));
            dauer.setMin(5);
            dauer.setMax(120);
            dauer.setWidth("6em");
            dauer.addValueChangeListener(e -> { if (e.getValue() != null) u.setDauerMin(e.getValue()); });
            dauerFelder.add(dauer);

            Select<Intensitaet> intensitaet = new Select<>();
            intensitaet.setItems(Intensitaet.values());
            intensitaet.setValue(u.getIntensitaet());
            intensitaet.setWidth("8em");
            intensitaet.addValueChangeListener(e -> u.setIntensitaet(e.getValue()));
            intensitaetFelder.add(intensitaet);

            HorizontalLayout felder = new HorizontalLayout(dauer, intensitaet);
            felder.setSpacing(true);
            VerticalLayout text = new VerticalLayout(name, felder);
            text.setPadding(false);
            text.setSpacing(false);
            HorizontalLayout zeile = new HorizontalLayout(tag, text);
            zeile.addClassName("uebung-zeile");
            zeile.setWidthFull();
            liste.add(zeile);
        }

        Button freigeben = new Button("Freigeben", VaadinIcon.CHECK.create());
        freigeben.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        freigeben.setWidthFull();
        freigeben.addClickListener(e -> {   // FR-17
            service.freigeben(v);
            status.setText("Freigegeben – Spieler sieht den Plan");
            status.getElement().getThemeList().remove("contrast");
            status.getElement().getThemeList().add("success");
            freigeben.setEnabled(false);
            dauerFelder.forEach(f -> f.setReadOnly(true));
            intensitaetFelder.forEach(f -> f.setReadOnly(true));
            Notification.show("Vorschlag für " + v.getSpieler().name() + " freigegeben")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        card.add(status, basis, liste, freigeben);
        return card;
    }
}
