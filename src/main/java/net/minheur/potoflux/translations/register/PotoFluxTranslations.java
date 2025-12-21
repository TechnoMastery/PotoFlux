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
        addCommandUse("source")
                .en("Usage: source-code");

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
        addPref("ascii", "button")
                .en("change terminal ASCII");
        addPref("lang")
                .en("Language");
        addPref("lang", "select")
                .en("Select language");
        addPref("lang", "button")
                .en("Change language");

        addScreen("tabHereNotHere")
                .en("ERROR: this tab is detected but unexisting !");

        addCardTab("name")
                .en("Card learning");
        addCardTab("add_card")
                .en("Add card");
        addCardTab("available_lists")
                .en("Available lists");
        addCardTab("cancel_all")
                .en("Cancel all");
        addCardTab("card_number")
                .en("Number of cards: ");
        addCardTab("choose_list")
                .en("Choose list");
        addCardTab("delete_error")
                .en("Error while deleting file: ");
        addCardTab("details")
                .en("Details of ");
        addCardTab("empty_list_valid")
                .en("Empty list but enabled button !");
        addCardTab("error", "loading_list")
                .en("Error while loading list.");
        addCardTab("error", "reading_file")
                .en("Error while reading file");
        addCardTab("error", "saving")
                .en("Error while saving");
        addCardTab("export")
                .en("Export list");
        addCardTab("export", "error")
                .en("Error while exporting: ");
        addCardTab("export", "done")
                .en("List exported successfully in: ");
        addCardTab("export", "done", "name")
                .en("Exported successfully");
        addCardTab("face", "front")
                .en("Front: ");
        addCardTab("face", "back")
                .en("Back: ");
        addCardTab("list", "invalid")
                .en("Invalid or empty list");
        addCardTab("list", "invalid", "name")
                .en("This list's name is invalid.");
        addCardTab("list", "column")
                .en("List: ");
        addCardTab("list", "loaded")
                .en("List loaded: ");
        addCardTab("list", "name")
                .en("List name: ");
        addCardTab("list", "delete", "confirm")
                .en("List delection check");
        addCardTab("list", "delete", "confirm", "dialog")
                .en("Do you really want to delete the list ");
        addCardTab("list", "delete", "done", "start")
                .en("The list ");
        addCardTab("list", "delete", "done", "end")
                .en(" has been deleted.");
        addCardTab("list", "saved")
                .en("List saved successfully");
        addCardTab("list", "no_found")
                .en("No lists found.");
        addCardTab("new")
                .en("New card");
        addCardTab("new", "empty")
                .en("Both input should be filled correctly.");
        addCardTab("no_card")
                .en("No cards added");
        addCardTab("no_selected")
                .en("No card selected.");
        addCardTab("override")
                .en("Do you want to override the cards ?");
        addCardTab("createError")
                .en("ERROR while creating tab ");

        addHomeTab("credit")
                .en("Created by Min_heur2000 - TechnoMastery");
        addHomeTab("name")
                .en("home");
        addHomeTab("title")
                .en("Potoflux - Home");

        addSettingsTab("name")
                .en("Settings");
        addSettingsTab("title")
                .en("Settings");

        addTerminalTab("name")
                .en("Terminal");
        addTerminalTab("title")
                .en("Terminal");
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

    // tabs helpers
    private TranslationBuilder addCardTab(String... children) {
        return addTab("card", children);
    }
    private TranslationBuilder addHomeTab(String... children) {
        return addTab("home", children);
    }
    private TranslationBuilder addSettingsTab(String... children) {
        return addTab("settings", children);
    }
    private TranslationBuilder addTerminalTab(String... children) {
        return addTab("term", children);
    }
}
