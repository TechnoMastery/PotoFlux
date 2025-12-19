package net.minheur.potoflux.translations.register;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;

public class CommonTranslations extends AbstractTranslationsRegistry {
    public CommonTranslations() {
        super("common");
    }

    @Override
    protected void makeTranslation() {
        add("back")
                .en("Back");
        add("cancel")
                .en("Cancel");
        add("cards")
                .en("Cards");
        add("close")
                .en("Close");
        add("confirm")
                .en("Confirm");
        add("error")
                .en("Error");
        add("export")
                .en("Export");
        add("fileNotFound")
                .en("File not found");
        add("file_saved")
                .en("File saved.");
        add("finish")
                .en("Finished");
        add("flip")
                .en("Flip");
        add("modify")
                .en("Modify");
        add("next")
                .en("Next");
        add("override_check")
                .en("Confirm override");
        add("path")
                .en("Path: ");
        add("start")
                .en("Start");
        add("saveSuccess")
                .en("Save successful");
        add("validate")
                .en("Validate");
    }
}
