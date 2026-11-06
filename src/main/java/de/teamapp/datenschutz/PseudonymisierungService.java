package de.teamapp.datenschutz;

import de.teamapp.messdaten.Messung;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

/**
 * Komponente "Datenschutz": ersetzt Spieler durch ein Pseudonym.
 * Wird vom KI-Trainingsvorschlag und später von der Web-Ansicht benutzt.
 */
@Service
public class PseudonymisierungService {

    private static final String SALT = "teamapp-demo-salt"; // später aus Konfiguration

    public List<PseudonymisierteMessung> pseudonymisieren(List<Messung> messungen) {
        return messungen.stream()
                .map(m -> new PseudonymisierteMessung(pseudonym(m.spieler().id()),
                        m.laufdistanzKm(), m.sprints(), m.hoechstgeschwKmh(), m.herzfrequenzBpm()))
                .toList();
    }

    private String pseudonym(long spielerId) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest((SALT + spielerId).getBytes(StandardCharsets.UTF_8));
            return "P-" + HexFormat.of().formatHex(hash).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
