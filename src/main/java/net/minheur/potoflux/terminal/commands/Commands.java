package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.loader.mod.events.SubscribeEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.terminal.Command;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Commands {
    private static final RegistryList<Command> LIST = new RegistryList<>();

    public static final Command HELLO_WORLD = LIST.add(new Command(fromModId("hello_workd"), "hello", CommandActions::helloWorld, CommandHelp.help()));
    public static final Command CLEAR = LIST.add(new Command(fromModId("clear"), "clear", CommandActions::clear, CommandHelp.clear()));
    public static final Command TIME = LIST.add(new Command(fromModId("time"), "time", CommandActions::time, CommandHelp.time()));
    public static final Command HELP = LIST.add(new Command(fromModId("help"), "help", CommandActions::help, CommandHelp.help()));
    public static final Command ECHO = LIST.add(new Command(fromModId("echo"), "echo", CommandActions::echo, CommandHelp.echo()));
    public static final Command TAB = LIST.add(new Command(fromModId("tab"), "tab", CommandActions::tab, CommandHelp.tab()));
    public static final Command SOURCE = LIST.add(new Command(fromModId("source"), "source", CommandActions::sourceCode, CommandHelp.source()));
    public static final Command ASCII = LIST.add(new Command(fromModId("ascii"), "ascii", CommandActions::writeAscii, CommandHelp.ascii()));
    public static final Command QUIT = LIST.add(new Command(fromModId("quit"), "quit", CommandActions::quit, CommandHelp.quit()));

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        LIST.register(event.reg);
    }
}
