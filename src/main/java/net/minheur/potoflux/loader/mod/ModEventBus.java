package net.minheur.potoflux.loader.mod;

import net.minheur.potoflux.utils.EventListener;
import net.minheur.potoflux.utils.LambdaUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Bus for registering all listeners in potoflux.<br>
 * Mods add theirs methods to the event listeners, so they can be executed on need.
 */
public class ModEventBus {

    // Map event type -> list of listeners
    /**
     * Map storing all listeners.<br>
     * At each event, we give a list of listeners that will be executed when the event is posted.
     */
    private final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    /**
     * Inner wrapper to help store listeners.
     */
    private static final class Listener {
        /**
         * Target of the listener.<br>
         * Might be null when storing a consumer
         */
        final Object target;
        /**
         * The method to invoke on the event post<br>
         * Take 1 parameter : the event it subscribes to
         */
        final Method method;
        /**
         * Other wat to store the target if a consumer is used
         */
        final Consumer<Object> consumer;

        /**
         * Creates a listener with a raw target and a method.
         * @param target raw target
         * @param method the subscribed method
         */
        Listener(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.consumer = null;
            this.method.setAccessible(true);
        }

        /**
         * Creates a listener with a consumer.<br>
         * The method is extracted from the consumer
         * @param consumer target of the listener
         */
        Listener(Consumer<Object> consumer) {
            this.target = null;
            this.method = null;
            this.consumer = consumer;
        }

        /**
         * This is called when the event of the listener is posted.<br>
         * This runs the {@link #method} with the {@link #target}, or it accepts the {@link #consumer} if no method / target are given.
         * @param event event that is posted : given to the method as its parameter.
         */
        void invoke(Object event) {
            try {
                if (consumer != null) {
                    consumer.accept(event);
                } else {
                    method.invoke(target, event);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a listener with an event class and a consumer of this event.<br>
     * This requires the registering mod to explicitly say which event is used.
     * @param eventClass event used
     * @param consumer method to get called when the event is posted
     * @param <E> the type of event
     */
    public <E> void addListener(Class<E> eventClass, Consumer<E> consumer) {
        List<Listener> list = listeners.computeIfAbsent(eventClass, k -> new ArrayList<>());
        // safe cast pour stockage interne
        Consumer<Object> c = (Consumer<Object>) consumer;
        list.add(new Listener(c));
    }

    /**
     * Only method references are supported (no lambda expressions).<br>
     * The listener method must take exactly one parameter.<br>
     * No inline listeners, as {@code e -> handle(e)} or {@code e -> System.out.println()}
     * @param consumer the consumer, in the form of {@code this::onSomething}, to invoke on the event post
     * @param <E> the event linked
     */
    public <E> void addListener(EventListener<E> consumer) {
        if (consumer == null)
            throw new IllegalArgumentException("listener null");

        Method m = LambdaUtils.getImplMethod(consumer);
        if (m == null)
            throw new IllegalArgumentException("Can't extract target method from listener");

        // la méthode doit accepter exactement 1 param
        if (m.getParameterCount() != 1)
            throw new IllegalArgumentException("Listener method must take exactly 1 parameter");

        @SuppressWarnings("unchecked")
        Class<E> eventClass = (Class<E>) m.getParameterTypes()[0];

        addListener(eventClass, consumer);
    }

    /**
     * This actually posts the events.<br>
     * When an event is posted, the param given to this method (the event) is then transferred to all method subscribing to the event.
     * @param event the posted event.
     */
    public void post(Object event) {
        if (event == null) throw new IllegalArgumentException("event null");
        List<Listener> list = listeners.get(event.getClass());
        if (list == null) return;
        for (Listener l : new ArrayList<>(list)) { // security copy, if listeners modified during the post
            l.invoke(event);
        }
    }
}

