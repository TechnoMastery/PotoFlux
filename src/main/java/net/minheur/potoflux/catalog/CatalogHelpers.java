package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.CatalogTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CatalogHelpers {
    private static final String TAB_CONSTANT = "    ";

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

        // ----- extended panel -----
        // panel
        JPanel expandablePanel = new JPanel();
        expandablePanel.setLayout(new BoxLayout(expandablePanel, BoxLayout.Y_AXIS));
        expandablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        expandablePanel.setOpaque(false);
        expandablePanel.setVisible(false); // start hidden
        expandablePanel.add(Box.createVerticalStrut(20));

        // version list
        if (mod.versions.isEmpty()) {
            JLabel emptyVersions = new JLabel("This mod has no version !"); // TODO
            expandablePanel.add(emptyVersions);
        } else {
            JLabel versionsLabel = new JLabel("Mod versions:"); // TODO
            expandablePanel.add(versionsLabel);

            expandablePanel.add(Box.createVerticalStrut(10));

            for (Map.Entry<String, ModCatalog.ModVersion> v : mod.versions.entrySet()) {
                // define version
                String line = "- " + v.getKey() + " : " + v.getValue().fileName;
                String noFileLine = "- " + v.getKey();
                JLabel versionLabel = new JLabel(v.getValue().fileName == null ? noFileLine : line);
                expandablePanel.add(versionLabel);

                // show compatible potoflux versions
                String compat = TAB_CONSTANT + "Compatible with PotoFlux: " + v.getValue().getPtfVersions(); // TODO
                JLabel compatLabel = new JLabel(compat);
                if (!v.getValue().ptfVersions.isEmpty())
                    expandablePanel.add(compatLabel);
                else {
                    JLabel emptyCompat = new JLabel(TAB_CONSTANT + "No compatible PotoFlux versions !"); // TODO
                    emptyCompat.putClientProperty(
                            "FlatLaf.style",
                            "foreground: $Actions.Red"
                    );
                    expandablePanel.add(emptyCompat);
                }

                // --- status ---

                // Part 1 : Published / Not Published
                JLabel publishedLabel = new JLabel(TAB_CONSTANT + (v.getValue().isPublished ? "Published" : "Not Published")); // TODO
                publishedLabel.putClientProperty(
                        "FlatLaf.style",
                        v.getValue().isPublished
                                ? "foreground: $Actions.Green"
                                : "foreground: $Actions.Red"
                );
                expandablePanel.add(publishedLabel);

                // space
                expandablePanel.add(Box.createHorizontalStrut(10));

                // Part 2 : Compatible / Not Compatible
                JLabel compatCurrent = new JLabel(TAB_CONSTANT + (v.getValue().ptfVersions.contains(PotoFlux.getVersion()) ? "Compatible" : "Not Compatible")); // TODO
                compatCurrent.putClientProperty(
                        "FlatLaf.style",
                        v.getValue().isPublished
                                ? "foreground: $Actions.Green"
                                : "foreground: $Actions.Red"
                );
                expandablePanel.add(compatCurrent);

                expandablePanel.add(Box.createVerticalStrut(4));
            }
        }

        // textPanel.add(expandablePanel);

        card.add(textPanel, BorderLayout.CENTER);

        // click to expand
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                expandablePanel.setVisible(!expandablePanel.isVisible());
                card.revalidate();
                card.repaint();
            }
        });

        return card;
    }

    private static JLabel getStatusLabel(ModCatalog mod) {
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

    private static void parameterDlButton(JButton dlButton, ModCatalog mod) {
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

        CatalogTab catalogTab = ((CatalogTab) PotoFlux.app.getTabMap().get(Tabs.INSTANCE.CATALOG));

        if (lastest == null) {
            dlButton.setText("! Incompatible - View");
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> catalogTab.viewMod(mod));
            return;
        }

        if (lastest.getKey().equals(loadedModVersion)) {
            dlButton.setText("Installed - View");
            dlButton.setEnabled(true);
            dlButton.addActionListener(e -> catalogTab.viewMod(mod));
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
            dlButton.addActionListener(e -> catalogTab.viewMod(mod));
            return;
        }

        throw new IllegalStateException("'sortedLasted' is not valid !");
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
