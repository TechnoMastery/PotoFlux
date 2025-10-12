package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class CardLearningTab extends BaseTab {
    File appData = new File(System.getenv("APPDATA"), "PotoFlux");
    File cardsDir = new File(appData, "cards");

    @Override
    protected void setPanel() {
        PANEL.setLayout(new BorderLayout());

        checkAndCreateDir();

        // create sub-tab
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.addTab("Main", createMainPanel()); // TODO
        subTabs.addTab("List", createListPanel()); // TODO
        subTabs.addTab("Load", createLoadPanel()); // TODO
        subTabs.addTab("Create", createCreatePanel()); // TODO
        subTabs.addTab("Export", createExportPanel()); // TODO

        // add all to the main one
        PANEL.add(subTabs, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Zone d'apprentissage (Main)", SwingConstants.CENTER), BorderLayout.CENTER); // TODO
        return panel;
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Zone de list", SwingConstants.CENTER), BorderLayout.CENTER); // TODO
        return panel;
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Zone de export", SwingConstants.CENTER), BorderLayout.CENTER); // TODO
        return panel;
    }

    private JPanel createLoadPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton loadButton = new JButton("Importer un fichier JSON..."); // TODO

        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier JSON (*.json)", "json"); // TODO
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int result = chooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(panel, "Fichier chargé : " + chooser.getSelectedFile().getName()); // TODO
                // TODO: load & display cards
            }
        });
        panel.add(loadButton, BorderLayout.NORTH);
        return panel;
    }

    private void checkAndCreateDir() {
        if (!cardsDir.exists()) {
            boolean created = cardsDir.mkdirs();
            if (!created) {
                System.err.println("❌ Impossible de créer le dossier 'cards' : " + cardsDir.getAbsolutePath()); // TODO
            } else {
                System.out.println("✅ Dossier 'cards' créé : " + cardsDir.getAbsolutePath()); // TODO
            }
        }
    }

    private JPanel createCreatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Création de listes de cartes (Create)", SwingConstants.CENTER), BorderLayout.CENTER); // TODO
        return panel;
    }

    @Override
    protected boolean doPreset() {
        return false;
    }

    @Override
    protected String getTitle() {
        return "Card learning"; // TODO
    }
}
