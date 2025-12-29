package net.minheur.potoflux.catalog.mods;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import javax.swing.*;
import java.util.function.Supplier;

public record ModCatalogTab(ResourceLocation id, String name, Supplier<JPanel> panel) implements IRegistryType {
    public JPanel getPanel() {
        return panel.get();
    }
}
