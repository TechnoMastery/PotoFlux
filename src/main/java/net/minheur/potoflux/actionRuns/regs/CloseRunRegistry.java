package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloseRunRegistry implements IRegistry<ActionRun> {
    private static final Map<ResourceLocation, ActionRun> REGISTRY = new LinkedHashMap<>();

    public static Collection<ActionRun> getAll() {
        return REGISTRY.values();
    }

    @Override
    public ActionRun add(ActionRun item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This potoflux action run is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
