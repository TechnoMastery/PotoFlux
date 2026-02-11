package net.minheur.potoflux.terminal;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * The main tab used to handle the terminal
 */
public class Terminal {
    /**
     * The area to print the content of the terminal (log)
     */
    private final JTextArea outputArea;
    /**
     * The area to write command in
     */
    private final JTextField inputField;

    /**
     * Init the terminal
     * @param panel the element that will contain the terminal. Should be empty
     */
    public Terminal(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // output system
        outputArea = new JTextArea();
        JScrollPane scrollPanel = setupOutputAndGetScroll();

        // input system
        inputField = setupInput();
        JPanel inputPanel = setupInputPanel();

        // main adding to panel
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollPanel,
                inputPanel
        );
        splitPane.setResizeWeight(1.0);
        splitPane.setDividerSize(5);
        splitPane.setDividerLocation(0.9);
        panel.add(splitPane, BorderLayout.CENTER);
    }

    @Nonnull
    private JPanel setupInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel prompt = new JLabel("  > " );
        prompt.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputField.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputPanel.add(prompt, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        return inputPanel;
    }

    @Nonnull
    private JTextField setupInput() {
        final JTextField inputField;
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                CommandProcessor.processCommand(command);
                inputField.setText("");
            }
        });
        return inputField;
    }

    @Nonnull
    private JScrollPane setupOutputAndGetScroll() {
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        return new JScrollPane(outputArea);
    }

    /**
     * Getter for the {@link #outputArea}
     * @return {@link #outputArea}
     */
    public JTextArea getOutputArea() {
        return outputArea;
    }

    /**
     * Fills the output area with the content of the terminal, saved in the files
     */
    public void fillOutputTextArea() {
        Path file = PotoFlux.getProgramDir().resolve("terminal.txt");

        // existing check
        if (!Files.exists(file)) {
            outputArea.setText("");
            buildASCII();
            return;
        }

        try {
            String content = Files.readString(file);

            // empty check
            if (content.trim().isEmpty()) {
                outputArea.setText("");
                buildASCII();
                return;
            }

            outputArea.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
            CommandProcessor.appendOutput("ERROR loading terminal file");
        }
    }

    /**
     * Gets the content of an ascii (in the files)
     * @param file name of the ASCII file the get
     * @return the content of the ASCII file
     */
    public static String getAsciiFileContent(String file) {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(Terminal.class.getResourceAsStream("/ascii/" + file + ".txt")),
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
