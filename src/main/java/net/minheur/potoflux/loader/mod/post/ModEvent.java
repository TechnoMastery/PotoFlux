package net.minheur.potoflux.loader.mod.post;

import net.minheur.potoflux.loader.mod.events.IEvent;
import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * Mod event record.
 */
public record ModEvent(ResourceLocation id, IEvent event) implements IRegistryType {
}
