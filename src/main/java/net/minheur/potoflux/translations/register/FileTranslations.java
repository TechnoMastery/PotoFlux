package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

public class FileTranslations extends AbstractTranslationsRegistry {
    public FileTranslations() {
        super("file");
    }

    @Override
    protected void makeTranslation() {
        error("exist")
                .en("File already existing.");
        error("exist", "desc")
                .en("A file with the same name already exists.");
        error("saving")
                .en("Error while saving file: ");
        error("not_found")
                .en("File not found");
        error("not_found", "linked")
                .en("File not found: ");

        add("loaded")
                .en("File loaded: ");
        add("saved")
                .en("File saved");

        json()
                .en("JSON files (*.json)");
        json("error", "invalid")
                .en("The JSON file is invalid !");
        json("error", "loading")
                .en("Error while loading JSON file !");
        json("import")
                .en("Import JSON file...");
    }

    private TranslationBuilder error(String... children) {
        return add("error", children);
    }
    private TranslationBuilder json(String... children) {
        return add("json", children);
    }
}
