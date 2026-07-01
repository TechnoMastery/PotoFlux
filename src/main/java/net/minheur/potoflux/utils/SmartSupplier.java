package net.minheur.potoflux.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The smart supplier is a [Supplier] that doesn't recreate the field each time.
 * Once the value is created from the [supplier] once, it is stored in [value] and given each time [get] is used
 *
 * @param <T> type of value stored in the supplier
 */
public class SmartSupplier<T> implements Supplier<T> {

    /**
     * The supplier field.
     */
    @NotNull
    protected final Supplier<@NotNull T> supplier;

    /**
     * {@link T} containing the value after it has been created once
     */
    protected T value = null;

    /**
     * Constructs a new SmartSupplier.
     */
    public SmartSupplier(@NotNull Supplier<@NotNull T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the value. If [value] is `null`, creates it from the [supplier]
     *
     * @return [value]
     */
    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }
}
