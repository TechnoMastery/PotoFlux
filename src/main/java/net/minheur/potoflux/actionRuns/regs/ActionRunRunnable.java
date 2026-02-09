package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.utils.LogAmountManager;

import javax.swing.*;

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

    /**
     * The action to execute for the terminal saving
     */
    public static void saveTerminal() {
        CommandProcessor.runSaveTerminal();
    }
    /**
     * The action to execute for the mod list saving
     */
    public static void saveModList() {
        PotoFluxLoadingContext.saveModList();
    }
}
