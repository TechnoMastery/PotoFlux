package net.minheur.potoflux.terminal;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.utils.Translations;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class CommandActions {
    static void helloWorld(List<String> args) {
        CommandProcessor.appendOutput("Hello world !");
    }

    static void time(List<String> args) {
        CommandProcessor.appendOutput(LocalTime.now().toString());
    }

    static void help(List<String> args) {
        StringBuilder out = new StringBuilder(Translations.get("command.help.title"));
        for (Commands command : Commands.values()) {
            if (!command.isHidden) {
                out.append("\n").append("→ ").append(command.key).append(" : ").append(command.commandHelp);
            }
        }
        CommandProcessor.appendOutput(out.toString());
    }
    static void sourceCode(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.source.out") + "https://github.com/TechnoMastery/PotoFlux");
    }

    static void quit(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.exit.out"));
        Functions.exit(500, 0);
    }

    static void echo(List<String> args) {
        if (args.isEmpty()) {
            CommandProcessor.appendOutput(Translations.get("command.echo.use"));
            return;
        }
        String message = String.join(" ", args);
        CommandProcessor.appendOutput(message);
    }

    static void tab(List<String> args) {
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

    static void clear(List<String> args) {
        CommandProcessor.clearArea();
    }

    static void writeAscii(List<String> args) {
        if (args.isEmpty()) {
            Terminal.buildASCII();
            return;
        }
        String ascii = args.get(0);

        String content = Terminal.getAsciiFileContent(ascii);
        if (content == null)
            CommandProcessor.appendOutput(Translations.get("command.ascii.use"));
        else CommandProcessor.appendOutput(content);
    }

    static void hidden(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.hidden.out"));
    }

    static void nope(List<String> args) {
        CommandProcessor.appendOutput(Translations.get("command.nope.out"));
    }

}
