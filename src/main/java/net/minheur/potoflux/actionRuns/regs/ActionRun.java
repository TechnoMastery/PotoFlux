package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

public record ActionRun(ResourceLocation id, Runnable run) implements IRegistryType {
}
