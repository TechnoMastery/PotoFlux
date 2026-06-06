package net.minheur.potoflux.utils;

import java.util.function.Supplier;

/**
 * The smart supplier is a {@link Supplier} that don't recreate the field each time.<br>
 * Once the value is created from the {@linkplain #supplier} once, it is stored in {@linkplain #value} and given each time {@linkplain #get()} is used
 * @param <T> type of value stored in the supplier
 */
public class SmartSupplier<T> implements Supplier<T> {

    /**
     * Supplier storing the data, to create it the first time
     */
    private final Supplier<T> supplier;
    /**
     * {@link T} containing the value after it has been created once
     */
    private T value;

    /**
     * Creates the smart supplier from a simple one
     * @param supplier initially storing the data
     */
    public SmartSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the value. If {@linkplain #value} is {@code null}, creates it from the {@linkplain #supplier}
     * @return {@link #value}
     */
    @Override
    public T get() {
        if (value == null)
            value = supplier.get();

        return value;
    }
}
