package net.minheur.potoflux.mod;

public interface EventListener<T> {
    void handle(T event);
}
