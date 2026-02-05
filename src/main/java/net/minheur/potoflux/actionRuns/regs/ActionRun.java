package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * This class creates an action run
 * @param id the resource loc of your action run
 * @param run the runnable to execute
 */
public record ActionRun(ResourceLocation id, Runnable run) implements IRegistryType {
}
