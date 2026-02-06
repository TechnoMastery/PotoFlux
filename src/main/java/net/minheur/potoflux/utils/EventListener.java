package net.minheur.potoflux.utils;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Used to add event listeners to mod event bus, without telling explicitly the event
 * @param <E> The event to listen
 */
@FunctionalInterface
public interface EventListener<E> extends Consumer<E>, Serializable {
}
