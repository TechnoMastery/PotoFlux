package net.minheur.potoflux.registry;

/**
 * A registry for an event. There should only be one by event.<br>
 * The mods should create a {@link RegistryList} then use its register method, giving the class implementing this.
 * @param <T> what registry item that the reg lists
 */
public interface IRegistry<T extends IRegistryType> {
    /**
     * Adds an item to the reg
     * @param item object to add to the reg
     * @return the item added
     */
    T add(T item);
}
