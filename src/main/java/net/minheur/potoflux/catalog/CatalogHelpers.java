package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

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

        mainPanel.add(title);
        mainPanel.add(Box.createHorizontalGlue());
        mainPanel.add(dlButton);

        mainPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                mainPanel.getPreferredSize().height
        ));

        // Statut
        JLabel status = new JLabel(mod.isCompatible() ? "Compatible" : "Not Compatible"); // TODO
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        status.putClientProperty(
                "FlatLaf.style",
                mod.isCompatible()
                        ? "foreground: $Actions.Green"
                        : "foreground: $Actions.Red"
        );

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
}
