package de.teamapp.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import de.teamapp.ui.views.SpielerView;
import de.teamapp.ui.views.TeamDashboardView;
import de.teamapp.ui.views.TrainingView;

/**
 * Gemeinsamer Rahmen aller Seiten (wie in beiden Figma-Frames):
 *  - oben: Titelleiste mit dem Namen der aktuellen Seite
 *  - Mitte: die jeweilige View
 *  - unten: Navigationsleiste Haus / Kalender / Person
 */
public class MainLayout extends Div implements RouterLayout {

    private final H1 titel = new H1();
    private final Div inhalt = new Div();

    public MainLayout() {
        addClassName("app-frame");
        titel.addClassName("view-title");
        inhalt.addClassName("app-content");

        Nav nav = new Nav(
                link(VaadinIcon.HOME, TeamDashboardView.class, "Dashboard"),
                link(VaadinIcon.LINE_CHART, TrainingView.class, "Training"),
                platzhalter(VaadinIcon.CALENDAR), // TODO: KalenderView bauen und hier link(...) verwenden
                link(VaadinIcon.USER, SpielerView.class, "Spieler"));
        nav.addClassName("bottom-nav");

        add(titel, inhalt, nav);
    }

    private static RouterLink link(VaadinIcon icon, Class<? extends Component> ziel, String label) {
        RouterLink link = new RouterLink();
        link.add(icon.create());
        link.setRoute(ziel);
        link.getElement().setAttribute("aria-label", label);
        return link;
    }

    private static Component platzhalter(VaadinIcon icon) {
        Div d = new Div(icon.create());
        d.addClassName("nav-platzhalter");
        return d;
    }

    /** Wird vom Router aufgerufen: View in die Mitte setzen und Titel aus @PageTitle übernehmen. */
    @Override
    public void showRouterLayoutContent(HasElement content) {
        inhalt.removeAll();
        inhalt.getElement().appendChild(content.getElement());
        PageTitle t = content.getClass().getAnnotation(PageTitle.class);
        titel.setText(t != null ? t.value() : "");
    }
}
