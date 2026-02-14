package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.PtfLogger;

import javax.annotation.Nonnull;
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

/**
 * Helper class for the catalog
 */
public class CatalogHelpers {
    /**
     * Builds a listing card (as a JPanel) for a given mod
     * @param mod the mod to build the card with
     * @return the card panel
     */
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

        JPanel textPanel = getCardTextPanel();

        textPanel.add(mainPanel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(status);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    @Nonnull
    private static JPanel getCardTextPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.setOpaque(false);
        return textPanel;
    }

    /**
     * Maker for the mod status label.<br>
     * Says if published, compatible or incompatible.
     * @param mod mod to make the status
     * @return the status (JLabel)
     */
    public static JLabel getStatusLabel(ModCatalog mod) {
        String statusContent = mod.isPublished ?
                (mod.isCompatible() ? "Compatible" : "Not Compatible") : // TODO
                "Not Published"; // TODO
        String statusColor = mod.isPublished ?
                (mod.isCompatible() ? "$Actions.Green"  : "#FFA500") :
                "$Actions.Red";

        JLabel status = new JLabel(statusContent);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.putClientProperty(
                "FlatLaf.style",
                "foreground: " + statusColor
        );
        return status;
    }

    /**
     * Getter for the installation status.<br>
     * Says if the mod is installed, not installed or downloaded but incompatible or unknown (not in the online catalog)
     * @param mod to check the installation status
     * @return the installation status
     */
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

