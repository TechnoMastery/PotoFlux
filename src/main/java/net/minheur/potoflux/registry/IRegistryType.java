package net.minheur.potoflux.registry;

import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * Used to tag objects that can be stored in registry lists and registered in events, like tabs or commands.
 */
public interface IRegistryType {
    ResourceLocation id();
}
