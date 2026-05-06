package net.minheur.potoflux.terminal;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.UserPrefsManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * The main tab used to handle the terminal
 */
public class Terminal {
    /**
     * The area to print the content of the terminal (log)
     */
    private final TextArea outputArea;
    /**
     * The area to write command in
     */
    private final TextField inputField;
    private int historyIndex = -1;

    /**
     * Init the terminal
     * @param panel the element that will contain the terminal. Should be empty
     */
    public Terminal(StackPane panel) {
        BorderPane root = new BorderPane();

        // OUTPUT
        outputArea = new TextArea();
        setupOutput();

        ScrollPane scrollPanel = new ScrollPane(outputArea);
        scrollPanel.setFitToWidth(true);
        scrollPanel.setFitToHeight(true);

        // INPUT
        inputField = new TextField();
        HBox inputPanel = setupInputPanel();

        // layout
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(scrollPanel, inputPanel);
        splitPane.setDividerPositions(0.9);

        root.setCenter(splitPane);
        panel.getChildren().add(root);
    }

    private @NotNull HBox setupInputPanel() {
        HBox inputPanel = new HBox();
        inputPanel.setSpacing(5);

        Label prompt = new Label("  > " );
        prompt.setFont(Font.font("Consolas", 20));

        inputField.setFont(Font.font("Consolas", 20));
        HBox.setHgrow(inputField, Priority.ALWAYS);

        setupInputActions();

        inputPanel.getChildren().addAll(prompt, inputField);
        return inputPanel;
    }

    private void setupInputActions() {

        inputField.setOnAction(e -> {
            String command = inputField.getText();
            CommandProcessor.processCommand(command);
            inputField.setText("");
            historyIndex = -1;
        });

        inputField.setOnKeyPressed(e -> {
            List<String> history = CommandHistorySaver.get();

            if (e.getCode() == KeyCode.UP) {
                if (history.isEmpty()) return;

                if (historyIndex < history.size() -1) historyIndex++;

                inputField.setText(history.get(historyIndex));
            }

            if (e.getCode() == KeyCode.DOWN) {
                if (history.isEmpty()) return;

                if (historyIndex > 0) {
                    historyIndex--;
                    inputField.setText(history.get(historyIndex));
                } else {
                    historyIndex = -1;
                    inputField.setText("");
                }
            }

            if (e.getCode() == KeyCode.ESCAPE) {
                historyIndex = -1;
                inputField.setText("");
            }
        });
    }

    private void setupOutput() {
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Consolas", 20));
        outputArea.setWrapText(true);
    }

    /**
     * Getter for the {@link #outputArea}
     * @return {@link #outputArea}
     */
    public TextArea getOutputArea() {
        return outputArea;
    }

    /**
     * Fills the output area with the content of the terminal, saved in the files
     */
    public void fillOutputTextArea() {
        Path file = PotoFlux.getProgramDir().resolve("terminal.txt");

        // existing check
        if (!Files.exists(file)) {
            outputArea.clear();
            buildASCII();
            return;
        }

        try {
            String content = Files.readString(file);

            // empty check
            if (content.trim().isEmpty()) {
                outputArea.clear();
                buildASCII();
                return;
            }

            outputArea.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
            CommandProcessor.appendOutput("ERROR loading terminal file");
            PtfLogger.error("ERROR loading terminal file");
        }
    }

    /**
     * Gets the content of an ascii (in the files)
     * @param file name of the ASCII file the get
     * @return the content of the ASCII file
     */
    public static String getAsciiFileContent(String file) {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(
                        Terminal.class.getResourceAsStream("/ascii/" + file + ".txt")
                ),
                StandardCharsets.UTF_8
        )) {
            StringBuilder content = new StringBuilder();
            fillContentFromReader(reader, content);

            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void fillContentFromReader(Reader reader, StringBuilder content) throws IOException {
        char[] buffer = new char[1024];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            content.append(buffer, 0, len);
        }
    }

    /**
     * Write the ASCII (the one set in the user prefs).<br>
     * By default, using {@code big}
     */
    public static void buildASCII() {
        String asciiFile = UserPrefsManager.getTerminalASCII();
        if (asciiFile == null) asciiFile = "big";

        String asciiContent = getAsciiFileContent(asciiFile);
        if (asciiContent == null) {
            JOptionPane.showMessageDialog(null, "ERROR: Failed getting ASCII ! Please report this error.");
            return;
        }

        CommandProcessor.appendOutput(asciiContent);

    }
}
