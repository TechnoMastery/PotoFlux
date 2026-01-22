package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.TabRegistry;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.terminal.Command;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.terminal.CommandRegistry;
import net.minheur.potoflux.terminal.Terminal;
import net.minheur.potoflux.translations.Translations;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class CommandActions {
    static void helloWorld(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.helloWorld());
            return;
        }

        CommandProcessor.appendOutput("Hello world !");
    }

    static void time(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.time());
            return;
        }

        CommandProcessor.appendOutput(LocalTime.now().toString());
    }

    static void help(List<String> args) {
        if (argAmountCheck(0, 1, args)) {
            CommandProcessor.appendOutput(CommandHelp.help());
            return;
        }

        if (args.isEmpty()) {
            StringBuilder out = new StringBuilder(Translations.get("potoflux:command.help.title"));
            for (Command command : CommandRegistry.getAll()) {
                if (!command.isHidden()) {
                    out.append("\n").append("→ ").append(command.getKey()).append(" : ").append(command.getCommandHelp());
                }
            }
            CommandProcessor.appendOutput(out.toString());
            return;
        } else {
            String commandHelp = args.get(0);
            for (Command command : CommandRegistry.getAll()) {
                if (commandHelp.equals(command.getKey())) {
                    CommandProcessor.appendOutput(command.getCommandHelp());
                    return;
                }
            }
        }
        CommandProcessor.appendOutput(Commands.INSTANCE.HELP.getCommandHelp());
    }
    static void sourceCode(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.source());
            return;
        }

        CommandProcessor.appendOutput(Translations.get("potoflux:command.source.out") + "https://github.com/TechnoMastery/PotoFlux");
    }

    static void quit(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.quit());
            return;
        }

        CommandProcessor.appendOutput(Translations.get("potoflux:command.quit.out"));
        Functions.exit(500, 0);
    }

    static void echo(List<String> args) {
        if (argAmountCheck(1, args)) {
            CommandProcessor.appendOutput(CommandHelp.echo());
            return;
        }

        String message = String.join(" ", args);
        CommandProcessor.appendOutput(message);
    }

    static void tab(List<String> args) {
        if (argAmountCheck(1, args)) {
            CommandProcessor.appendOutput(CommandHelp.tab());
            return;
        }

        if (args.get(0).equals("Terminal")) {
            CommandProcessor.appendOutput(Translations.get("potoflux:command.tab.opened"));
        }
        for (Tab tab : TabRegistry.getAll()) {
            if (Objects.equals(tab.name(), args.get(0))) {
                PotoFlux.app.setOpenedTab(tab);
                return;
            }
        }
        CommandProcessor.appendOutput(CommandHelp.tab());
        CommandProcessor.appendOutput(Translations.get("potoflux:command.tab.null.start") + args.get(0) + Translations.get("potofluxcommand.tab.null.end"));
    }

    static void clear(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.clear());
            return;
        }

        CommandProcessor.clearArea();
    }

    static void writeAscii(List<String> args) {
        if (argAmountCheck(0, 1, args)) {
            CommandProcessor.appendOutput(CommandHelp.ascii());
            return;
        }

        if (args.isEmpty()) {
            Terminal.buildASCII();
            return;
        }
        String ascii = args.get(0);

        String content = Terminal.getAsciiFileContent(ascii);
        CommandProcessor.appendOutput(Objects.requireNonNullElseGet(content, CommandHelp::ascii));
    }

    static void hidden(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendNoCommand();
            return;
        }

        CommandProcessor.appendOutput(Translations.get("potoflux:command.hidden.out"));
    }

    static void nope(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendNoCommand();
            return;
        }

        CommandProcessor.appendOutput(Translations.get("potoflux:command.nope.out"));
    }

    // helper methods
    private static boolean argAmountCheck(int min, int max, List<String> args) {
        int actual = args.size();
        return actual < min || actual > max;
    }
    private static boolean argAmountCheck(int amount, List<String> args) {
        int actual = args.size();
        return actual != amount;
    }
    private static boolean argAmountCheck(List<String> args, int... allowed) {
        int actual = args.size();
        for (int a : allowed) if (a == actual) return false;
        return true;
    }
    private static boolean checkNoArgs(List<String> args) {
        return !args.isEmpty();
    }

}
