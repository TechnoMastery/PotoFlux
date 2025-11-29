package net.minheur.potoflux.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RegistryList<T> {
    private final List<T> list = new ArrayList<>();

    public T register(Supplier<T> supplier) {
        list.add(supplier.get());
        return supplier.get();
    }
}
