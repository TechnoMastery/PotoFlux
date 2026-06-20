package net.minheur.potoflux.registry;

import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A registry for an event. There should only be one by event.<br>
 * The mods should create a {@link RegistryList} then use its register method, giving the class implementing this.
 *
 * @param <T> what registry item that the reg lists
 */
public abstract class AbstractRegistry<T extends IRegistryType> {

    /**
     * Actual registry for the {@link T}, by their ID and their value.
     */
    protected final Map<ResourceLocation, T> REGISTRY = new LinkedHashMap<>();
    protected final AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * Getter for all value
     *
     * @return {@linkplain #REGISTRY}'s values
     */
    @Contract(pure = true)
    public @NotNull Collection<T> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds an item to the reg
     *
     * @param item object to add to the reg
     * @return the item added
     */
    public T add(@NotNull T item) {
        if (closed.get()) throw new IllegalStateException("Can't add item to closed reg !");
        if (REGISTRY.containsKey(item.id()))
            throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }

    public void close() {
        closed.set(true);
    }
}
