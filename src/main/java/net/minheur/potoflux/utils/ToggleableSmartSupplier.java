package net.minheur.potoflux.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ToggleableSmartSupplier<T> extends SmartSupplier<T> {
    /**
     * Constructs a new SmartSupplier.
     */
    public ToggleableSmartSupplier(@NotNull Supplier<T> supplier) {
        super(supplier);
    }

    public void reset() {
        this.value = null;
    }
}
