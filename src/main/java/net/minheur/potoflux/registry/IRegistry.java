package net.minheur.potoflux.registry;

public interface IRegistry<T extends IRegistryType> {
    T add(T item);
}
