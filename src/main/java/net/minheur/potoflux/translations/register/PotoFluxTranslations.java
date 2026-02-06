package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

/**
 * Registry for all potoflux translations
 */
public class PotoFluxTranslations extends AbstractTranslationsRegistry {
    /**
     * Calls the constructor to set the modID
     */
    public PotoFluxTranslations() {
        super(PotoFlux.ID);
    }

    /**
     * Actually add translation
     */
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

        addCommand("ptfVersion")
                .en("Get actual PotoFlux version");
        addCommandUse("ptfVersion")
                .en("Usage: version");

        addCommand("echo")
                .en("Repeats what you give");
        addCommandUse("echo")
                .en("Usage: echo <text>");

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
                .en("I got you !")
                .fr("CHEH");

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

        addCommand("modList")
                .en("Lists you loaded mods");
        addCommandUse("modList")
                .en("Usage: modList");

        addCommand("modDir")
                .en("Open mod directory");
        addCommandUse("modDir")
                .en("Usage: modDir");

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
        addPref("theme")
                .en("Theme")
                .fr("Thème");
        addPref("theme", "select")
                .en("Select new theme")
                .fr("Sélectionnez le nouveau thème");
        addPref("theme", "button")
                .en("Change theme")
                .fr("Changer le thème");

        addScreen("tabHereNotHere")
                .en("ERROR: this tab is detected but unexisting !");

        addHomeTab("credit")
                .en("Created by Min_heur2000 - TechnoMastery");
        addHomeTab("name")
                .en("home");
        addHomeTab("title")
                .en("Potoflux - Home");
        addHomeTab("version")
                .en("PotoFlux in version $$1")
                .fr("PotoFlux version $$1");

        addSettingsTab("name")
                .en("Settings");
        addSettingsTab("title")
                .en("Settings");

        addTerminalTab("name")
                .en("Terminal");
        addTerminalTab("title")
                .en("Terminal");

        addCatalogTab("name")
                .en("Catalog")
                .fr("Catalogue");
    }

    /**
     * Add a translation for the command processor
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addCommandPro(String... children) {
        return add("commandPro", children);
    }
    /**
     * Add a translation for the preferences
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addPref(String... children) {
        return add("prefs", children);
    }
    /**
     * Add a translation for the screen
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addScreen(String... children) {
        return add("screen", children);
    }

    // tabs helpers
    /**
     * Add a translation for the home tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addHomeTab(String... children) {
        return addTab("home", children);
    }
    /**
     * Add a translation for the settings tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addSettingsTab(String... children) {
        return addTab("settings", children);
    }
    /**
     * Add a translation for the terminal tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addTerminalTab(String... children) {
        return addTab("term", children);
    }
    /**
     * Add a translation for the catalog tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addCatalogTab(String... children) {
        return addTab("catalog", children);
    }
}
