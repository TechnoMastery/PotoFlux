package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.terminal.commands.Commands;
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
        addCommand(Commands.INSTANCE.ASCII)
                .en("Show an ASCII");
        addCommandUse(Commands.INSTANCE.ASCII)
                .en("Usage: ascii");
        addCommandUse(Commands.INSTANCE.ASCII,"choose")
                .en("Usage: ascii <name>");

        addCommand(Commands.INSTANCE.CLEAR)
                .en("Clears the terminal");
        addCommandUse(Commands.INSTANCE.CLEAR)
                .en("Usage: clear");

        addCommand(Commands.INSTANCE.VERSION)
                .en("Get actual PotoFlux version");
        addCommandUse(Commands.INSTANCE.VERSION)
                .en("Usage: version");

        addCommand(Commands.INSTANCE.ECHO)
                .en("Repeats what you give");
        addCommandUse(Commands.INSTANCE.ECHO)
                .en("Usage: echo <text>");

        addCommand(Commands.INSTANCE.HELLO_WORLD)
                .en("Says hello world");
        addCommandUse(Commands.INSTANCE.ECHO)
                .en("Usage: hello");

        addCommand(Commands.INSTANCE.HELP)
                .en("Shows this menu");
        addCommand(Commands.INSTANCE.HELP, "title")
                .en("Command list:");
        addCommandUse(Commands.INSTANCE.HELP)
                .en("Usage: help");
        addCommandUse(Commands.INSTANCE.HELP, "command")
                .en("Usage: help <command>");

        addCommand(Commands.INSTANCE.HIDDEN, "out")
                .en("Hidden command :)");

        addCommand(Commands.INSTANCE.NOPE, "out")
                .en("I got you !")
                .fr("CHEH");

        addCommand(Commands.INSTANCE.QUIT)
                .en("Exits app");
        addCommand(Commands.INSTANCE.QUIT, "out")
                .en("Exiting...");
        addCommandUse(Commands.INSTANCE.QUIT)
                .en("Usage: quit");

        addCommand(Commands.INSTANCE.SOURCE)
                .en("Link to source code");
        addCommand(Commands.INSTANCE.SOURCE, "out")
                .en("Opened code source in browser !");
        addCommand(Commands.INSTANCE.SOURCE, "noBrowse")
                .en("Could not open source code ! Here is the link:");
        addCommandUse(Commands.INSTANCE.SOURCE)
                .en("Usage: source-code");

        addCommand(Commands.INSTANCE.TAB)
                .en("Open given tab");
        addCommand(Commands.INSTANCE.TAB, "null")
                .en("Tab $$1 doesn't exists !")
                .fr("L'onglet $$1 n'existe pas !");
        addCommand(Commands.INSTANCE.TAB, "opened")
                .en("This tab is already opened !");
        addCommandUse(Commands.INSTANCE.TAB)
                .en("Usage: tab <tabResourceLoc>");

        addCommand(Commands.INSTANCE.TAB_LIST)
                .en("Lists all the tabs")
                .fr("Liste touts les onglets");
        addCommand(Commands.INSTANCE.TAB_LIST, "intro")
                .en("Here is all the tabs: ")
                .fr("Voici touts les onglets : ");
        addCommandUse(Commands.INSTANCE.TAB_LIST)
                .en("Usage: tabList")
                .fr("Utilisation : tabList");
        addCommandUse(Commands.INSTANCE.TAB_LIST, "resourceLoc")
                .en("Usage: tabList --resourceLoc")
                .fr("Utilisation : tabList --resourceLoc");

        addCommand(Commands.INSTANCE.TIME)
                .en("Tells the time");
        addCommandUse(Commands.INSTANCE.TIME)
                .en("Usage: time");

        addCommand(Commands.INSTANCE.MOD_LIST)
                .en("Lists you loaded mods");
        addCommandUse(Commands.INSTANCE.MOD_LIST)
                .en("Usage: modList");

        addCommand(Commands.INSTANCE.MOD_DIR)
                .en("Open mod directory");
        addCommandUse(Commands.INSTANCE.MOD_DIR)
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
                .en("Created by Min_heur2000 - TechnoMastery")
                .fr("Créé par Min_heur2000 - TechnoMastery");
        addHomeTab("name")
                .en("Home")
                .fr("Home");
        addHomeTab("title")
                .en("Potoflux - Home")
                .fr("Potoflux - Home");
        addHomeTab("version")
                .en("PotoFlux in version $$1")
                .fr("PotoFlux en version $$1");

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
