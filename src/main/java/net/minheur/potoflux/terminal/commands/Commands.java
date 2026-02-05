package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.terminal.Command;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Registry of all commands of potoflux
 */
public class Commands {
    /**
     * The registry list of all potoflux commands
     */
    private final RegistryList<Command> LIST = new RegistryList<>();
    /**
     * Checks if the reg has already generated
     */
    private static boolean hasGenerated = false;

    /**
     * The only instance of the reg that should exist
     */
    public static Commands INSTANCE;

    /**
     * Builder to check of the reg has already generated
     */
    public Commands() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    /**
     * Command for the {@code hello} command.
     */
    public final Command HELLO_WORLD = LIST.add(new Command(fromModId("hello_world"), "hello", CommandActions::helloWorld, CommandHelp.help()));
    /**
     * Command for the {@code clear} command.
     */
    public final Command CLEAR = LIST.add(new Command(fromModId("clear"), "clear", CommandActions::clear, CommandHelp.clear()));
    /**
     * Command for the {@code modList} command.
     */
    public final Command MOD_LIST = LIST.add(new Command(fromModId("mod_list"), "modList", CommandActions::modList, CommandHelp.modList()));
    /**
     * Command for the {@code modDir} command.
     */
    public final Command MOD_DIR = LIST.add(new Command(fromModId("mod_dir"), "modDir", CommandActions::modDir, CommandHelp.modDir()));
    /**
     * Command for the {@code time} command.
     */
    public final Command TIME = LIST.add(new Command(fromModId("time"), "time", CommandActions::time, CommandHelp.time()));
    /**
     * Command for the {@code help} command.
     */
    public final Command HELP = LIST.add(new Command(fromModId("help"), "help", CommandActions::help, CommandHelp.help()));
        /**
     * Command for the {@code echo} command.
     */
    public final Command ECHO = LIST.add(new Command(fromModId("echo"), "echo", CommandActions::echo, CommandHelp.echo()));
        /**
     * Command for the {@code tab} command.
     */
    public final Command TAB = LIST.add(new Command(fromModId("tab"), "tab", CommandActions::tab, CommandHelp.tab()));
    /**
     * Command for the {@code source} command.
     */
    public final Command SOURCE = LIST.add(new Command(fromModId("source"), "source", CommandActions::sourceCode, CommandHelp.source()));
    public final Command VERSION = LIST.add(new Command(fromModId("version"), "ptfVersion", CommandActions::ptfVersion, CommandHelp.ptfVersion()));
    /**
     * Command for the {@code ascii} command.
     */
    public final Command ASCII = LIST.add(new Command(fromModId("ascii"), "ascii", CommandActions::writeAscii, CommandHelp.ascii()));
    /**
     * Command for the {@code quit} command.
     */
    public final Command QUIT = LIST.add(new Command(fromModId("quit"), "quit", CommandActions::quit, CommandHelp.quit()));

    // hidden commands
    /**
     * Command for the {@code hide} command.<br>
     * It's hidden
     */
    public final Command HIDDEN = LIST.add(new Command(fromModId("hidden"), "hide", CommandActions::hidden));
    /**
     * Command for the {@code nope} command.<br>
     * It's hidden
     */
    public final Command NOPE = LIST.add(new Command(fromModId("nope"), "nope", CommandActions::nope));
    // easter egg for Mathis
    public final Command POTATO_COMMAND = LIST.add(new Command(fromModId("mathis_easter_egg"), "LordHawima", CommandActions::lordHawima));

    /**
     * Registers the commands to the reg
     * @param event the event to register commands to
     */
    public static void register(RegisterCommandsEvent event) {
        INSTANCE = new Commands();
        INSTANCE.LIST.register(event.reg);
    }
}
