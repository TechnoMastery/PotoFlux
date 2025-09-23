package net.minheur.potoflux.terminal;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public enum Commands {
    HELLO_WORLD("hello", Commands::helloWorld, "Says hello world !"),
    TIME("time", Commands::time, "Tells actual time"),
    HELP("help", Commands::help, "Shows this menu"),
    ECHO("echo", Commands::echo, "Repeats what you give"),
    TAB("tab", Commands::tab, "Ouvre l'onglet indiqué"),
    QUIT("quit", Commands::quit, "Exit app");

    private final String key;
    private final Consumer<List<String>> commandOutput;
    private final String commandDesc;

    Commands(String key, Consumer<List<String>> commandOutput, String commandDesc) {
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandDesc = commandDesc;
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
        StringBuilder out = new StringBuilder("Command list :");
        for (Commands command : Commands.values()) {
            out.append("\n").append("→ ").append(command.key).append(" : ").append(command.commandDesc);
        }
        CommandProcessor.appendOutput(out.toString());
    }
    private static void quit(List<String> args) {
        CommandProcessor.appendOutput("Exiting...");
        Functions.exit(500, 0);
    }
    private static void echo(List<String> args) {
        if (args.isEmpty()) {
            CommandProcessor.appendOutput("Usage: echo <text>");
            return;
        }
        String message = String.join(" ", args);
        CommandProcessor.appendOutput(message);
    }
    private static void tab(List<String> args) {
        if (args.isEmpty()) {
            CommandProcessor.appendOutput("Usage: tab <tabName>");
            return;
        }
        if (args.get(0).equals("Terminal")) {
            CommandProcessor.appendOutput("Cette tab est déjà ouverte !");
            return;
        }
        for (Tabs tab : Tabs.values()) {
            if (Objects.equals(tab.getName(), args.get(0))) {
                PotoFlux.app.setOpenedTab(tab);
                return;
            }
        }
        CommandProcessor.appendOutput("Usage: tab <tabName>");
        CommandProcessor.appendOutput("Tab " + args.get(0) + " doesn't exist !");
    }
}
