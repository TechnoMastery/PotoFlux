package net.minheur.potoflux.terminal;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.utils.Translations;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public enum Commands {
    HELLO_WORLD("hello", CommandActions::helloWorld, Translations.get("command.hello_world")),
    CLEAR("clear", CommandActions::clear, Translations.get("command.clear")),
    TIME("time", CommandActions::time, Translations.get("command.time")),
    HELP("help", CommandActions::help, Translations.get("command.help")),
    ECHO("echo", CommandActions::echo, Translations.get("command.echo")),
    TAB("tab", CommandActions::tab, Translations.get("command.tab")),
    SOURCE("code-source", CommandActions::sourceCode, Translations.get("command.source")),
    ASCII("ascii", CommandActions::writeAscii, Translations.get("command.ascii")),
    QUIT("quit", CommandActions::quit, Translations.get("command.exit")),

    HIDDEN("hide", CommandActions::hidden, true),
    NOPE("nop", CommandActions::nope, true);

    final String key;
    final Consumer<List<String>> commandOutput;
    final Consumer<String> commandhelp;
    final String commandDesc;
    final boolean isHidden;

    Commands(String key, Consumer<List<String>> commandOutput, Consumer<String> commandHelp, String commandDesc, boolean isHidden) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandDesc = commandDesc;
        this.commandhelp = commandHelp;
        this.isHidden = isHidden;
    }
    Commands(String key, Consumer<List<String>> commandOutput, boolean isHidden) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandDesc = null;
        this.isHidden = isHidden;
    }
    Commands(String key, Consumer<List<String>> commandOutput, String commandDesc) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandDesc = commandDesc;
        this.isHidden = false;
    }

    public static boolean containsKey(String pKey) {
        for (Commands command : Commands.values()) {
            if (pKey.equals(command.getKey())) return true;
        }
        return false;
    }
    public static Commands getCommandWithKey(String pKey) {
        for (Commands command : Commands.values()) {
            if (pKey.equals(command.getKey())) return command;
        }
        return null;
    }

    public String getKey() {
        return key;
    }
    public Consumer<List<String>> getCommandOutput() {
        return commandOutput;
    }
}
