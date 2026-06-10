package net.minheur.potoflux.styles;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

public record StylesheetEntry(ResourceLocation id, String stylesheetDir) implements IRegistryType {
}
