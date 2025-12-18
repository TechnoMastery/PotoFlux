package net.minheur.potoflux.utils;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface EventListener<E> extends Consumer<E>, Serializable {
}
