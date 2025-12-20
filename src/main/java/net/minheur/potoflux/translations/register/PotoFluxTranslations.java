package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

public class PotoFluxTranslations extends AbstractTranslationsRegistry {
    public PotoFluxTranslations() {
        super(PotoFlux.ID);
    }

    @Override
    protected void makeTranslation() {
        // commands
        addCommand("ascii")
                .en("Show an ASCII");
        addCommandUse("ascii")
                .en("Usage: ascii");
        addCommandUse("ascii","choose")
                .en("Usage: ascii <name>");

        addCommand("clear")
                .en("Clears the terminal");
        addCommandUse("clear")
                .en("Usage: clear");

        addCommand("echo")
                .en("Repeats what you give");
        addCommandUse("echo")
                .en("Echo: usage <text>");

        addCommand("hello_world")
                .en("Says hello world");
        addCommandUse("hello_world")
                .en("Usage: hello");

        addCommand("help")
                .en("Shows this menu");
        addCommand("help", "title")
                .en("Command list:");
        addCommandUse("help")
                .en("Usage: help");
        addCommandUse("help", "command")
                .en("Usage: help <command>");

        addCommand("hidden", "out")
                .en("Hidden command :)");

        addCommand("nope", "out")
                .en("NOPE !!!!! :)");

        addCommand("quit")
                .en("Exit app");
        addCommand("quit", "out")
                .en("Exiting...");
        addCommandUse("quit")
                .en("Usage: quit");

        addCommand("source")
                .en("Link to source code");
        addCommand("source", "out")
                .en("Source code availble at ");
        addCommandUse("Usage: source-code");

        addCommand("tab")
                .en("Open given tab");
        addCommand("tab", "null", "start")
                .en("Tab ");
        addCommand("tab", "null", "end")
                .en(" doesn't exists !");
        addCommand("tab", "opened")
                .en("This tab is already opened !");
        addCommandUse("tab")
                .en("Usage: tab <tabName>");

        addCommand("time")
                .en("Tells the time");
        addCommandUse("time")
                .en("Usage: time");

        addCommandPro("empty")
                .en("Could not execute empty command !");
        addCommandPro("none")
                .en("Command not recognized ! Try \"help\" !");

        addPref("reload")
                .en("Please restart PotoFlux to apply.");
        addPref("ascii")
                .en("ASCII");
        addPref("ascii", "select")
                .en("Select terminal ASCII");
        addPref("lang")
                .en("Language");
        addPref("lang", "select")
                .en("Select language");

        addScreen("tabHereNotHere")
                .en("ERROR: this tab is detected but unexisting !");

        addTab("card"); // TODO
    }

    private TranslationBuilder addCommandPro(String... children) {
        return add("commandPro", children);
    }
    private TranslationBuilder addPref(String... children) {
        return add("prefs", children);
    }
    private TranslationBuilder addScreen(String... children) {
        return add("screen", children);
    }
}
