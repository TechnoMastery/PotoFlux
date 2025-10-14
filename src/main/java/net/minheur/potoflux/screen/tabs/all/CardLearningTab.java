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

        // title
        JLabel title = new JLabel("Listes disponibles", SwingConstants.CENTER);
        title.setFont(new Font("Segeo UI", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        // content
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        File[] jsonFiles = cardsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            listPanel.add(new JLabel("Aucune liste de cartes trouvée.", SwingConstants.CENTER)); // TODO
        } else {
            for (File file : jsonFiles) {
                try {
                    // reading content
                    String content = Files.readString(file.toPath());
                    CardList list = new Gson().fromJson(content, CardList.class);
                    if (list == null || list.cards == null) continue;

                    // line for the corresponding list
                    JPanel row = new JPanel(new BorderLayout());
                    row.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                            BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));

                    // left text
                    JLabel label = new JLabel(list.name + " (" + list.cards.size() + " cartes)"); // TODO
                    label.setFont(new Font("Segeo UI", Font.PLAIN, 14));
                    row.add(label, BorderLayout.WEST);

                    // right button
                    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                    JButton deleteButton = new JButton("Supprimer");
                    JButton infoButton = new JButton("Infos");

                    buttons.add(deleteButton);
                    buttons.add(infoButton);

                    row.add(buttons, BorderLayout.EAST);

                    listPanel.add(row);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // scrollable
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Zone de export", SwingConstants.CENTER), BorderLayout.CENTER); // TODO
        return panel;
    }

    private JPanel createLoadPanel() {
        CardList[] list = new CardList[1];

        JPanel panel = new JPanel(new BorderLayout());

        JButton loadButton = new JButton("Importer un fichier JSON..."); // TODO
        JButton validateButton = new JButton("Valider"); // TODO
        validateButton.setEnabled(false);


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

            try {
                // read content
                String content = Files.readString(selectedPath);

                // parse to JSON object
                Gson gson = new Gson();
                list[0] = gson.fromJson(content, CardList.class);

                // check is everything right
                if (list[0] == null || list[0].cards == null || getCheckedListName(list[0].name) == null) {
                    showCardError();
                    return;
                }
                else {
                    for (Card card : list[0].cards) if (card.main == null || card.secondary == null) {
                        showCardError();
                        return;
                    }
                    list[0].name = getValidatedListName(list[0].name);
                    JOptionPane.showMessageDialog(PANEL, "Liste chargé : " + list[0].name + // TODO
                            "\nNombre de cartes : " + list[0].cards.size()); // TODO
                    validateButton.setEnabled(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PANEL, "Erreur du chargement du fichier JSON", // TODO
                        "Erreur", JOptionPane.ERROR_MESSAGE); // TODO
                return;
            }

            panel.add(createCardPanelAsScroll(list[0], false));
            panel.revalidate();
            panel.repaint();

        });

        validateButton.addActionListener(e -> {
            if (list[0] == null) {
                JOptionPane.showMessageDialog(PANEL,
                        "list vide mais bouton valide !", // TODO
                        "erreur liste", JOptionPane.ERROR_MESSAGE); // TODO
                list[0] = null;
                validateButton.setEnabled(false);
            }

            String fileName = list[0].name.replaceAll(" ", "_");
            Path outputFile = cardsDir.resolve(fileName + ".json");

            // cancel if already existing
            if (Files.exists(outputFile)) {
                JOptionPane.showMessageDialog(PANEL,
                        "Un fichier du meme nom existe deja.\nL'ajout à été annulé.", // TODO
                        "Fichier deja existant", // TODO
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Gson gson = new Gson();
                Files.writeString(outputFile, gson.toJson(list));
                JOptionPane.showMessageDialog(PANEL,
                        "Fichier enregistré.", // TODO
                        "Sauvegarde réussi", // TODO
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PANEL,
                        "Erreur lors de l'enregistrement du fichier : " + ex.getMessage(), // TODO
                        "Erreur", JOptionPane.ERROR_MESSAGE); // TODO
            }
        });

        // adding buttons to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(loadButton);
        buttonPanel.add(validateButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        return panel;
    }

    @Override
    protected boolean invokeLater() {
        return true;
    }

    private JPanel createCardPanel(CardList list, boolean randomized) {
        JPanel allCards = new JPanel();
        allCards.setLayout(new GridLayout(0, 1, 10, 10));
        allCards.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Card card : list.cards) {
            JPanel cardPanel = new JPanel(new GridLayout(1, 2, 5, 5));
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

            // TODO: add randomizer option
            JLabel left = new JLabel(card.main, SwingConstants.CENTER);
            JLabel right = new JLabel(card.secondary, SwingConstants.CENTER);

            left.setFont(new Font("Segoe UI", Font.BOLD, 14));
            right.setFont(new Font("Segoe UI", Font.BOLD, 14));

            cardPanel.add(left);
            cardPanel.add(right);
            allCards.add(cardPanel);
        }

        return allCards;
    }

    private JScrollPane createCardPanelAsScroll(CardList list, boolean randomized) {
        JPanel p = createCardPanel(list, randomized);
        JScrollPane scrollPane = new JScrollPane(p);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
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
