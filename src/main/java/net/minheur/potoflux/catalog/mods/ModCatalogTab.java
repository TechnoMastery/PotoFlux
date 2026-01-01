package net.minheur.potoflux.catalog.mods;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * Make a catalog tab for your own tasks
 * @param id the unique id of your tab
 * @param name your tab's name (an id gettable via Translations.get())
 * @param panel the panel of your tab
 */
public record ModCatalogTab(ResourceLocation id, String name, Supplier<JPanel> panel) implements IRegistryType {
    public JPanel getPanel() {
        return panel.get();
    }
}
