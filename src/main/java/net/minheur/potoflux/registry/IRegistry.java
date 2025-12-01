package net.minheur.potoflux.registry;

import java.util.Collection;

public interface IRegistry<T extends IRegistryType> {
    T add(T item);
}
