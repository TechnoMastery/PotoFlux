package net.minheur.potoflux.styles;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * Stylesheet entry record.
 */
public record StylesheetEntry(ResourceLocation id, String stylesheetDir) implements IRegistryType {
}
