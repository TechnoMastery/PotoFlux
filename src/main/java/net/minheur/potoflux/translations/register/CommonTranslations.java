package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

/**
 * Translation registry for all common words, that can be used a lot.
 */
public class CommonTranslations extends AbstractTranslationsRegistry {
    public CommonTranslations() {
        super("common");
    }

    /**
     * Actual registering
     */
    @Override
    protected void makeTranslation() {
        add("back")
                .en("Back")
                .fr("Retour");
        add("add")
                .en("Add")
                .fr("Ajouter");
        add("pinned")
                .en("Pinned")
                .fr("Épinglé");
        add("rename")
                .en("Rename")
                .fr("Renommer");
        add("cancel")
                .en("Cancel")
                .fr("Annuler");
        add("cards")
                .en("Cards")
                .fr("Cartes");
        add("create")
                .en("Create")
                .fr("Créer");
        add("list")
                .en("List")
                .fr("Liste");
        add("load")
                .en("Load")
                .fr("Charger");
        add("main")
                .en("Main")
                .fr("Principal");
        add("close")
                .en("Close")
                .fr("Fermer");
        add("confirm")
                .en("Confirm")
                .fr("Confirmer");
        add("error")
                .en("Error")
                .fr("Erreur");
        add("export")
                .en("Export")
                .fr("Exporter");
        add("finish")
                .en("Finished")
                .fr("Fini");
        add("flip")
                .en("Flip")
                .fr("Retourner");
        add("modify")
                .en("Modify")
                .fr("Modifier");
        add("next")
                .en("Next")
                .fr("Suivant");
        add("override_check")
                .en("Confirm override")
                .fr("Confirmation d'écrasement");
        add("path")
                .en("Path: ")
                .fr("Chemin : ");
        add("start")
                .en("Start")
                .fr("Début");
        add("saveSuccess")
                .en("Save successful")
                .fr("Sauvegardé avec succès");
        add("validate")
                .en("Validate")
                .fr("Valider");
        add("add_cancel")
                .en("Adding has been canceled")
                .fr("Ajout annulé");
        add("info")
                .en("Infos")
                .fr("Infos");
        add("delete")
                .en("Delete")
                .fr("Supprimer");
        add("delete", "success")
                .en("Delection successful")
                .fr("Suppression réussi");
        add("read_error")
                .en("Error while reading: ")
                .fr("Erreur en lisant : ");
        add("nothing_save")
                .en("There is nothing to save.")
                .fr("Il n'y a rien a sauvegarder.");
        add("select_list")
                .en("<Select List>")
                .fr("<Sélectionnez une liste>");
    }
}
