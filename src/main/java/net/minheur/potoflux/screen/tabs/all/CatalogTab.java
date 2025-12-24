package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.catalog.CatalogGetterHandler;
import net.minheur.potoflux.catalog.ModCatalog;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import java.util.Map;

public class CatalogTab extends BaseTab {
    private final Map<String, ModCatalog> catalog;

    public CatalogTab() {
        super();
        CatalogGetterHandler.buildCatalog();
        this.catalog = CatalogGetterHandler.getCatalog();
    }

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
