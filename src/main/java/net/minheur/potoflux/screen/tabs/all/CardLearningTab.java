package net.minheur.potoflux.screen.tabs.all;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.card.Card;
import net.minheur.potoflux.card.CardJsonManager;
import net.minheur.potoflux.card.CardList;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.utils.Translations;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.Functions.removeProhibitedChar;

public class CardLearningTab extends BaseTab {
    public static final Path cardsDir = Paths.get(PotoFlux.getProgramDir().toString(), "cards");

    // vars needed in different places
    private final JPanel listPanel = new JPanel();
    private final JComboBox<String> exportComboBox = new JComboBox<>();
    private final JComboBox<String> mainComboBox = new JComboBox<>();
    private final JComboBox<String> modifyComboBox = new JComboBox<>();
    private final List<JComboBox<String>> allComboBox = new ArrayList<>();

    @Override
    protected void setPanel() {
        PANEL.setLayout(new BorderLayout());

        allComboBox.add(exportComboBox);
        allComboBox.add(mainComboBox);
        allComboBox.add(modifyComboBox);

        checkAndCreateDir();

        // create sub-tab
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.addTab(Translations.get("tabs.card.tab_name.main"), createMainPanel());
        subTabs.addTab(Translations.get("tabs.card.tab_name.list"), createListPanel());
        subTabs.addTab(Translations.get("tabs.card.tab_name.load"), createLoadPanel());
        subTabs.addTab(Translations.get("tabs.card.tab_name.create"), createCreatePanel());
        subTabs.addTab(Translations.get("tabs.card.tab_name.export"), createExportPanel());

        // add all to the main one
        PANEL.add(subTabs, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // top - list selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel listLabel = new JLabel(Translations.get("tabs.card.list_column"));
        JButton startButton = new JButton(Translations.get("common.start"));

        refreshComboBox();

        topPanel.add(listLabel);
        topPanel.add(mainComboBox);
        topPanel.add(startButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // center - cards
        JPanel cardPanel = new JPanel(new BorderLayout());
        JLabel cardLabel = new JLabel("", SwingConstants.CENTER);
        cardLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        cardPanel.add(cardLabel, BorderLayout.CENTER);
        panel.add(cardPanel, BorderLayout.CENTER);

        // bottom - buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton backButton = new JButton(Translations.get("common.back")); // TODO
        JButton flipButton = new JButton(Translations.get("common.flip"));
        JButton nextButton = new JButton(Translations.get("common.next"));
        backButton.setEnabled(false);
        flipButton.setEnabled(false);
        nextButton.setEnabled(false);
        bottomPanel.add(backButton);
        bottomPanel.add(flipButton);
        bottomPanel.add(nextButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // quiz data
        final CardList[] currentList = new CardList[1];
        final int[] index = {0};

        startButton.addActionListener(e -> {
            String selected = (String) mainComboBox.getSelectedItem();
            if (selected == null || selected.equals(Translations.get("tabs.card.select_list"))) return;

            Path filePath = cardsDir.resolve(selected + ".json");
            if (!Files.exists(filePath)) {
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.file_not_found") + selected, Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String content = Files.readString(filePath);
                currentList[0] = CardJsonManager.fromJson(JsonParser.parseString(content).getAsJsonObject(), true);
                if (currentList[0] == null || currentList[0].cards == null || currentList[0].cards.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.invalid_list"), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.read_error") + ex.getMessage(), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // reset index
            index[0] = 0;
            cardLabel.setText(currentList[0].cards.get(index[0]).main);

            backButton.setEnabled(false);
            flipButton.setEnabled(true);
            nextButton.setEnabled(true);
        });

        flipButton.addActionListener(e -> {
            if (currentList[0] == null || currentList[0].cards.isEmpty()) return;

            Card card = currentList[0].cards.get(index[0]);

            if (cardLabel.getText().equals(card.main)) {
                cardLabel.setText(card.secondary);
            } else {
                cardLabel.setText(card.main);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentList[0] == null || currentList[0].cards.isEmpty()) return;

            int size = currentList[0].cards.size();

            if (index[0] == size - 1) {
                nextButton.setEnabled(false);
                backButton.setEnabled(true);
                return;
            }

            index[0]++;

            backButton.setEnabled(true);
            cardLabel.setText(currentList[0].cards.get(index[0]).main);

            if (index[0] == size - 1) nextButton.setEnabled(false);
        });

        backButton.addActionListener(e -> {
            if (currentList[0] == null || currentList[0].cards.isEmpty()) return;

            if (index[0] <= 0) return;

            index[0]--;

            cardLabel.setText(currentList[0].cards.get(index[0]).main);

            if (index[0] == 0) {
                backButton.setEnabled(false);
            }

            if (!nextButton.isEnabled()) {
                nextButton.setEnabled(true);
            }
        });

        return panel;
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // title
        JLabel title = new JLabel(Translations.get("tabs.card.available_lists"), SwingConstants.CENTER);
        title.setFont(new Font("Segeo UI", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        // content
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        loadListPanel();

        // scrollable
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadListPanel() {
        listPanel.removeAll();

        File[] jsonFiles = cardsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            listPanel.add(new JLabel(Translations.get("tabs.card.no_list_found"), SwingConstants.CENTER));
        } else {
            for (File file : jsonFiles) {
                try {
                    // reading content
                    String content = Files.readString(file.toPath());
                    CardList list = CardJsonManager.fromJson(JsonParser.parseString(content).getAsJsonObject(), false);
                    if (list == null || list.cards == null) continue;

                    // line for the corresponding list
                    JPanel row = new JPanel(new BorderLayout());
                    row.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                            BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));

                    // left - text
                    JLabel label = new JLabel(list.name + " (" + list.cards.size() + " " + Translations.get("common.cards") + ")");
                    label.setFont(new Font("Segeo UI", Font.PLAIN, 14));
                    row.add(label, BorderLayout.WEST);

                    // right - buttons
                    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                    JButton deleteButton = new JButton("Supprimer");
                    JButton infoButton = new JButton("Infos");

                    deleteButton.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(listPanel,
                                Translations.get("tabs.card.list_del_validate") + list.name + " ?",
                                Translations.get("tabs.card.list_del_conf"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                Files.deleteIfExists(file.toPath());
                                JOptionPane.showMessageDialog(listPanel,
                                        Translations.get("tabs.card.list_removed.start") + list.name + Translations.get("tabs.card.list_removed.end"),
                                        Translations.get("tabs.card.list_removed.name"),
                                        JOptionPane.INFORMATION_MESSAGE);

                                loadListPanel();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(listPanel,
                                        Translations.get("tabs.card.delete_error") + ex.getMessage(),
                                        Translations.get("common.error"),
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    infoButton.addActionListener(e -> {
                        // window for display
                        JDialog infoDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(PANEL), Translations.get("tabs.card.details") + list.name, true);
                        infoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        infoDialog.setLocationRelativeTo(PANEL);
                        infoDialog.setLayout(new BorderLayout());

                        // title
                        JLabel title = new JLabel(list.name + " (" + list.cards.size() + " " + Translations.get("common.cards") + ")", SwingConstants.CENTER);
                        title.setFont(new Font("Segeo UI", Font.BOLD, 16));
                        infoDialog.add(title, BorderLayout.NORTH);

                        // card
                        JScrollPane scrollPane = createCardPanelAsScroll(list);
                        infoDialog.add(scrollPane, BorderLayout.CENTER);

                        // close button
                        JButton closeButton = new JButton(Translations.get("common.close"));
                        closeButton.addActionListener(ev -> infoDialog.dispose());

                        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        bottomPanel.add(closeButton);
                        infoDialog.add(bottomPanel, BorderLayout.SOUTH);

                        infoDialog.pack();
                        infoDialog.setLocationRelativeTo(PANEL);
                        infoDialog.setVisible(true);
                    });

                    buttons.add(deleteButton);
                    buttons.add(infoButton);

                    row.add(buttons, BorderLayout.EAST);

                    listPanel.add(row);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        listPanel.revalidate();
        listPanel.repaint();

        refreshComboBox();
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // title
        JLabel title = new JLabel(Translations.get("tabs.card.export_list"), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        // up - select list 6 button
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        // list
        refreshComboBox(); // adding .json files
        // export button
        JButton exportButton = new JButton(Translations.get("common.export"));
        exportButton.setEnabled(false);

        exportButton.addActionListener(e -> {
            String selected = (String) exportComboBox.getSelectedItem();
            if (selected == null || selected.equals(Translations.get("tabs.card.select_list"))) return;

            Path sourcePath = cardsDir.resolve(selected + ".json");
            if (!Files.exists(sourcePath)) {
                JOptionPane.showMessageDialog(PANEL,
                         Translations.get("common.fileNotFound") + " : " + sourcePath,
                        Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(Translations.get("tabs.card.export") + selected);
            chooser.setSelectedFile(new File(selected + ".json"));

            // ONLY json
            FileNameExtensionFilter filter = new FileNameExtensionFilter(Translations.get("files.json"), "json");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int userSelection = chooser.showSaveDialog(PANEL);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            File destinationFile = chooser.getSelectedFile();
            if (!destinationFile.getName().toLowerCase().endsWith(".json")) {
                destinationFile = new File(destinationFile.getAbsolutePath() + ".json");
            }

            // check if existing
            if (destinationFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(PANEL,
                        Translations.get("tabs.card.replace.content"),
                        Translations.get("tabs.card.replace.name"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (overwrite != JOptionPane.YES_OPTION) return;
            }

            try {
                Files.copy(sourcePath, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(PANEL,
                        Translations.get("tabs.card.export_done") + "\n" + destinationFile.getAbsolutePath(),
                        Translations.get("tabs.card.export_done.name"), JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PANEL,
                        Translations.get("tabs.card.export.error") + ex.getMessage(),
                        Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
            }
        });

        topPanel.add(exportComboBox, BorderLayout.CENTER);
        topPanel.add(exportButton, BorderLayout.EAST);

        // center content
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        // display cards
        JPanel cardsPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        cardsPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(cardsPanel, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        // behaviour
        exportComboBox.addActionListener(e -> {
            String selected = (String) exportComboBox.getSelectedItem();

            // null check
            if (selected == null || selected.equals(Translations.get("tabs.card.select_list"))) {
                scrollPane.setViewportView(null);
                exportButton.setEnabled(false);
                return;
            }

            // existing file check
            Path filePath = cardsDir.resolve(selected + ".json");
            if (!Files.exists(filePath)) {
                scrollPane.setViewportView(new JLabel(Translations.get("tabs.card.file.not_found"), SwingConstants.CENTER));
                exportButton.setEnabled(false);
                return;
            }

            try {
                String content = Files.readString(filePath);
                CardList list = CardJsonManager.fromJson(JsonParser.parseString(content).getAsJsonObject(), false);

                // null check
                if (list == null || list.cards == null) {
                    scrollPane.setViewportView(new JLabel(Translations.get("tabs.card.error_loading_list"), SwingConstants.CENTER));
                    exportButton.setEnabled(false);
                    return;
                }

                JScrollPane cardsScroll = createCardPanelAsScroll(list);
                scrollPane.setViewportView(cardsScroll.getViewport().getView());
                exportButton.setEnabled(true); // export button is now available
            } catch (Exception ex) {
                ex.printStackTrace();
                scrollPane.setViewportView(new JLabel(Translations.get("tabs.card.error_reading_file"), SwingConstants.CENTER));
                exportButton.setEnabled(false);
            }
        });

        return panel;
    }

    private void refreshComboBox() {
        for (JComboBox<String> c : allComboBox) refreshComboBox(c);
    }

    private void refreshComboBox(JComboBox<String> box) {
        box.removeAllItems();
        box.addItem(Translations.get("tabs.card.select_list"));
        File[] jsonFiles = cardsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));
        // null check
        if (jsonFiles != null) {
            // add
            for (File file : jsonFiles) {
                String name = file.getName().replace(".json", "");
                box.addItem(name);
            }
        }
    }

    private JPanel createLoadPanel() {
        CardList[] list = new CardList[1];
        JScrollPane[] loadedListCards = new JScrollPane[1];

        JPanel panel = new JPanel(new BorderLayout());

        JButton loadButton = new JButton(Translations.get("file.json.import"));
        JButton validateButton = new JButton(Translations.get("common.validate"));
        validateButton.setEnabled(false);


        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(Translations.get("file.json"), "json");
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
                    panel, Translations.get("tabs.card.file_loaded") + chooser.getSelectedFile().getName() +
                    "\n" + Translations.get("common.path") + selectedPath);

            try {
                // read content
                String content = Files.readString(selectedPath);

                // parse to JSON object
                list[0] = CardJsonManager.fromJson(JsonParser.parseString(content).getAsJsonObject(), false);

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
                    list[0].name = removeProhibitedChar(list[0].name);
                    JOptionPane.showMessageDialog(PANEL, Translations.get("tabs.card.list_loaded") + list[0].name +
                            "\n" + Translations.get("tabs.card.card_number") + list[0].cards.size());
                    validateButton.setEnabled(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PANEL, Translations.get("file.error.json.loading"),
                        Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            loadedListCards[0] = createCardPanelAsScroll(list[0]);
            panel.add(loadedListCards[0]);
            panel.revalidate();
            panel.repaint();

        });

        validateButton.addActionListener(e -> {
            if (list[0] == null) {
                JOptionPane.showMessageDialog(PANEL,
                        Translations.get("tabs.card.empty_list_valid"),
                        Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                list[0] = null;
                validateButton.setEnabled(false);
                removeLoadedCards(loadedListCards[0], panel);
                loadedListCards[0] = null;
            }

            String fileName = list[0].name.replaceAll(" ", "_");
            Path outputFile = cardsDir.resolve(fileName + ".json");

            // cancel if already existing
            if (Files.exists(outputFile)) {
                JOptionPane.showMessageDialog(PANEL,
                        Translations.get("tabs.card.file_exist"),
                        Translations.get("file.error.exist"),
                        JOptionPane.ERROR_MESSAGE);
                list[0] = null;
                validateButton.setEnabled(false);
                removeLoadedCards(loadedListCards[0], panel);
                loadedListCards[0] = null;
                return;
            }

            try {
                Gson gson = new Gson();
                Files.writeString(outputFile, gson.toJson(list[0]));
                JOptionPane.showMessageDialog(PANEL,
                        Translations.get("common.file_saved"),
                        Translations.get("common.success"),
                        JOptionPane.INFORMATION_MESSAGE);
                validateButton.setEnabled(false);
                list[0] = null;
                removeLoadedCards(loadedListCards[0], panel);
                loadedListCards[0] = null;

                loadListPanel(); // reload

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PANEL,
                        Translations.get("file.error.saving") + ex.getMessage(),
                        Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
            }
        });

        // adding buttons to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(loadButton);
        buttonPanel.add(validateButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        return panel;
    }

    private void removeLoadedCards(JScrollPane scrollPane, JPanel panel) {
        if (scrollPane != null) {
            panel.remove(scrollPane);
            panel.revalidate();
            panel.repaint();
        }
    }

    @Override
    protected boolean invokeLater() {
        return true;
    }

    private JPanel createCardPanel(CardList list) {
        JPanel allCards = new JPanel();
        allCards.setLayout(new GridLayout(0, 1, 10, 10));
        allCards.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Card card : list.cards) {
            JPanel cardPanel = new JPanel(new GridLayout(1, 2, 5, 5));
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

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

    private JScrollPane createCardPanelAsScroll(CardList list) {
        JPanel p = createCardPanel(list);
        JScrollPane scrollPane = new JScrollPane(p);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private String getCheckedListName(String s) {
        if (s == null) return null;
        if (s.isEmpty() || s.trim().isEmpty()) return null;

        String newString = removeProhibitedChar(s);

        if (newString.isEmpty() || newString.trim().isEmpty()) return null;
        return newString;
    }

    private void showCardError() {
        JOptionPane.showMessageDialog(PANEL, Translations.get("file.json.error.invalid"),
                Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
    }

    private void checkAndCreateDir() {
        try {
            Files.createDirectories(cardsDir);
        } catch (IOException ignored) {}
    }

    private JPanel createCreatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // temp data
        final List<Card> tempCards = new ArrayList<>();
        final JTextField nameField = new JTextField(20);

        // up - name + button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addCardButton = new JButton(Translations.get("tabs.card.add_card"));
        JButton saveButton = new JButton(Translations.get("common.validate"));
        JButton cancelButton = new JButton(Translations.get("common.cancel"));
        JButton modifyButton = new JButton(Translations.get("common.modify"));
        saveButton.setEnabled(false);

        topPanel.add(new JLabel(Translations.get("tabs.card.list_name")));
        topPanel.add(nameField);
        topPanel.add(addCardButton);
        topPanel.add(saveButton);
        topPanel.add(cancelButton);
        topPanel.add(modifyButton);

        // center - added cards
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // display features
        Runnable[] refreshCards = new Runnable[1];

        refreshCards[0] = () -> {
            cardsPanel.removeAll();

            if (tempCards.isEmpty()) cardsPanel.add(new JLabel(Translations.get("tabs.card.no_card"), SwingConstants.CENTER));
            else for (Card card : tempCards) {
                JPanel row = new JPanel(new GridLayout(1, 2, 5, 5));
                row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

                JPanel content = new JPanel(new GridLayout(1, 2, 5, 5));
                JLabel left = new JLabel(card.main, SwingConstants.CENTER);
                JLabel right = new JLabel(card.secondary, SwingConstants.CENTER);

                left.setFont(new Font("Segoe UI", Font.BOLD, 14));
                right.setFont(new Font("Segoe UI", Font.BOLD, 14));

                content.add(left);
                content.add(right);

                // delete button
                JButton deleteButton = new JButton("✕");
                deleteButton.addActionListener(e -> {
                    tempCards.remove(card);
                    refreshCards[0].run();
                });

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                buttonPanel.add(deleteButton);

                row.add(content, BorderLayout.CENTER);
                row.add(buttonPanel, BorderLayout.EAST);

                cardsPanel.add(row);
            }

            cardsPanel.revalidate();
            cardsPanel.repaint();
            saveButton.setEnabled(!tempCards.isEmpty() && getCheckedListName(nameField.getText()) != null);
        };

        // button modify
        modifyButton.addActionListener(e -> {
            if (!tempCards.isEmpty()) {
                int reset = JOptionPane.showConfirmDialog(
                        panel, Translations.get("tabs.card.override_creating"),
                                Translations.get("common.override_check"),
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                );
                if (reset != JOptionPane.YES_OPTION) return;
            }

            refreshComboBox();
            tempCards.clear();

            // defs
            JPanel comboPanel = new JPanel(new BorderLayout(50, 400));
            JLabel label = new JLabel(Translations.get("tabs.card.choose_list"));
            comboPanel.add(label, BorderLayout.WEST);
            comboPanel.add(modifyComboBox, BorderLayout.EAST);

            JOptionPane.showMessageDialog(
                    panel, comboPanel,
                    Translations.get("tabs.card.choose_list"),
                    JOptionPane.QUESTION_MESSAGE
            );

            String selected = (String) modifyComboBox.getSelectedItem();
            if (selected == null || selected.equals(Translations.get("tabs.card.select_list"))) {
                JOptionPane.showMessageDialog(
                        panel, Translations.get("tabs.card.no_selected"),
                        Translations.get("common.error"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Path filePath = cardsDir.resolve(selected + ".json");
            if (!Files.exists(filePath)) {
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.file_not_found") + selected, Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String content = Files.readString(filePath);
                CardList cardList = CardJsonManager.fromJson(JsonParser.parseString(content).getAsJsonObject(), true);

                for (Card c : cardList.cards) {
                    if (c == null || c.main == null || c.secondary == null) {
                        JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.invalid_list"), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tempCards.add(c);
                }

                if (tempCards.isEmpty() || cardList.name == null) {
                    JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.invalid_list"), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nameField.setText(cardList.name.replaceAll("\"", ""));
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.read_error") + ex.getMessage(), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            refreshCards[0].run();

        });

        // button "add card"
        addCardButton.addActionListener(e -> {
            JTextField mainField = new JTextField();
            JTextField secondaryField = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            inputPanel.add(new JLabel(Translations.get("tabs.card.face.front")));
            inputPanel.add(mainField);
            inputPanel.add(new JLabel(Translations.get("tabs.card.face.back")));
            inputPanel.add(secondaryField);

            int result = JOptionPane.showConfirmDialog(
                    panel, inputPanel, Translations.get("tabs.card.new"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String main = removeProhibitedChar(mainField.getText());
                String secondary = removeProhibitedChar(secondaryField.getText());

                if (main.isEmpty() || secondary.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.new.empty"), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Card c = new Card();
                c.main = main;
                c.secondary = secondary;

                tempCards.add(c);
                refreshCards[0].run();
            }
        });

        // button validate (save)
        saveButton.addActionListener(e -> {
            String listName = getCheckedListName(nameField.getText());
            if (listName == null) {
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.invalid_list_name"), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tempCards.isEmpty()) {
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.saving_no_cards"), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            Path outputFile = cardsDir.resolve(listName.replaceAll(" ", "_") + ".json");
            if (Files.exists(outputFile)) {
                int overwrite = JOptionPane.showConfirmDialog(panel,
                        Translations.get("tabs.card.replace.content"),
                        Translations.get("file.error.exist"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (overwrite != JOptionPane.YES_OPTION) return;
            }

            CardList list = new CardList();
            list.name = listName;
            list.cards = new ArrayList<>(tempCards);

            try {
                Gson gson = new Gson();
                Files.writeString(outputFile, gson.toJson(list));
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.list_saved"), Translations.get("common.success"), JOptionPane.INFORMATION_MESSAGE);
                tempCards.clear();
                nameField.setText("");
                refreshCards[0].run();
                loadListPanel(); // refresh global list
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, Translations.get("tabs.card.error_saving") + ex.getMessage(), Translations.get("common.error"), JOptionPane.ERROR_MESSAGE);
            }
        });

        // button cancel
        cancelButton.addActionListener(e -> {
            if (tempCards.isEmpty() && nameField.getText().isEmpty()) return;

            int confirm = JOptionPane.showConfirmDialog(panel,
                    Translations.get("tabs.card.cancel_all"),
                    Translations.get("common.confirm"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                tempCards.clear();
                nameField.setText("");
                refreshCards[0].run();
            }
        });

        // auto run validate button check
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { refreshCards[0].run(); }
            @Override
            public void removeUpdate(DocumentEvent e) { refreshCards[0].run(); }
            @Override
            public void changedUpdate(DocumentEvent e) { refreshCards[0].run(); }
        });

        refreshCards[0].run();
        return panel;
    }

    @Override
    protected boolean doPreset() {
        return false;
    }

    @Override
    protected String getTitle() {
        return Translations.get("tabs.card.name");
    }
}
