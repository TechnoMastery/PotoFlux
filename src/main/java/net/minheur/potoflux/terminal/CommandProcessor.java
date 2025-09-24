package net.minheur.potoflux.terminal;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.utils.Translations;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class CommandProcessor {
    private static final JTextArea outputArea = ((TerminalTab) PotoFlux.app.getTabMap().get(Tabs.TERMINAL)).getTerminal().getOutputArea();

    public static void processCommand(String pCommand) {
        // check command is empty
        if (pCommand == null || pCommand.trim().isEmpty()) {
            appendOutput(Translations.get("commandPro.empty"));
            return;
        }

        // send command
        appendOutput("> " + pCommand);

        // split
        String[] split = pCommand.trim().split("\\s+");
        String cmdKey = split[0];
        List<String> args = Arrays.asList(split).subList(1, split.length);

        // check if command exist
        if (!Commands.containsKey(cmdKey)) {
            appendOutput(Translations.get("commandPro.none"));
            return;
        }

        // define & send command result
        Commands command = Commands.getCommandWithKey(cmdKey);
        if (command == null) throw new IllegalCallerException("Command exist but is empty !");
        command.getCommandOutput().accept(args);
    }

    public static void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

}
