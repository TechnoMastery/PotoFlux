package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
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
                .en("Show an ASCII")
                .fr("Affiche un ASCII");
        addCommandUse(Commands.INSTANCE.ASCII)
                .en("Usage: ascii")
                .fr("Utilisation : ascii");
        addCommandUse(Commands.INSTANCE.ASCII,"choose")
                .en("Usage: ascii <name>")
                .fr("Utilisation : ascii <nom>");

        addCommand(Commands.INSTANCE.CLEAR)
                .en("Clears the terminal")
                .fr("Vide le terminal");
        addCommandUse(Commands.INSTANCE.CLEAR)
                .en("Usage: clear")
                .fr("Utilisation : clear");

        addCommand(Commands.INSTANCE.VERSION)
                .en("Get actual PotoFlux version")
                .fr("Affiche la version actuelle de PotoFLux");
        addCommandUse(Commands.INSTANCE.VERSION)
                .en("Usage: version")
                .fr("Utilisation : version");

        addCommand(Commands.INSTANCE.ECHO)
                .en("Repeats what you give")
                .fr("Répète ce que vous écrivez");
        addCommandUse(Commands.INSTANCE.ECHO)
                .en("Usage: echo <text>")
                .fr("Utilisation : echo <texte>");

        addCommand(Commands.INSTANCE.HELLO_WORLD)
                .en("Says hello world")
                .fr("Dit hello world");
        addCommandUse(Commands.INSTANCE.ECHO)
                .en("Usage: hello")
                .fr("Utilisation : hello");

        addCommand(Commands.INSTANCE.HELP)
                .en("Shows this menu")
                .fr("Affiche ce menu");
        addCommand(Commands.INSTANCE.HELP, "title")
                .en("Command list:")
                .fr("Liste des commandes :");
        addCommandUse(Commands.INSTANCE.HELP)
                .en("Usage: help")
                .fr("Utilisation : help");
        addCommandUse(Commands.INSTANCE.HELP, "command")
                .en("Usage: help <command>")
                .fr("Utilisation : help <commande>");

        addCommand(Commands.INSTANCE.HIDDEN, "out")
                .en("Hidden command :)")
                .fr("Commande caché ;)");

        addCommand(Commands.INSTANCE.NOPE, "out")
                .en("I got you !")
                .fr("CHEH");

        addCommand(Commands.INSTANCE.QUIT)
                .en("Exits app")
                .fr("Ferme l'app");
        addCommand(Commands.INSTANCE.QUIT, "out")
                .en("Exiting...")
                .fr("Fermeture...");
        addCommandUse(Commands.INSTANCE.QUIT)
                .en("Usage: quit")
                .fr("Utilisation : quit");

        addCommand(Commands.INSTANCE.SOURCE)
                .en("Link to source code")
                .fr("Lien vers le code source");
        addCommand(Commands.INSTANCE.SOURCE, "out")
                .en("Opened code source in browser !")
                .fr("Code source ouvert dans le navigateur !");
        addCommand(Commands.INSTANCE.SOURCE, "noBrowse")
                .en("Could not open source code ! Here is the link:")
                .fr("Impossible d'ouvrir le code source dans le navigateur. Voici le lien :");
        addCommandUse(Commands.INSTANCE.SOURCE)
                .en("Usage: source-code")
                .fr("Utilisation : source-code");

        addCommand(Commands.INSTANCE.TAB)
                .en("Open given tab")
                .fr("Ouvre l'onglet donné");
        addCommand(Commands.INSTANCE.TAB, "null")
                .en("Tab $$1 doesn't exists !")
                .fr("L'onglet $$1 n'existe pas !");
        addCommand(Commands.INSTANCE.TAB, "opened")
                .en("This tab is already opened !")
                .fr("Cet onglet est déjà ouvert !");
        addCommandUse(Commands.INSTANCE.TAB)
                .en("Usage: tab <tabResourceLoc>")
                .fr("Utilisation : tab <ressourceLocDeLaTab>");

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
                .en("Tells the time")
                .fr("Dit le temps (très précis)");
        addCommandUse(Commands.INSTANCE.TIME)
                .en("Usage: time")
                .fr("Utilisation : time");

        addCommand(Commands.INSTANCE.MOD_LIST)
                .en("Lists you loaded mods")
                .fr("Liste tous les mods chargé (activés)");
        addCommandUse(Commands.INSTANCE.MOD_LIST)
                .en("Usage: modList")
                .fr("Utilisation : modList");

        addCommand(Commands.INSTANCE.MOD_DIR)
                .en("Open mod directory")
                .fr("Ouvre le dossier des mods");
        addCommandUse(Commands.INSTANCE.MOD_DIR)
                .en("Usage: modDir")
                .fr("Utilisation : modDir");

        addCommandPro("empty")
                .en("Could not execute empty command !")
                .fr("Impossible d'exécuter une commande vide !");
        addCommandPro("none")
                .en("Command not recognized ! Try \"help\" !")
                .fr("Commande non reconnu ! Essayez \"help\" !");

        addPref("reload")
                .en("Please restart PotoFlux to apply.")
                .fr("Merci de redémarrer PotoFlux, afin d'appliquer.");
        addPref("ascii")
                .en("ASCII")
                .fr("ASCII");
        addPref("ascii", "select")
                .en("Select terminal ASCII")
                .fr("Choisissez un ASCII du terminal");
        addPref("ascii", "button")
                .en("Change terminal ASCII")
                .fr("Changer l'ASCII du terminal");
        addPref("lang")
                .en("Language")
                .fr("Langue");
        addPref("lang", "select")
                .en("Select language")
                .fr("Sélectionnez la langue");
        addPref("lang", "button")
                .en("Change language")
                .fr("Changer la langue");
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
                .en("ERROR: this tab is detected but unexisting !")
                .fr("Erreur : cette onglet est détecté mais inéxistante !");

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
                .en("Settings")
                .fr("Paramètres");

        addTerminalTab("name")
                .en("Terminal")
                .fr("Terminal");

        addCatalogTab("name")
                .en("Catalog")
                .fr("Catalogue");

        add("modUpdate", "query", "compatible")
                .en("The mod $$1 has an available update: $$2 to $$3.\nDo you want to download it ?")
                .fr("Le mod $$1 a une mise a jour disponible : $$2 vers $$3.\nVoulez-vous l'installer ?");
        add("modUpdate", "query", "notCompatible")
                .en("The mod $$1 is incompatible ! However, it's version $$2 is.\nDo you want to download it ?")
                .fr("Le mod $$1 est incompatible ! Cependant, sa version $$2 l'est.\nVoulez-vous l'installer ?");

        add("modUpdate", "dl", "noLink")
                .en("There are no install link set !\nPlease contact the mod owner.")
                .fr("Aucuns lien d'installation défini !\nRéferez vous au développeur du mod.");
        add("modUpdate", "dl", "noLink", "title")
                .en("No install link")
                .fr("Lien d'installation manquant");

        add("modUpdate", "dl", "failed")
                .en("Failed to open install link in browser !")
                .fr("Nous n'avons pas pu ouvrir le lien d'installation dans votre navigateur.");
        add("modUpdate", "dl", "failed", "title")
                .en("Browsing failed")
                .fr("La navigation a échoué");
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
        return addTab(Tabs.INSTANCE.HOME, children);
    }
    /**
     * Add a translation for the settings tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addSettingsTab(String... children) {
        return addTab(Tabs.INSTANCE.SETTINGS, children);
    }
    /**
     * Add a translation for the terminal tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addTerminalTab(String... children) {
        return addTab(Tabs.INSTANCE.TERMINAL, children);
    }
    /**
     * Add a translation for the catalog tab
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder addCatalogTab(String... children) {
        return addTab(Tabs.INSTANCE.CATALOG, children);
    }
}
