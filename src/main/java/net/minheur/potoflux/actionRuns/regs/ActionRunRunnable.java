package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.update.Candidate;
import net.minheur.potoflux.loader.mod.update.ModUpdateReg;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.TokenHandler;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.terminal.CommandHistorySaver;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.utils.LogAmountManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all potoflux action run runnable
 */
public class ActionRunRunnable {
    /**
     * The action to execute for the terminal filling
     */
    public static void fillTerminal() {
        ((TerminalTab) PotoFlux.app.getTabMap().get(Tabs.INSTANCE.TERMINAL)).getTerminal().fillOutputTextArea();
    }

    public static void checkRickRoll() {
        int logAmount = LogAmountManager.getLogAmount();
        boolean isCorrectLogAmount = logAmount % 50 == 0;

        if (!isCorrectLogAmount) return;

        JOptionPane.showMessageDialog(null, "It looks like it's your " + logAmount + "th connection !",
                "Connection amount",
                JOptionPane.INFORMATION_MESSAGE);

        Functions.browse("https://rickroll.it/rickroll.mp4");
    }

    public static void displayModUpdates() {
        List<Candidate> candidates = ModUpdateReg.getAll();
        if (candidates.isEmpty()) return;

        for (Candidate c : candidates) {
            PotoFluxLoadingContext.openUpdateDialog(
                    c.mod(),
                    c.compatible(),
                    c.declaredLastest()
            );
        }
    }

    public static void loadCommandHistory() {
        File target = PotoFlux.getProgramDir().resolve("commandHistory.txt").toFile();
        List<String> history = new ArrayList<>();

        if (!target.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(target))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (history.size() >= CommandHistorySaver.MAX_SIZE) break;

                if (!line.isBlank())
                    history.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not load command history");
        }

        if (!history.isEmpty())
            CommandHistorySaver.loadFrom(history);

    }
    public static void saveCommandHistory() {
        Path target = PotoFlux.getProgramDir().resolve("commandHistory.txt");
        List<String> history = CommandHistorySaver.get();

        if (history.isEmpty()) {

            try {
                Files.deleteIfExists(target);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

        StringBuilder content = new StringBuilder();

        content.append(history.get(0));
        for (int i = 1; i < history.size(); i++) {
            content.append("\n")
                    .append(history.get(i));
        }

        try {

            Files.createDirectories(target.getParent());
            Files.writeString(target, content.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks if you have a token.
     * If you do, sends a request to connect to your account
     */
    public static void connectToken() {
        if (!TokenHandler.has()) return;

        ConnectionHandler.accountFor(TokenHandler.get());
    }

    /**
     * The action to execute for the terminal saving
     */
    public static void saveTerminal() {
        CommandProcessor.runSaveTerminal();
    }
}
