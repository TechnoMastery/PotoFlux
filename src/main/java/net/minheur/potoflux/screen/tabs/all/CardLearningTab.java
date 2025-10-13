package net.minheur.potoflux.screen.tabs.all;

import com.google.gson.Gson;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.card.Card;
import net.minheur.potoflux.card.CardList;
import net.minheur.potoflux.screen.tabs.BaseTab;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CardLearningTab extends BaseTab {
    public static final Path cardsDir = Paths.get(PotoFlux.getProgramDir().toString(), "cards");
    public final List<String> cardNames = new ArrayList<>();

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

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier JSON" + " (*.json)", "json"); // TODO
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int result = chooser.showOpenDialog(panel);
            if (result != JFileChooser.APPROVE_OPTION) return;

            // get file
            File selectedFile = chooser.getSelectedFile();
            // turn to path
            Path selectedPath = selectedFile.toPath();

            // show check : file loaded
            JOptionPane.showMessageDialog(
                    panel, "Fichier chargé : " + chooser.getSelectedFile().getName() + // TODO
                    "\nChemin : " + selectedPath); // TODO

            CardList list;

            try {
                // read content
                String content = Files.readString(selectedPath);

                // parse to JSON object
                Gson gson = new Gson();
                list = gson.fromJson(content, CardList.class);

                // check is everything right
                if (list == null || list.cards == null || getCheckedListName(list.name) == null) {
                    showCardError();
                    return;
                }
                else {
                    for (Card card : list.cards) if (card.main == null || card.secondary == null) {
                        showCardError();
                        return;
                    }
                    list.name = getValidatedListName(list.name);
                    JOptionPane.showMessageDialog(PANEL, "Liste chargé : " + list.name + // TODO
                            "\nNombre de cartes : " + list.cards.size()); // TODO
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PANEL, "Erreur du chargement du fichier JSON", // TODO
                        "Erreur", JOptionPane.ERROR_MESSAGE); // TODO
                return;
            }

            String fileName = list.name.replaceAll(" ", "_");

            // TODO: create a file in cardDir → theNameEnteredAndChecked.json
        });
        panel.add(loadButton, BorderLayout.NORTH);
        return panel;
    }

    @Override
    protected boolean invokeLater() {
        return true;
    }

    private String getCheckedListName(String s) {
        if (s == null) return null;
        if (s.isEmpty() || s.trim().isEmpty()) return null;

        String newString = getValidatedListName(s);

        if (newString.isEmpty() || newString.trim().isEmpty()) return null;
        return newString;
    }

    private String getValidatedListName(String s) {
        return s.replaceAll("[^a-zA-Z0-9 ]", "");
    }

    private void showCardError() {
        JOptionPane.showMessageDialog(PANEL, "Le fichier JSON n'est pas valide !", // TODO
                "Erreur", JOptionPane.ERROR_MESSAGE); // TODO
    }

    private void reloadAllCards() {} // TODO: reload cards

    private void checkAndCreateDir() {
        try {
            Files.createDirectories(cardsDir);
        } catch (IOException ignored) {}
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
