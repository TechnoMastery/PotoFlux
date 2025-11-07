package net.minheur.potoflux.terminal;

import java.util.List;
import java.util.function.Consumer;

public enum Commands {
    HELLO_WORLD("hello", CommandActions::helloWorld, CommandHelp.helloWorld()),
    CLEAR("clear", CommandActions::clear, CommandHelp.clear()),
    TIME("time", CommandActions::time, CommandHelp.time()),
    HELP("help", CommandActions::help, CommandHelp.help()),
    ECHO("echo", CommandActions::echo, CommandHelp.echo()),
    TAB("tab", CommandActions::tab, CommandHelp.tab()),
    SOURCE("code-source", CommandActions::sourceCode, CommandHelp.source()),
    ASCII("ascii", CommandActions::writeAscii, CommandHelp.ascii()),
    QUIT("quit", CommandActions::quit, CommandHelp.quit()),

    HIDDEN("hide", CommandActions::hidden, true),
    NOPE("nop", CommandActions::nope, true);

    final String key;
    final Consumer<List<String>> commandOutput;
    final String commandHelp;
    final boolean isHidden;

    Commands(String key, Consumer<List<String>> commandOutput, String commandHelp, boolean isHidden) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandHelp = commandHelp;
        this.isHidden = isHidden;
    }
    Commands(String key, Consumer<List<String>> commandOutput, boolean isHidden) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.isHidden = isHidden;
        this.commandHelp = null;
    }
    Commands(String key, Consumer<List<String>> commandOutput, String commandHelp) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.isHidden = false;
        this.commandHelp = commandHelp;
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
