package net.minheur.potoflux.terminal;

import net.minheur.potoflux.Functions;

import java.time.LocalTime;

public enum Commands {
    HELLO_WORLD("hello", Commands::helloWorld, "Says hello world !"),
    TIME("time", Commands::time, "Tells actual time"),
    HELP("help", Commands::help, "Shows this menu"),
    QUIT("quit", Commands::quit, "Exit app");

    private final String key;
    private final Runnable commandOutput;
    private final String commandDesc;

    Commands(String key, Runnable commandOutput, String commandDesc) {
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
    public Runnable getCommandOutput() {
        return commandOutput;
    }

    // define returns
    private static void helloWorld() {
        CommandProcessor.appendOutput("Hello world !");
    }
    private static void time() {
        CommandProcessor.appendOutput(LocalTime.now().toString());
    }
    private static void help() {
        StringBuilder out = new StringBuilder("Command list :");
        for (Commands command : Commands.values()) {
            out.append("\n").append("→ ").append(command.key).append(" : ").append(command.commandDesc);
        }
        CommandProcessor.appendOutput(out.toString());
    }
    private static void quit() {
        CommandProcessor.appendOutput("Exiting...");
        Functions.exit(500, 0);
    }
}
