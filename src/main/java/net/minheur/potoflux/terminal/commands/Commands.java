package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.terminal.Command;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Commands {
    private final RegistryList<Command> LIST = new RegistryList<>();
    private static boolean hasGenerated = false;

    public static Commands INSTANCE;

    public Commands() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    public final Command HELLO_WORLD = LIST.add(new Command(fromModId("hello_world"), "hello", CommandActions::helloWorld, CommandHelp.help()));
    public final Command CLEAR = LIST.add(new Command(fromModId("clear"), "clear", CommandActions::clear, CommandHelp.clear()));
    public final Command MOD_LIST = LIST.add(new Command(fromModId("mod_list"), "modList", CommandActions::modList, CommandHelp.modList()));
    public final Command MOD_DIR = LIST.add(new Command(fromModId("mod_dir"), "modDir", CommandActions::modDir, CommandHelp.modDir()));
    public final Command TIME = LIST.add(new Command(fromModId("time"), "time", CommandActions::time, CommandHelp.time()));
    public final Command HELP = LIST.add(new Command(fromModId("help"), "help", CommandActions::help, CommandHelp.help()));
    public final Command ECHO = LIST.add(new Command(fromModId("echo"), "echo", CommandActions::echo, CommandHelp.echo()));
    public final Command TAB = LIST.add(new Command(fromModId("tab"), "tab", CommandActions::tab, CommandHelp.tab()));
    public final Command SOURCE = LIST.add(new Command(fromModId("source"), "source", CommandActions::sourceCode, CommandHelp.source()));
    public final Command VERSION = LIST.add(new Command(fromModId("version"), "ptfVersion", CommandActions::ptfVersion, CommandHelp.ptfVersion()));
    public final Command ASCII = LIST.add(new Command(fromModId("ascii"), "ascii", CommandActions::writeAscii, CommandHelp.ascii()));
    public final Command QUIT = LIST.add(new Command(fromModId("quit"), "quit", CommandActions::quit, CommandHelp.quit()));

    // hidden commands
    public final Command HIDDEN = LIST.add(new Command(fromModId("hidden"), "hide", CommandActions::hidden));
    public final Command NOPE = LIST.add(new Command(fromModId("nope"), "nope", CommandActions::nope));
    // easter egg for Mathis
    public final Command POTATO_COMMAND = LIST.add(new Command(fromModId("mathis_easter_egg"), "LordHawima", CommandActions::lordHawima));

    public static void register(RegisterCommandsEvent event) {
        INSTANCE = new Commands();
        INSTANCE.LIST.register(event.reg);
    }
}
