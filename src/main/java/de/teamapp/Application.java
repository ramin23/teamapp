package de.teamapp;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Startpunkt der Anwendung.
 * Lumo = Standard-Look von Vaadin, styles.css = eigene Farben/Abstände aus Figma
 * (liegt unter src/main/resources/META-INF/resources/styles.css).
 */
@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@StyleSheet("styles.css")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
