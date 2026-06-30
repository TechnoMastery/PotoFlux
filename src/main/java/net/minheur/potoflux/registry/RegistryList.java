package net.minheur.potoflux.registry;

import net.minheur.potoflux.utils.SmartSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class is used to store all elements of an event, for a specific mod.<br>
 * Create one in your registering class, then add your items to it.
 *
 * @param <T> the type of registry item that the registry stores
 */
public class RegistryList<T extends IRegistryType> {
    /**
     * The actual list containing items
     */
    private final List<SmartSupplier<T>> innerList = new ArrayList<>();

    /**
     * Method to add an item to the registry.
     *
     * @param item item to add
     * @return the item added, or null if already added
     */
    public SmartSupplier<T> add(SmartSupplier<T> item) {
        if (innerList.contains(item)) return null;
        innerList.add(item);
        return item;
    }

    /**
     * Adds an item to the registry list
     * @param item the item
     * @return the added item
     */
    public SmartSupplier<T> add(Supplier<T> item) {
        return add(new SmartSupplier<>(item));
    }

    /**
     * Registering all items in an actual registry.
     *
     * @param reg the registry to add the items to
     */
    public void register(AbstractRegistry<T> reg) {
        for (SmartSupplier<T> sup : innerList) {
            reg.add(sup.get());
        }
    }
}