    /**
     * Maker for the installation status.<br>
     * Says if the mod is installed, not installed, downloaded andincompatible or unknown.
     * @param mod the mod to make the installation status from
     * @return the installation status (JLabel)
     */
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
                statusLabel.setText("Incompatible"); // TODO
                statusColor = "$Actions.Red";
            }
            default -> {
                statusLabel.setText("Not installed"); // TODO
                statusColor = "$Actions.Red";
            }
        }

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel.putClientProperty(
                "FlatLaf.style",
                "foreground: " + statusColor
        );

        return statusLabel;
    }

    /**
     * Parameter the download button depending on the mod
     * @param dlButton the button to parameter
     * @param mod mod to parameter the button with
     */
    private static void parameterDlButton(JButton dlButton, ModCatalog mod) {
        if (!mod.isPublished) return;

        Map.Entry<String, ModCatalog.ModVersion> lastest = mod.getLastestCompatibleVersion();

        if (!PotoFluxLoadingContext.isModListed(mod.modId)) {
            if (lastest == null) return;

            dlButton.setText("Download"); // TODO
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
            dlButton.setText("! Incompatible - View"); // TODO
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> openModDesc(mod));
            return;
        }

        if (lastest.getKey().equals(loadedModVersion)) {
            dlButton.setText("Installed - View"); // TODO
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> openModDesc(mod));
            return;
        }

        String sortedLastest = Stream.of(lastest.getKey(), loadedModVersion)
                .max(CatalogHelpers::compareVersions)
                .orElse(null);

        if (sortedLastest.equals(loadedModVersion)) {
            dlButton.setText("Unknown version installed !"); // TODO
            dlButton.setEnabled(false);
            return;
        }

        if (sortedLastest.equals(lastest.getKey())) {
            dlButton.setText("Installed - View / Update"); // TODO
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> openModDesc(mod));
            return;
        }

        throw new IllegalStateException("'sortedLasted' is not valid !");
    }

    /**
     * Opening the description of a mod
     * @param mod mod to open the description
     */
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

        fillModPanel(mod, modPanel);

        // line 4: button panels
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // dl button
        JButton dlButton = new JButton("Unavailable"); // TODO
        dlButton.setEnabled(false);
        // delete button
        JButton deleteButton = new JButton("Delete"); // TODO
        deleteButton.setEnabled(false);

        // toggle button
        JButton toggleButton = new JButton("Can't toggle - Not installed"); // TODO
        toggleButton.setEnabled(false);

        // def buttons state
        modDescButtonDefinition(mod, dlButton, toggleButton, deleteButton);

        buttonsPanel.add(dlButton);
        if (PotoFluxLoadingContext.isModListed(mod.modId)) buttonsPanel.add(deleteButton);
        buttonsPanel.add(toggleButton);

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

    private static void modDescButtonDefinition(ModCatalog mod, JButton dlButton, JButton toggleButton, JButton deleteButton) {
        if (PotoFluxLoadingContext.isModListed(mod.modId)) {

            // if listed
            performButtonDefListed(mod, dlButton, toggleButton, deleteButton);

            deleteButton.setEnabled(true);

        }
        // unlisted
        else performButtonDefUnlisted(mod, dlButton);
    }

    private static void performButtonDefUnlisted(ModCatalog mod, JButton dlButton) {
        if (mod.isCompatible()) {

            // if NOT listed & compatible

            dlButton.setText("Download"); // TODO
            dlButton.setEnabled(true);

        }
    }

    private static void performButtonDefListed(ModCatalog mod, JButton dlButton, JButton toggleButton, JButton deleteButton) {
        if (PotoFluxLoadingContext.isModLoaded(mod.modId)) {

            // if listed & loaded
            performButtonDefUpdate(mod, dlButton);

            toggleButton.setText("Toggle - Disable"); // TODO
            toggleButton.setEnabled(true);

            deleteButton.setText("Delete and disable"); // TODO

        }
        // listed & unloaded
        else performButtonDefUnloaded(mod, dlButton, toggleButton, deleteButton);
    }

    private static void performButtonDefUnloaded(ModCatalog mod, JButton dlButton, JButton toggleButton, JButton deleteButton) {
        if (mod.isCompatible(PotoFluxLoadingContext.getModVersion(mod.modId))) {

            performButtonDefUpdate(mod, dlButton);

            // if listed & NOT loaded & compatible

            toggleButton.setText("Toggle - Enable"); // TODO
            toggleButton.setEnabled(true);

            deleteButton.setText("Delete"); // TODO

        } else {

            if (mod.isCompatible()) {

                // if listed & NOT loaded & NOT compatible ; but has compatible version

                dlButton.setText("Incompatible ! Update to compatible"); // TODO
                dlButton.setEnabled(true);

                deleteButton.setText("Delete"); // TODO

            }
            else performButtonDefIncompatible(dlButton, deleteButton);

        }
    }

    private static void performButtonDefIncompatible(JButton dlButton, JButton deleteButton) {
        dlButton.setText("Incompatible"); // TODO
        deleteButton.setText("Delete incompatible mod"); // TODO
    }

    private static void performButtonDefUpdate(ModCatalog mod, JButton dlButton) {
        if (mod.isLastestCompatibleVersion(PotoFluxLoadingContext.getModVersion(mod.modId))) {

            // if listed & loaded ; is the lastest compatible version
            dlButton.setText("Lastest downloaded"); // TODO

        } else {

            // if listed & loaded ; is NOT the lastest compatible version
            dlButton.setText("Update to lastest"); // TODO
            dlButton.setEnabled(true);

        }
    }

    private static void fillModPanel(ModCatalog mod, JPanel modPanel) {
        // line 1: modId
        JLabel modIdLabel = getModIdLabel(mod);
        modPanel.add(modIdLabel);

        modPanel.add(Box.createVerticalStrut(5));

        // line 2: main status
        JLabel statusLabel = getStatusLabel(mod);
        modPanel.add(statusLabel);

        modPanel.add(Box.createVerticalStrut(5));

        // line 3: install status
        JLabel installStatusLabel = getInstallationStatus(mod);
        modPanel.add(installStatusLabel);

        modPanel.add(Box.createVerticalStrut(10));
    }

    @Nonnull
    private static JLabel getModIdLabel(ModCatalog mod) {
        JLabel modIdLabel = new JLabel(mod.modId);
        modIdLabel.setFont(modIdLabel.getFont().deriveFont(Font.BOLD, 28f));
        modIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return modIdLabel;
    }

    /**
     * Download a mod
     * @param mod mod to download
     * @throws IOException if could not install the mod on the drive
     * @throws InterruptedException if the connection got interrupted
     */
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

    /**
     * Utility method to get which version is the lastest
     * @param v1 first version to compare
     * @param v2 second version to compare
     * @return the compared output
     */
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
