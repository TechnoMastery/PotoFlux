package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

public class CatalogTab extends BaseTab {
    @Override
    protected void setPanel() {
        // TODO
    }

    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.catalog.name");
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
