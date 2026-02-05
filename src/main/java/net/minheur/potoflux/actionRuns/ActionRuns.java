package net.minheur.potoflux.actionRuns;

import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.actionRuns.regs.ActionRunRunnable;
import net.minheur.potoflux.loader.mod.events.RegisterRunsEvent;
import net.minheur.potoflux.registry.RegistryList;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * the potoflux reg for the action runs
 */
public class ActionRuns {
    /**
     * The reg of actions that runs on close
     */
    private final RegistryList<ActionRun> LIST_CLOSE = new RegistryList<>();
    /**
     * The reg of actions that runs on start, in the UI scope
     */
    private final RegistryList<ActionRun> LIST_START_UI = new RegistryList<>();
    /**
     * The reg of actions that runs on start, in the logic scope
     */
    private final RegistryList<ActionRun> LIST_START_LOGIC = new RegistryList<>();

    /**
     * Handles if the reg has already generated
     */
    private static boolean hasGenerated = false;
    /**
     * The instance of the reg, used to access action runs
     */
    public static ActionRuns INSTANCE;

    /**
     * Make sure the reg cannot generate 2 times
     */
    private ActionRuns() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    // start ui
    /**
     * This action fills the terminal when the UI starts
     */
    public final ActionRun FILL_TERMINAL = LIST_START_UI.add(new ActionRun(fromModId("fill_terminal"), ActionRunRunnable::fillTerminal));

    // start logic

    // close
    /**
     * This action saves the terminal when the app closes
     */
    public final ActionRun SAVE_TERMINAL = LIST_CLOSE.add(new ActionRun(fromModId("save_terminal"), ActionRunRunnable::saveTerminal));
    /**
     * This action saves the mod list when the app closes
     */
    public final ActionRun SAVE_MOD_LIST = LIST_CLOSE.add(new ActionRun(fromModId("save_mod_list"), ActionRunRunnable::saveModList));

    /**
     * This registers all action runs to the main reg
     * @param event the event to register all actions to
     */
    public static void register(RegisterRunsEvent event) {
        INSTANCE = new ActionRuns();

        INSTANCE.LIST_CLOSE.register(event.closeReg);
        INSTANCE.LIST_START_UI.register(event.startUiReg);
        INSTANCE.LIST_START_LOGIC.register(event.startLogicReg);
    }
}
