package net.minheur.potoflux.terminal;

import net.minheur.potoflux.PotoFlux;

import javax.swing.*;

public class CommandProcessor {
    private static final JTextArea outputArea = PotoFlux.app.getOutputArea();

    public static void processCommand(String pCommand) {
        // check command is empty
        if (pCommand == null || pCommand.trim().isEmpty()) {
            appendOutput("Could not execute empty command !");
            return;
        }
        // send command
        appendOutput("> " + pCommand);
        // check if command exist
        if (!Commands.containsKey(pCommand)) {
            appendOutput("Command not recognized ! Try help !");
            return;
        }
        // define & send command result
        Commands command = Commands.getCommandWithKey(pCommand);
        if (command == null) throw new IllegalCallerException("Command exist but is empty !");
        command.getCommandOutput().run();
    }

    public static void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

}
