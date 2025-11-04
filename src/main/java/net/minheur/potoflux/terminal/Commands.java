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
    HELLO_WORLD("hello", Commands::helloWorld, Translations.get("command.hello_world")),
    CLEAR("clear", Commands::clear, Translations.get("command.clear")),
    TIME("time", Commands::time, Translations.get("command.time")),
    HELP("help", Commands::help, Translations.get("command.help")),
    ECHO("echo", Commands::echo, Translations.get("command.echo")),
    TAB("tab", Commands::tab, Translations.get("command.tab")),
    SOURCE("code-source", Commands::sourceCode, Translations.get("command.source")),
    QUIT("quit", Commands::quit, Translations.get("command.exit")),

    HIDDEN("hide", Commands::hidden, true),
    NOPE("nop", Commands::nope, true);

    private final String key;
    private final Consumer<List<String>> commandOutput;
    private final String commandDesc;
    private final boolean isHidden;

    Commands(String key, Consumer<List<String>> commandOutput, String commandDesc, boolean isHidden) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandDesc = commandDesc;
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

    // define returns
    private static void helloWorld(List<String> args) {
        CommandProcessor.appendOutput("Hello world !");
    }
    private static void time(List<String> args) {
        CommandProcessor.appendOutput(LocalTime.now().toString());
    }
    private static void help(List<String> args) {
        StringBuilder out = new StringBuilder(Translations.get("command.help.title"));
        for (Commands command : Commands.values()) {
            if (!command.isHidden) out.append("\n").append("→ ").append(command.key).append(" : ").append(command.commandDesc);
        }
        CommandProcessor.appendOutput(out.toString());
    }
    private static void sourceCode(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.source.out") + "https://github.com/TechnoMastery/PotoFlux");
    }
    private static void quit(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.exit.out"));
        Functions.exit(500, 0);
    }
    private static void echo(List<String> args) {
        if (args.isEmpty()) {
            CommandProcessor.appendOutput(Translations.get("command.echo.use"));
            return;
        }
        String message = String.join(" ", args);
        CommandProcessor.appendOutput(message);
    }
    private static void tab(List<String> args) {
        if (args.isEmpty()) {
            CommandProcessor.appendOutput(Translations.get("command.tab.use"));
            return;
        }
        if (args.get(0).equals("Terminal")) {
            CommandProcessor.appendOutput(Translations.get("command.tab.opened"));
            return;
        }
        for (Tabs tab : Tabs.values()) {
            if (Objects.equals(tab.getName(), args.get(0))) {
                PotoFlux.app.setOpenedTab(tab);
                return;
            }
        }
        CommandProcessor.appendOutput(Translations.get("command.tab.use"));
        CommandProcessor.appendOutput(Translations.get("command.tab.null.start") + args.get(0) + Translations.get("command.tab.null.end"));
    }
    private static void clear(List<String> args) {
        CommandProcessor.clearArea();
    }

    private static void hidden(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.hidden.out"));
    }
    private static void nope(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.nope.out"));
    }
}
