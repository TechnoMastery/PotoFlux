package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

/**
 * Registry for all file related translations
 */
public class FileTranslations extends AbstractTranslationsRegistry {
    public FileTranslations() {
        super("file");
    }

    /**
     * Actual registering
     */
    @Override
    protected void makeTranslation() {
        error("exist")
                .en("File already exists.")
                .fr("Le fichier existe déjà");
        error("exist", "desc")
                .en("A file with the same name already exists.")
                .fr("Un fichier de même nom existe déjà.");
        error("saving")
                .en("Error while saving file: ")
                .fr("Erreur lors de l'enregistrement du fichier : ");
        error("not_found")
                .en("File not found")
                .fr("Fichier non trouvé");
        error("not_found", "linked")
                .en("File not found: ")
                .fr("fichier non trouvé : ");

        add("loaded")
                .en("File loaded: ")
                .fr("Fichier chargé : ");
        add("saved")
                .en("File saved")
                .fr("Fichier sauvegardé");

        json()
                .en("JSON files (*.json)")
                .fr("Fichiers JSON (*.json)");
        json("error", "invalid")
                .en("The JSON file is invalid !")
                .fr("Ce fichier JSON est invalide !");
        json("error", "loading")
                .en("Error while loading JSON file !")
                .fr("Erreur lors du chargement du fichier JSON !");
        json("import")
                .en("Import JSON file...")
                .fr("Importez un  fichier JSON...");
    }

    /**
     * Adds an error branch file translation
     * @param children optional extra members
     * @return the translation builder associated
     */
    private TranslationBuilder error(String... children) {
        return add("error", children);
    }
    private TranslationBuilder json(String... children) {
        return add("json", children);
    }
}
