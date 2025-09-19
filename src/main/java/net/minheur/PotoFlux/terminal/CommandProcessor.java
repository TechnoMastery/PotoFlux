package net.minheur.PotoFlux.terminal;

import javax.swing.*;
import java.time.LocalTime;

public class CommandProcessor {
    private JTextArea outputArea = MiniTerminal.
    public static void OutputProcessor(String command) {
        // chose what to do with the command
        appendOutput("> " + command);
        switch (command) {
            case "hello":
                appendOutput("Hello world !");
                break;
            case "time":
                appendOutput("System time: " + LocalTime.now());
                break;
            default:
                appendOutput("Unknown command: " + command);
        }
    }

    public static void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
}
