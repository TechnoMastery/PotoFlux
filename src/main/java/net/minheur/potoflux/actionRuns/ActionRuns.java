package net.minheur.potoflux.actionRuns;

import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.actionRuns.regs.ActionRunRunnable;
import net.minheur.potoflux.loader.mod.events.RegisterRunsEvent;
import net.minheur.potoflux.registry.RegistryList;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class ActionRuns {
    private final RegistryList<ActionRun> LIST_CLOSE = new RegistryList<>();
    private final RegistryList<ActionRun> LIST_START_UI = new RegistryList<>();
    private final RegistryList<ActionRun> LIST_START_LOGIC = new RegistryList<>();

    private static boolean hasGenerated = false;
    public static ActionRuns INSTANCE;

    private ActionRuns() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    // start ui
    public final ActionRun FILL_TERMINAL = LIST_START_UI.add(new ActionRun(fromModId("fill_terminal"), ActionRunRunnable::fillTerminal));

    // start logic

    // close
    public final ActionRun SAVE_TERMINAL = LIST_CLOSE.add(new ActionRun(fromModId("save_terminal"), ActionRunRunnable::saveTerminal));
    public final ActionRun SAVE_MOD_LIST = LIST_CLOSE.add(new ActionRun(fromModId("save_mod_list"), ActionRunRunnable::saveModList));

    public static void register(RegisterRunsEvent event) {
        INSTANCE = new ActionRuns();

        INSTANCE.LIST_CLOSE.register(event.closeReg);
        INSTANCE.LIST_START_UI.register(event.startUiReg);
        INSTANCE.LIST_START_LOGIC.register(event.startLogicReg);
    }
}
