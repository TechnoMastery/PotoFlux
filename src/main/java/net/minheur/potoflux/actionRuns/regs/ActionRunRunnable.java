package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.terminal.CommandProcessor;

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
