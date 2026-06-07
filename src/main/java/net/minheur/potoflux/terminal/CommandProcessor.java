package net.minheur.potoflux.terminal;

import javafx.scene.control.TextArea;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class is used to handle the processing of the commands, the input and the output
 */
public class CommandProcessor {
    /**
     * Gets the output area from the terminal tab.<br>
     * Used to add content to the log
     */
    private static final Supplier<TextArea> outputArea = () -> ((TerminalTab) PotoFlux.app.getTabMap().get(Tabs.TERMINAL.get())).getTerminal().getOutputArea();

    /**
     * Process a raw command to an output in the terminal
     * @param pCommand the raw command to process
     */
    public static void processCommand(String pCommand) {
        // check command is empty
        if (pCommand == null || pCommand.trim().isEmpty()) {
            appendOutput(Translations.get("potoflux:commandPro.empty"));
            return;
        }

        // send command
        appendOutput("> " + pCommand);

        // save command to history
        CommandHistorySaver.save(pCommand);

        // split
        String[] split = pCommand.trim().split("\\s+");
        String cmdKey = split[0];
        List<String> args = Arrays.asList(split).subList(1, split.length);

        // check if command exist
        if (!CommandRegistry.containsKey(cmdKey)) {
            appendNoCommand();
            return;
        }

        // define & send command result
        Command command = CommandRegistry.getCommandWithKey(cmdKey);
        if (command == null) throw new IllegalCallerException("Command exist but is empty !");
        command.commandOutput().accept(args);
    }

    /**
     * Method to write in the terminal that no commands has been found.
     */
    public static void appendNoCommand() {
        appendOutput(Translations.get("potoflux:commandPro.none"));
    }

    /**
     * Method used to print something in the terminal.
     * @param text the content to print
     */
    public static void appendOutput(String text) {
        outputArea.get().appendText(text + "\n");
        outputArea.get().positionCaret(outputArea.get().getLength());
    }

    /**
     * Empties all the terminal content
     */
    public static void clearArea() {
        outputArea.get().setText("");
    }

    /**
     * Saves the terminal content to the file
     */
    public static void runSaveTerminal() {
        String content = outputArea.get().getText();
        Path file = PotoFlux.getProgramDir().resolve("terminal.txt");

        if (checkEmptyTerminal(content, file)) return;

        try {
            Files.createDirectories(file.getParent());

            Files.writeString(file, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            PtfLogger.info("Terminal saved !", LogCategories.TERMINAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the terminal's file content is empty.<br>
     * If so, deletes the file.
     * @param content of the terminal's content file
     * @param file path to the file, used to remove it if needed
     * @return if the file's content is empty
     */
    private static boolean checkEmptyTerminal(String content, Path file) {
        if (content.trim().isEmpty()) { // if string is empty : delete file
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
