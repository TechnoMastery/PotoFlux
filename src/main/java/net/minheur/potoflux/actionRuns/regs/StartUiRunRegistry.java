package net.minheur.potoflux.actionRuns.regs;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The main registry for all UI start action runs
 */
public class StartUiRunRegistry implements IRegistry<ActionRun> {
    /**
     * The actual reg that contains the action runs
     */
    private static final Map<ResourceLocation, ActionRun> REGISTRY = new LinkedHashMap<>();

    /**
     * Gets all action runs
     * @return the UI start action runs
     */
    public static Collection<ActionRun> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds an action run to the on UI start actions
     * @param item action run to add to the reg
     * @return the added action run
     */
    @Override
    public ActionRun add(ActionRun item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This potoflux action run is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
