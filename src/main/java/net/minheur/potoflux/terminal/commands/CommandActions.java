package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.screen.tabs.TabRegistry;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.terminal.Command;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.terminal.CommandRegistry;
import net.minheur.potoflux.terminal.Terminal;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static net.minheur.potoflux.terminal.commands.CommandMakerHelpers.*;

/**
 * The class containing all potoflux command actions
 */
public class CommandActions {
    /**
     * Command execution for command {@code hello}.
     * @param args all the args given to the command
     */
    static void helloWorld(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.helloWorld());
            return;
        }

        CommandProcessor.appendOutput("Hello world !");
    }

    /**
     * Command execution for command {@code time}.
     * @param args all the args given to the command
     */
    static void time(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.time());
            return;
        }

        CommandProcessor.appendOutput(LocalTime.now().toString());
    }

    /**
     * Command execution for command {@code help}.
     * @param args all the args given to the command
     */
    static void help(List<String> args) {
        if (argAmountCheck(0, 1, args)) {
            CommandProcessor.appendOutput(CommandHelp.help());
            return;
        }

        if (args.isEmpty()) {
            StringBuilder out = new StringBuilder(Translations.get("potoflux:command.help.title"));
            for (Command command : CommandRegistry.getAll()) {
                if (!command.hidden()) {
                    out.append("\n").append("→ ").append(command.key()).append(" : ").append(command.commandHelp());
                }
            }
            CommandProcessor.appendOutput(out.toString());
            return;
        } else {
            String commandHelp = args.get(0);
            for (Command command : CommandRegistry.getAll()) {
                if (commandHelp.equals(command.key())) {
                    CommandProcessor.appendOutput(command.commandHelp());
                    return;
                }
            }
        }
        CommandProcessor.appendOutput(Commands.INSTANCE.HELP.commandHelp());
    }

    /**
     * Command execution for command {@code source}.
     * @param args all the args given to the command
     */
    static void sourceCode(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.source());
            return;
        }

        String url = "https://github.com/TechnoMastery/PotoFlux";
        boolean hasBrowse = Functions.browse(url);

        if (hasBrowse)
            CommandProcessor.appendOutput(Translations.get("potoflux:command.source.out"));
        else {
            CommandProcessor.appendOutput(Translations.get("potoflux:command.source.noBrowse"));
            CommandProcessor.appendOutput(url);
        }
    }

    /**
     * Command execution for command {@code quit}.
     * @param args all the args given to the command
     */
    static void quit(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.quit());
            return;
        }

        CommandProcessor.appendOutput(Translations.get("potoflux:command.quit.out"));
        Functions.exit(500, 0);
    }

    /**
     * Command execution for command {@code echo}.
     * @param args all the args given to the command
     */
    static void echo(List<String> args) {
        if (argAmountCheck(1, args)) {
            CommandProcessor.appendOutput(CommandHelp.echo());
            return;
        }

        String message = String.join(" ", args);
        CommandProcessor.appendOutput(message);
    }

    /**
     * Command execution for command {@code tab}.
     * @param args all the args given to the command
     */
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

    /**
     * Command execution for command {@code clear}.
     * @param args all the args given to the command
     */
    static void clear(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.clear());
            return;
        }

        CommandProcessor.clearArea();
    }

    /**
     * Command execution for command {@code ascii}.
     * @param args all the args given to the command
     */
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

    /**
     * Command execution for command {@code modList}.
     * @param args all the args given to the command
     */
    static void modList(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.modList());
            return;
        }

        List<String> modIds = PotoFluxLoadingContext.getLoadedMods();

        if (modIds.isEmpty()) {
            CommandProcessor.appendOutput("No mods loaded !");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the loaded mods :");

        for (String modId : modIds) {
            sb.append("\n   → ").append(modId);
        }

        CommandProcessor.appendOutput(sb.toString());
    }

    /**
     * Command execution for command {@code modDir}.
     * @param args all the args given to the command
     */
    static void modDir(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.modDir());
            return;
        }

        File target = PotoFlux.getProgramDir().resolve("mods").toFile();

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(target);
                CommandProcessor.appendOutput("Opened in file explorer !");
            } else {
                CommandProcessor.appendOutput("Could not get desktop !");
            }
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not open mods folder !", LogCategories.TERMINAL, "command");
            CommandProcessor.appendOutput("Could not get desktop !");
        }

    }

    /**
     * Command execution for command {@code ptfVersion}
     * @param args all the args given to the command
     */
    static void ptfVersion(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendOutput(CommandHelp.ptfVersion());
            return;
        }

        String version = PotoFlux.getVersion();
        if (version == null)
            CommandProcessor.appendOutput("Could not get potoflux version !");

        else CommandProcessor.appendOutput("PotoFlux version: " + version);
    }

    /**
     * Command execution for command {@code hide}.
     * @param args all the args given to the command
     */
    static void hidden(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendNoCommand();
            return;
        }

        CommandProcessor.appendOutput(Translations.get("potoflux:command.hidden.out"));
    }

    /**
     * Command execution for command {@code nope}.
     * @param args all the args given to the command
     */
    static void nope(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendNoCommand();
            return;
        }

        String url = "https://rickroll.it/rickroll.mp4"; // don't worry ^^
        boolean hasBrowse = Functions.browse(url);

        if (hasBrowse)
            CommandProcessor.appendOutput(Translations.get("potoflux:command.nope.out"));
        else
            CommandProcessor.appendNoCommand();

    }

    /**
     * Command execution for command {@code LordHawima}
     * @param args all the args given to the command
     */
    static void lordHawima(List<String> args) {
        if (checkNoArgs(args)) {
            CommandProcessor.appendNoCommand();
            return;
        }

        CommandProcessor.appendOutput("!!!!!!!!!!!!!! Les patates c'est BON !!!!!!!!!!!!!!");
        CommandProcessor.appendOutput("Cet easter egg vous est offert par Magaco");
    }

}
