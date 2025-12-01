package net.minheur.potoflux.terminal;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.utils.UserPrefsManager;

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

public class Terminal {
    private final JTextArea outputArea;
    private final JTextField inputField;

    public Terminal(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // register all commands
        PotoFluxLoadingContext.get().getModEventBus().post(new RegisterCommandsEvent());

        // output system
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        // style
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        // adding
        JScrollPane scrollPanel = new JScrollPane(outputArea);

        // input system
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                CommandProcessor.processCommand(command);
                inputField.setText("");
            }
        });

        // input panel prefix
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel prompt = new JLabel("  > " );
        prompt.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputField.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputPanel.add(prompt, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);

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

    public JTextArea getOutputArea() {
        return outputArea;
    }

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

    public static String getAsciiFileContent(String file) {
        try (Reader reader = new InputStreamReader(
                Terminal.class.getResourceAsStream("/ascii/" + file + ".txt"),
                StandardCharsets.UTF_8
        )) {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                content.append(buffer, 0, len);
            }

            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void buildASCII() {
        String asciiFile = UserPrefsManager.getTerminalASCII();
        if (asciiFile == null) asciiFile = "big";

        String asciiContent = getAsciiFileContent(asciiFile);
        if (asciiContent == null) {
            JOptionPane.showMessageDialog(null, "ERROR: Failed loading translations ! Please report this error.");
            return;
        }

        CommandProcessor.appendOutput(asciiContent);

    }
}
