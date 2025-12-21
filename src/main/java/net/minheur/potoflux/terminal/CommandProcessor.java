package net.minheur.potoflux.terminal;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.logger.LogCategories;
import net.minheur.potoflux.utils.logger.PtfLogger;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class CommandProcessor {
    private static final JTextArea outputArea = ((TerminalTab) PotoFlux.app.getTabMap().get(Tabs.INSTANCE.TERMINAL)).getTerminal().getOutputArea();

    public static void processCommand(String pCommand) {
        // check command is empty
        if (pCommand == null || pCommand.trim().isEmpty()) {
            appendOutput(Translations.get("potoflux:commandPro.empty"));
            return;
        }

        // send command
        appendOutput("> " + pCommand);

        // split
        String[] split = pCommand.trim().split("\\s+");
        String cmdKey = split[0];
        List<String> args = Arrays.asList(split).subList(1, split.length);

        // check if command exist
        if (!CommandRegistry.containsKey(cmdKey)) {
            appendOutput(Translations.get("potoflux:commandPro.none"));
            return;
        }

        // define & send command result
        Command command = CommandRegistry.getCommandWithKey(cmdKey);
        if (command == null) throw new IllegalCallerException("Command exist but is empty !");
        command.getCommandOutput().accept(args);
    }

    public static void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void clearArea() {
        outputArea.setText("");
    }

    public static void runSaveTerminal() {
        String content = outputArea.getText();
        Path file = PotoFlux.getProgramDir().resolve("terminal.txt");

        if (content.trim().isEmpty()) { // is string is empty : delete file
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            Files.createDirectories(file.getParent());

            Files.writeString(file, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            PtfLogger.info("Terminal saved !", LogCategories.TERMINAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
