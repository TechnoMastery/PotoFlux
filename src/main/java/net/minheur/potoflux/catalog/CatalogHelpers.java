package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.PtfLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class CatalogHelpers {
    public static JPanel buildCard(ModCatalog mod) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);

        card.putClientProperty(
                "FlatLaf.style",
                "background: $Panel.background.lighten(4%); arc: 10"
        );

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UIManager.getColor("Component.borderColor")
                ),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // ----- Main text -----
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // mod name
        JLabel title = new JLabel(mod.modId);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        // dl button
        JButton dlButton = new JButton("Not available"); // TODO
        dlButton.setEnabled(false);

        parameterDlButton(dlButton, mod);

        mainPanel.add(title);
        mainPanel.add(Box.createHorizontalGlue());
        mainPanel.add(dlButton);

        mainPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                mainPanel.getPreferredSize().height
        ));

        // Statut
        JLabel status = getStatusLabel(mod);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.setOpaque(false);

        textPanel.add(mainPanel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(status);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    public static JLabel getStatusLabel(ModCatalog mod) {
        String statusContent = mod.isPublished ?
                (mod.isCompatible() ? "Compatible" : "Not Compatible") : // TODO
                "Not Published"; // TODO
        String statusColor = mod.isPublished ?
                (mod.isCompatible() ? "$Actions.Green"  : "#FFA500") :
                "$Actions.Red";

        JLabel status = new JLabel(statusContent);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        status.putClientProperty(
                "FlatLaf.style",
                "foreground: " + statusColor
        );
        return status;
    }

    private static String getInstallStatus(ModCatalog mod) {

        if (PotoFluxLoadingContext.isModListed(mod.modId)) { // mod listed

            String version = PotoFluxLoadingContext.getModVersion(mod.modId);

            // safety check
            if (version == null) throw new IllegalStateException("Listed mod is not listed?");

            if (PotoFluxLoadingContext.isModLoaded(mod.modId)) // mod loaded
                return "loaded";

            if (mod.isCompatible(version)) // compatible ?
                return "disabled"; // yes = disabled
            else return "dlIncompatible"; // no = dl but incompatible

        } else if (CatalogGetterHandler.isModKnown(mod.modId)) {
            if (mod.isCompatible())
                return "notI";
            else return "ndlIncompatible";
        }

        else return "unknown";

    }

    public static JLabel getInstallationStatus(ModCatalog mod) {
        // setup status
        String modInstallationStatus = getInstallStatus(mod);
        String statusColor;

        JLabel statusLabel = new JLabel();

        switch (modInstallationStatus) {
            case "loaded" -> {
                statusLabel.setText("Loaded"); // TODO
                statusColor = "$Actions.Green";
            }
            case "disabled" -> {
                statusLabel.setText("Disabled"); // TODO
                statusColor = "#FFA500";
            }
            case "dlIncompatible" -> {
                statusLabel.setText("Installed - Incompatible !"); // TODO
                statusColor = "$Actions.Red";
            }
            case "ndlIncompatible" -> {
                statusLabel.setText("Incompatible");
                statusColor = "$Actions.Red";
            }
            default -> {
                statusLabel.setText("Not installed"); // TODO
                statusColor = "$Actions.Red";
            }
        }

        statusLabel.putClientProperty(
                "FlatLaf.style",
                "foreground: " + statusColor
        );

        return statusLabel;
    }

    private static void parameterDlButton(JButton dlButton, ModCatalog mod) {
        dlButton.setEnabled(true);
        dlButton.addActionListener(e -> openModDesc(mod)); // TODO

        if (!mod.isPublished) return;

        Map.Entry<String, ModCatalog.ModVersion> lastest = mod.getLastestCompatibleVersion();

        if (!PotoFluxLoadingContext.isModListed(mod.modId)) {
            if (lastest == null) return;

            dlButton.setText("Download");
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> {
                try {
                    downloadMod(mod);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    PtfLogger.error("Failed to download mod !");
                }
            });
            return;
        }

        String loadedModVersion = PotoFluxLoadingContext.getModVersion(mod.modId);
        if (loadedModVersion == null) return;

        if (lastest == null) {
            dlButton.setText("! Incompatible - View");
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> openModDesc(mod));
            return;
        }

        if (lastest.getKey().equals(loadedModVersion)) {
            dlButton.setText("Installed - View");
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> openModDesc(mod));
            return;
        }

        String sortedLastest = Stream.of(lastest.getKey(), loadedModVersion)
                .max(CatalogHelpers::compareVersions)
                .orElse(null);

        if (sortedLastest.equals(loadedModVersion)) {
            dlButton.setText("Unknown version installed !");
            dlButton.setEnabled(false);
            return;
        }

        if (sortedLastest.equals(lastest.getKey())) {
            dlButton.setText("Installed - View / Update");
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> openModDesc(mod));
            return;
        }

        throw new IllegalStateException("'sortedLasted' is not valid !");
    }

    public static void openModDesc(ModCatalog mod) {
        // dialog
        JDialog dialog = new JDialog(
                ((JFrame) null),
                "Mod description", // TODO
                true
        );

        // pane
        JPanel modPanel = new JPanel();
        modPanel.setLayout(new BoxLayout(modPanel, BoxLayout.Y_AXIS));

        // line 1: modId
        JLabel modIdLabel = new JLabel(mod.modId);
        modIdLabel.setFont(modIdLabel.getFont().deriveFont(Font.BOLD, 28f));
        modIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modPanel.add(modIdLabel);

        modPanel.add(Box.createVerticalStrut(5));

        // line 2: main status
        JLabel statusLabel = getStatusLabel(mod);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modPanel.add(statusLabel);

        modPanel.add(Box.createVerticalStrut(5));

        // line 3: install status
        JLabel installStatusLabel = getInstallationStatus(mod);
        installStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modPanel.add(installStatusLabel);

        modPanel.add(Box.createVerticalStrut(10));

        // line 4: button panels
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // dl button
        JButton dlButton = new JButton("Unavailable");
        dlButton.setEnabled(false);

        // toggle button
        JButton toggleButton = new JButton("No installed");
        toggleButton.setEnabled(false);

        // def dl button state
        switch (getInstallStatus(mod)) {
            case "loaded" -> {
                dlButton.setText("Disable - next restart");
                dlButton.setEnabled(true);
            }
            case "disabled" -> {
                dlButton.setText("Enable - next restart");
                dlButton.setEnabled(true);
            }
            case "dlIncompatible" -> {
                if (mod.isCompatible()) {
                    dlButton.setText("INCOMPATIBLE - Download lastest compatible version");
                    dlButton.setEnabled(true);
                } else {
                    dlButton.setText("INCOMPATIBLE - Delete");
                    dlButton.setEnabled(true);
                }
            }
            case "notI" -> {
                dlButton.setText("Download");
                dlButton.setEnabled(true);
            }
            case "ndlIncompatible" -> dlButton.setText("Incompatible");
            default -> {
                dlButton.setText("Unknown ! Delete");
                dlButton.setEnabled(false);
            }
        }

        buttonsPanel.add(dlButton);

        // ok button
        JButton okButton = new JButton("Ok"); // TODO
        okButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(okButton);

        modPanel.add(buttonsPanel);

        // dialog defs
        dialog.setContentPane(modPanel);
        dialog.pack();
        Dimension size = dialog.getSize();
        dialog.setSize(size.width + 40, size.height);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void downloadMod(ModCatalog mod) throws IOException, InterruptedException {
        var lastest = mod.getLastestCompatibleVersion();

        if (lastest.getKey() == null || lastest.getValue() == null)
            throw new IllegalStateException("No compatible version found for mod: " + mod.modId);

        String fileName = lastest.getValue().fileName;
        String dlUrl = "https://technomastery.github.io/PotoFluxAppData/modCatalog/"
                + mod.modId + "/"
                + fileName;

        Path modsDir = PotoFlux.getProgramDir().resolve("mods");
        Files.createDirectories(modsDir);

        Path targetFile = modsDir.resolve(fileName);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dlUrl))
                .build();

        HttpResponse<Path> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofFile(targetFile)
        );

        if (response.statusCode() != 200) {
            Files.deleteIfExists(targetFile);
            throw new IOException("Failed to download mod (" + response.statusCode() + ")");
        }
    }

    public static int compareVersions(String v1, String v2) {
        String[] p1 = v1.split("\\.");
        String[] p2 = v2.split("\\.");

        int max = Math.max(p1.length, p2.length);

        for (int i = 0; i < max; i++) {
            int n1 = i < p1.length ? Integer.parseInt(p1[i]) : 0;
            int n2 = i < p2.length ? Integer.parseInt(p2[i]) : 0;

            if (n1 != n2) return Integer.compare(n1, n2);
        }

        return 0;
    }
}
