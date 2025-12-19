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
        error("saving")
                .en("Error while saving file: ");

        json()
                .en("JSON files (*.json)");
        // TODO: a partir de ligne 52 dans en.json

    }

    private TranslationBuilder error(String... children) {
        return add("error", children);
    }
    private TranslationBuilder json(String... children) {
        return add("json", children);
    }
}
