package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.terminal.CommandProcessor;

public class ActionRunRunnable {
    public static void fillTerminal() {
        ((TerminalTab) PotoFlux.app.getTabMap().get(Tabs.INSTANCE.TERMINAL)).getTerminal().fillOutputTextArea();
    }

    public static void saveTerminal() {
        CommandProcessor.runSaveTerminal();
    }
    public static void saveModList() {
        PotoFluxLoadingContext.saveModList();
    }
}
