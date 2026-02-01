package net.minheur.potoflux.utils;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Another way to add listeners to the mod Event bus.
 * @deprecated because another framework is easier to use
 * @param <E> The event to listen
 */
@Deprecated(since = "6.4")
@FunctionalInterface
public interface EventListener<E> extends Consumer<E>, Serializable {
}
