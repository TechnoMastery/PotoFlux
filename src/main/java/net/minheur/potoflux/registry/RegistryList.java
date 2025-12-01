package net.minheur.potoflux.registry;

import java.util.ArrayList;
import java.util.List;

public class RegistryList<T extends IRegistryType> {
    private final List<T> innerList = new ArrayList<>();

    public T add(T item) {
        if (innerList.contains(item)) return null;
        innerList.add(item);
        return item;
    }

    public void register(IRegistry<T> reg) {
        for (T t : innerList) {
            reg.add(t);
        }
    }
}
