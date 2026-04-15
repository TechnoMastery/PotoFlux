package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.terminal.commands.Commands;
import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

import java.util.Arrays;

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
                .en("Show an ASCII")
                .fr("Affiche un ASCII");
        addCommandUse("ascii")
                .en("Usage: ascii")
                .fr("Utilisation : ascii");
        addCommandUse("ascii","choose")
                .en("Usage: ascii <name>")
                .fr("Utilisation : ascii <nom>");

        addCommand("clear")
                .en("Clears the terminal")
                .fr("Vide le terminal");
        addCommandUse("clear")
                .en("Usage: clear")
                .fr("Utilisation : clear");

        addCommand("version")
                .en("Get actual PotoFlux version")
                .fr("Affiche la version actuelle de PotoFLux");
        addCommandUse("version")
                .en("Usage: version")
                .fr("Utilisation : version");

        addCommand("echo")
                .en("Repeats what you give")
                .fr("Répète ce que vous écrivez");
        addCommandUse("echo")
                .en("Usage: echo <text>")
                .fr("Utilisation : echo <texte>");

        addCommand("hello_world")
                .en("Says hello world")
                .fr("Dit hello world");
        addCommandUse("hello_world")
                .en("Usage: hello")
                .fr("Utilisation : hello");

        addCommand("help")
                .en("Shows this menu")
                .fr("Affiche ce menu");
        addCommand("help", "title")
                .en("Command list:")
                .fr("Liste des commandes :");
        addCommandUse("help")
                .en("Usage: help")
                .fr("Utilisation : help");
        addCommandUse("help", "command")
                .en("Usage: help <command>")
                .fr("Utilisation : help <commande>");

        addCommand("hidden", "out")
                .en("Hidden command :)")
                .fr("Commande caché ;)");

        addCommand("nope", "out")
                .en("I got you !")
                .fr("CHEH");

        addCommand("quit")
                .en("Exits app")
                .fr("Ferme l'app");
        addCommand("quit", "out")
                .en("Exiting...")
                .fr("Fermeture...");
        addCommandUse("quit")
                .en("Usage: quit")
                .fr("Utilisation : quit");

        addCommand("source")
                .en("Link to source code")
                .fr("Lien vers le code source");
        addCommand("source", "out")
                .en("Opened code source in browser !")
                .fr("Code source ouvert dans le navigateur !");
        addCommand("source", "noBrowse")
                .en("Could not open source code ! Here is the link:")
                .fr("Impossible d'ouvrir le code source dans le navigateur. Voici le lien :");
        addCommandUse("source")
                .en("Usage: source-code")
                .fr("Utilisation : source-code");

        addCommand("tab")
                .en("Open given tab")
                .fr("Ouvre l'onglet donné");
        addCommand("tab", "null")
                .en("Tab $$1 doesn't exists !")
                .fr("L'onglet $$1 n'existe pas !");
        addCommand("tab", "opened")
                .en("This tab is already opened !")
                .fr("Cet onglet est déjà ouvert !");
        addCommandUse("tab")
                .en("Usage: tab <tabResourceLoc>")
                .fr("Utilisation : tab <ressourceLocDeLaTab>");

        addCommand("listTab")
                .en("Lists all the tabs")
                .fr("Liste touts les onglets");
        addCommand("listTab", "intro")
                .en("Here is all the tabs: ")
                .fr("Voici touts les onglets : ");
        addCommandUse("listTab")
                .en("Usage: tabList")
                .fr("Utilisation : tabList");
        addCommandUse("listTab", "resourceLoc")
                .en("Usage: tabList --resourceLoc")
                .fr("Utilisation : tabList --resourceLoc");

        addCommand("time")
                .en("Tells the time")
                .fr("Dit le temps (très précis)");
        addCommandUse("time")
                .en("Usage: time")
                .fr("Utilisation : time");

        addCommand("modList")
                .en("Lists you loaded mods")
                .fr("Liste tous les mods chargé (activés)");
        addCommandUse("modList")
                .en("Usage: modList")
                .fr("Utilisation : modList");

        addCommand("modDir")
                .en("Open mod directory")
                .fr("Ouvre le dossier des mods");
        addCommandUse("modDir")
                .en("Usage: modDir")
                .fr("Utilisation : modDir");

        addCommand("logDir")
                .en("Open logs directory")
                .fr("Ouvre le dossier des logs");
        addCommandUse("logDir")
                .en("Usage: logDir")
                .fr("Utilisation : logDir");

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

        addPerm("viewUsers")
                .en("See user's informations")
                .fr("Consulter les informations des utilisateurs");
        addPerm("mkUsers")
                .en("Create new users")
                .fr("Créer de nouveaux utilisateurs");
        addPerm("rmUsers")
                .en("Delete users")
                .fr("Supprimer des comptes");
        addPerm("mdUserInfos")
                .en("Modify user's informations")
                .fr("Modifier les informations des utilisateurs");
        addPerm("mdUserPasswords")
                .en("Reset user's passwords")
                .fr("Réinitialiser les mots de passe");
        addPerm("lock")
                .en("Lock accounts")
                .fr("Vérrouiller des comptes");

        addPerm("executesInDetailsMenu")
                .en("This perm needs to be executed in the user details menu !\nIf you don't have it, please contact a referent")
                .fr("Cette permission doit être executé dans le menu des détails de l'utilisateur.\nSi vous ne l'avez pas, contactez votre référent");

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

        addAccountTab("name")
                .en("Account")
                .fr("Compte");
        addAccountTab("title")
                .en("Welcome !")
                .fr("Bienvenu");
        addAccountTab("title", "connected")
                .en("Welcome, $$1 $$2")
                .fr("Bienvenu, $$1 $$2");
        addAccountTab("failed")
                .en("Couldn't connect to server !")
                .fr("La connexion au serveur a échoué.");
        addAccountTab("error", "tokenMalformed")
                .en("Your connection token is malformed")
                .fr("Votre token de connexion est malformé");
        addAccountTab("error", "noToken")
                .en("Server didn't send a token ! Please retry.\nIf the error persists, please contact the devs.")
                .fr("Le serveur n'a pas envoyé de token ! Réessayez.\nSi l'erreur persiste, contactez les développeurs.");
        addAccountTab("error", "noUser")
                .en("User not found.\nIs the email correct?")
                .fr("Utilisateur inconnu.\nVérifiez l'adresse mail.");
        addAccountTab("error", "invalidPassword")
                .en("Password invalid !\nPlease retry.")
                .fr("Mot de passe invalide !\nRéessayez.");
        addAccountTab("error", "noResponse")
                .en("Could not contact server.\nPlease retry later")
                .fr("Impossible de contacter le serveur.\nRéessayez plus tard");
        addAccountTab("error", "token", "noUser")
                .en("Your token is not linked \nPlease reconnect.!")
                .fr("Votre token n'est pas lié !\nVeillez vous reconnecter");
        addAccountTab("error", "token", "notExists")
                .en("Your token doesn't exists !\nPlease reconnect.")
                .fr("Votre token n'existe pas !\nVeillez vous reconnecter");
        addAccountTab("error", "token", "expired")
                .en("Your token has expired.\nPlease reconnect.")
                .fr("Votre token a éxpiré.\nVeillez vous reconnecter");
        addAccountTab("error", "noPermRun")
                .en("This permission can't be run !")
                .fr("Cette permission ne peut pas être executé !");
        addAccountTab("executePermButton")
                .en("Run permission action").fr("Utiliser la permission");
        addAccountTab("addUser", "success")
                .en("Account $$1 has been created !").fr("Le compte $$1 a été créer avec succès");
        addAccountTab("rmUser", "check")
                .en("Are you sure you want to delete the account of the person ?\n$$1")
                .fr("Êtes vous sûr(e) de vouloir supprimer ce compte ?\n$$1");
        addAccountTab("rmUser", "success")
                .en("Account $$1 has been removed !").fr("Le compte $$1 a été supprimé avec succès");
        addAccountTab("error", "noPerm")
                .en("You don't have the permission to do this !")
                .fr("Vous n'avez pas la permission de faire cela !");
        addAccountTab("rmUser", "notExists")
                .en("User $$1 doesn't exists !")
                .fr("$$1 n'est pas un utilisateur !");
        addAccountTab("addUser", "outOfRangeRank")
                .en("Uhm... it seems like the rank you gave isn't in the range.\nTry getting it between 0 and 100 !")
                .fr("Oula... Tu es dans un endroit étrange.\nEssaye de garder le rang entre 0 et 100 !");
        addAccountTab("error", "rankToBig")
                .en("The rank you gave ($$1) is to high for yours ($$2) !\nRemember that a smaller number means a bigger rank.\nYou can create accounts with a rank at least one level down from yours !")
                .fr("Le rang fournit ($$1) est trop haut pour le tien ($$2) !\nRappelle-toi qu'un nombre petit indique un rang plus haut.\nTu ne peut créer un compte qu'avec un rang ayant au moins un niveau en dessous du tien !");
        addAccountTab("error", "rankToSmall")
                .en("The rank of the account you're trying to delete ($$1) is to big for yours ($$2) !\nRemember that a smaller number means a bigger rank.\nYou can delete accounts with a rank at least one level down from yours !")
                .fr("Le rang du compte que tu essaye de supprimer ($$1) est trop grand pour le tien ($$2) !\nRappelle-toi qu'un nombre petit indique un rang plus haut.\nTu ne peut supprimmer un compte qu'avec un rang ayant au moins un niveau en dessous du tien !");
        addAccountTab("listUsers", "failedUsers")
                .en("Some errors happened while getting users.\nInformations where not received for $$1 of them.\nFor more details, please check the logs, or/and contact the devs.")
                .fr("Quelques erreurs sont parvenu en essayant de lister les utilisateurs.\nLes informations n'ont pas été récupéré pour $$1 d'entre eux.\nPour plus d'informations, regardez les logs et/ou contactez les développeurs.");
        addAccountTab("listUsers", "empty")
                .en("It seems like there are no users listed.")
                .fr("Il n'y a aucuns utilisateurs à lister.");
        addAccountTab("listUsers", "details")
                .en("Email: $$1\nName: $$2 $$3\nRank: $$4\nPerms: $$5")
                .fr("Mail: $$1\nNom: $$2 $$3\nRank: $$4\nPermissions: $$5");
        addAccountTab("listUsers", "details", "emptyPerms")
                .en("None")
                .fr("Aucunes");
        addAccountTab("createAccount", "success")
                .en("Account created successfully ! You can now connect with those ids:\nEmail: $$1\nPassword: $$2")
                .fr("Compte créé avec succès ! Vous pouvez maintenant vous connecter avec ces identifiants :\nMail : $$1\nMot de passe : $$2");
        addAccountTab("createAccount", "disabled")
                .en("Sorry, but account creation is disabled. You can try again later !")
                .fr("Désolé, mais la création de compte est désactivé. Vous pouvez réessayer plus tard !");
        addAccountTab("createAccount", "invalidEmail")
                .en("It seems like the email you gave is invalid. Is it empty ?")
                .fr("L'adresse mail que vous avez fournit est invalide. Est elle vide ?");
        addAccountTab("createAccount", "emailUsed")
                .en("This email is already used.")
                .fr("Cet adresse mail est déjà utilisé.");
        addAccountTab("createAccount", "button")
                .en("Create an account")
                .fr("Créer un compte");
        addAccountTab("mdUserInfos", "confirm")
                .en("Are you sure you want to modify those infos :")
                .fr("Êtes vous sûr de vouloir modifier ces informations :");
        addAccountTab("mdUserInfos", "noMods")
                .en("No modifications were done.")
                .fr("Aucunes modifications apportées.");
        addAccountTab("mdUserInfos", "done")
                .en("Operation finished! Here's what changed:")
                .fr("Opération terminée ! Voici ce qui a changé :");
        addAccountTab("mdUserInfos", "idRemember")
                .en("Please remember to tell the user that it's email changed !")
                .fr("Merci de penser à notifier l'utilisateur de la modification de son mail !");
        addAccountTab("mdUserPassword", "done")
                .en("You modified $$1's password successfully.\nIt is now $$2 !\nPlease remember to let him now.")
                .fr("Vous avez modifié le mot de passe de $$1 avec succès !\nC'est maintenant $$2.\nMerci de le prévenir !");
        addAccountTab("error", "insufficientRank")
                .en("Looks like your rank is insufficient to perform this action on this user.")
                .fr("On dirait que ton rank est insuffisant pour exécuter cette action sur cet utilisateur.");
        addAccountTab("mdUserPassword", "button")
                .en("Modify password")
                .fr("Modifier le mot de passe");

        add("modUpdate", "query", "title")
                .en("Mod update available")
                .fr("Mise a jour de mod disponible");
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

        add("ptfUpdate", "title")
                .en("Update")
                .fr("Mise a jour");
        add("ptfUpdate", "desc")
                .en("New version of PotoFlux available !")
                .fr("Nouvelle version de PotoFlux disponible !");
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

    private TranslationBuilder addPerm(String... children) {
        return add("perms", children);
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
        return addTab("terminal", children);
    }
    private TranslationBuilder addAccountTab(String... children) {
        return addTab("account", children);
    }
}
