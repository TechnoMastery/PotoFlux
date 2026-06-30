package net.minheur.potoflux.actionRuns;

import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.actionRuns.regs.ActionRunRunnable;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.events.RegisterRunsEvent;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.notifications.NotificationHandler;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * the potoflux reg for the action runs
 */
public class ActionRuns {
    /**
     * The reg of actions that runs on close
     */
    private static final RegistryList<ActionRun> LIST_CLOSE = new RegistryList<>();
    /**
     * This action saves the terminal when the app closes
     */
    public static final SmartSupplier<ActionRun> SAVE_TERMINAL = LIST_CLOSE.add(() -> new ActionRun(fromModId("save_terminal"), ActionRunRunnable::saveTerminal));
    /**
     * This action saves the terminal's command history
     */
    public static final SmartSupplier<ActionRun> SAVE_COMMAND_HISTORY = LIST_CLOSE.add(() -> new ActionRun(fromModId("save_command_history"), ActionRunRunnable::saveCommandHistory));

    /**
     * The reg of actions that runs on start, in the UI scope
     */
    private static final RegistryList<ActionRun> LIST_START_UI = new RegistryList<>();
    /**
     * This action fills the terminal when the UI starts
     */
    public static final SmartSupplier<ActionRun> FILL_TERMINAL = LIST_START_UI.add(() -> new ActionRun(fromModId("fill_terminal"), ActionRunRunnable::fillTerminal));
    /**
     * Updates the auth button states on boot.
     */
    public static final SmartSupplier<ActionRun> UPDATE_AUTH_BUTTONS = LIST_START_UI.add(() -> new ActionRun(fromModId("update_auth_buttons"), ConnectionHandler::reloadAuthUi));
    /**
     * Displays the mod updates.
     */
    public static final SmartSupplier<ActionRun> DISPLAY_MOD_UPDATES = LIST_START_UI.add(() -> new ActionRun(fromModId("display_mod_updates"), ActionRunRunnable::displayModUpdates));
    /**
     * Displays the mod errors.
     */
    public static final SmartSupplier<ActionRun> DISPLAY_MOD_ERRORS = LIST_START_UI.add(() -> new ActionRun(fromModId("display_mod_errors"), ActionRunRunnable::displayModErrors));
    /**
     * Displays the potoflux update popup.
     */
    public static final SmartSupplier<ActionRun> CHECK_POTOFLUX_UPDATE = LIST_START_UI.add(() -> new ActionRun(fromModId("check_potoflux_update"), PotoFluxLoadingContext::checkUpdates));

    /**
     * The reg of actions that runs on start, in the logic scope
     */
    private static final RegistryList<ActionRun> LIST_START_LOGIC = new RegistryList<>();
    /**
     * Runs rick roll check
     */
    public static final SmartSupplier<ActionRun> CHECK_RICK_ROLL = LIST_START_LOGIC.add(() -> new ActionRun(fromModId("check_rick_roll"), ActionRunRunnable::checkRickRoll));
    /**
     * Load the terminal's command history
     */
    public static final SmartSupplier<ActionRun> LOAD_COMMAND_HISTORY = LIST_START_LOGIC.add(() -> new ActionRun(fromModId("load_command_history"), ActionRunRunnable::loadCommandHistory));
    /**
     * Connect to your account with your token, if you have one
     */
    public static final SmartSupplier<ActionRun> CONNECT_TOKEN = LIST_START_LOGIC.add(() -> new ActionRun(fromModId("connect_token"), ActionRunRunnable::connectToken));
    /**
     * Run the account creation authorization check.
     */
    public static final SmartSupplier<ActionRun> CHECK_ALLOW_ACCOUNT_CREATION = LIST_START_LOGIC.add(() -> new ActionRun(fromModId("check_allow_account_creation"), ConnectionHandler::reloadAccountCreationPermission));
    /**
     * Run the notif checks.
     */
    public static final SmartSupplier<ActionRun> INIT_NOTIFICATIONS = LIST_START_LOGIC.add(() -> new ActionRun(fromModId("init_notifications"), NotificationHandler::load));

    /**
     * This registers all action runs to the main reg
     *
     * @param event the event to register all actions to
     */
    public static void register(@NotNull RegisterRunsEvent event) {
        LIST_CLOSE.register(event.closeReg);
        LIST_START_UI.register(event.startUiReg);
        LIST_START_LOGIC.register(event.startLogicReg);
    }
}
