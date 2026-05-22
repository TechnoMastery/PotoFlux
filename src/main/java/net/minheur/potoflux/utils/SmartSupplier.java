package net.minheur.potoflux.utils;

import java.util.function.Supplier;

public class SmartSupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private T value;

    public SmartSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value == null)
            value = supplier.get();

        return value;
    }
}
