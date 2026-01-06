package net.minheur.potoflux.catalog;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class CatalogHelpers {
    private static final String TAB_CONSTANT = "    ";

    public static JPanel buildCard(ModCatalog mod) {
        JPanel card = new JPanel(new BorderLayout());
        // card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
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
        // mod name
        JLabel title = new JLabel(mod.modId);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        // Statut
        JLabel status = new JLabel(mod.isPublished ? "Published" : "Not Published"); // TODO
        status.putClientProperty(
                "FlatLaf.style",
                mod.isPublished
                        ? "foreground: $Actions.Green"
                        : "foreground: $Actions.Red"
        );

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(status);

        // ----- extended panel -----
        // panel
        JPanel expandablePanel = new JPanel();
        expandablePanel.setLayout(new BoxLayout(expandablePanel, BoxLayout.Y_AXIS));
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
                String compat = TAB_CONSTANT + "Compatible with PotoFlux: " + v.getValue().getPtfVersions();
                JLabel compatLabel = new JLabel(compat);
                expandablePanel.add(compatLabel);

                // --- status ---

                // Part 1 : Published / Not Published
                JLabel publishedLabel = new JLabel(TAB_CONSTANT + (v.getValue().isPublished ? "Published" : "Not Published"));
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
                JLabel compatCurrent = new JLabel(TAB_CONSTANT + (v.getValue().ptfVersions.contains(PotoFlux.getVersion()) ? "Compatible" : "Not Compatible"));
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

        textPanel.add(expandablePanel);

        card.add(textPanel, BorderLayout.WEST);

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
}

