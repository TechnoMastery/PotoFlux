package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.terminal.Command;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Registry of all commands of potoflux
 */
public class Commands {
    /**
     * The registry list of all potoflux commands
     */
    private static final RegistryList<Command> LIST = new RegistryList<>();

    /**
     * Command for the {@code hello} command.
     */
    public static final SmartSupplier<Command> HELLO_WORLD = LIST.add(() -> new Command(fromModId("hello_world"), "hello", CommandActions::helloWorld, CommandHelp.help()));
    /**
     * Command for the {@code clear} command.
     */
    public static final SmartSupplier<Command> CLEAR = LIST.add(() -> new Command(fromModId("clear"), "clear", CommandActions::clear, CommandHelp.clear()));
    /**
     * Command for the {@code modList} command.
     */
    public static final SmartSupplier<Command> MOD_LIST = LIST.add(() -> new Command(fromModId("modList"), "modList", CommandActions::modList, CommandHelp.modList()));
    /**
     * Command for the {@code modDir} command.
     */
    public static final SmartSupplier<Command> MOD_DIR = LIST.add(() -> new Command(fromModId("modDir"), "modDir", CommandActions::modDir, CommandHelp.modDir()));
    /**
     * Command for the {@code logDir} command.
     */
    public static final SmartSupplier<Command> LOG_DIR = LIST.add(() -> new Command(fromModId("logsDir"), "logDir", CommandActions::logDir, CommandHelp.logDir()));
    /**
     * Command for the {@code time} command.
     */
    public static final SmartSupplier<Command> TIME = LIST.add(() -> new Command(fromModId("time"), "time", CommandActions::time, CommandHelp.time()));
    /**
     * Command for the {@code echo} command.
     */
    public static final SmartSupplier<Command> ECHO = LIST.add(() -> new Command(fromModId("echo"), "echo", CommandActions::echo, CommandHelp.echo()));    /**
     * Command for the {@code help} command.
     */
    public static final SmartSupplier<Command> HELP = LIST.add(() -> new Command(fromModId("help"), "help", CommandActions::help, CommandHelp.help()));
    /**
     * Command for the {@code tab} command.
     */
    public static final SmartSupplier<Command> TAB = LIST.add(() -> new Command(fromModId("tab"), "tab", CommandActions::tab, CommandHelp.tab()));
    /**
     * Command for the {@code tabList} command.
     */
    public static final SmartSupplier<Command> TAB_LIST = LIST.add(() -> new Command(fromModId("tabList"), "tabList", CommandActions::tabList, CommandHelp.tabList()));
    /**
     * Command for the {@code source} command.
     */
    public static final SmartSupplier<Command> SOURCE = LIST.add(() -> new Command(fromModId("source"), "source", CommandActions::sourceCode, CommandHelp.source()));
    /**
     * Command for the {@code ptfVersion} command.
     */
    public static final SmartSupplier<Command> VERSION = LIST.add(() -> new Command(fromModId("version"), "ptfVersion", CommandActions::ptfVersion, CommandHelp.ptfVersion()));
    /**
     * Command for the {@code ascii} command.
     */
    public static final SmartSupplier<Command> ASCII = LIST.add(() -> new Command(fromModId("ascii"), "ascii", CommandActions::writeAscii, CommandHelp.ascii()));
    /**
     * Command for the {@code quit} command.
     */
    public static final SmartSupplier<Command> QUIT = LIST.add(() -> new Command(fromModId("quit"), "quit", CommandActions::quit, CommandHelp.quit()));

    // hidden commands
    /**
     * Command for the {@code hide} command.<br>
     * It's hidden
     */
    public static final SmartSupplier<Command> HIDDEN = LIST.add(() -> new Command(fromModId("hidden"), "hide", CommandActions::hidden));
    /**
     * Command for the {@code nope} command.<br>
     * It's hidden
     */
    public static final SmartSupplier<Command> NOPE = LIST.add(() -> new Command(fromModId("nope"), "nope", CommandActions::nope));
    /**
     * An Easter egg for Mathis (aka. Maggacco666)
     */
    public static final SmartSupplier<Command> POTATO_COMMAND = LIST.add(() -> new Command(fromModId("mathis_easter_egg"), "LordHawima", CommandActions::lordHawima));
    public static final SmartSupplier<Command> DISABLE_RICK_ROLL = LIST.add(() -> new Command(fromModId("diable_rick_roll"), "rockRoll", CommandActions::disableRickRoll));

    /**
     * Registers the commands to the reg
     *
     * @param event the event to register commands to
     */
    public static void register(@NotNull RegisterCommandsEvent event) {
        LIST.register(event.reg);
    }


}
