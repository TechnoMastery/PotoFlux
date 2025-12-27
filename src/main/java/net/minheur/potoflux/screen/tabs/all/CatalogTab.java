package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.catalog.CatalogGetterHandler;
import net.minheur.potoflux.catalog.ModCatalog;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import java.util.List;

public class CatalogTab extends BaseTab {
    private List<ModCatalog> catalog;

    @Override
    protected void setPanel() {
        reloadCatalog();

        //
    }

    public void reloadCatalog() {
        CatalogGetterHandler.buildCatalog();
        this.catalog = CatalogGetterHandler.getCatalog();
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
