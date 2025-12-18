package net.minheur.potoflux.loader.mod;

import net.minheur.potoflux.utils.EventListener;
import net.minheur.potoflux.utils.LambdaUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Bus simple, robuste.
 */
public class ModEventBus {

    // Map event type -> list of listeners
    private final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    // Wrapper interne pour stocker la cible et la méthode à appeler
    private static final class Listener {
        final Object target;    // Peut-être null si on stocke un Consumer
        final Method method;    // méthode à invoquer (prend 1 paramètre)
        final Consumer<Object> consumer; // alternative si user passe un Consumer

        Listener(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.consumer = null;
            this.method.setAccessible(true);
        }

        Listener(Consumer<Object> consumer) {
            this.target = null;
            this.method = null;
            this.consumer = consumer;
        }

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

    // 1) Méthode explicite : on fournit la classe et un Consumer
    public <E> void addListener(Class<E> eventClass, Consumer<E> consumer) {
        List<Listener> list = listeners.computeIfAbsent(eventClass, k -> new ArrayList<>());
        // safe cast pour stockage interne
        Consumer<Object> c = (Consumer<Object>) consumer;
        list.add(new Listener(c));
    }

    // 2) Méthode automatique : on passe une method reference (this::onSomething)
    //    On extrait la méthode réelle et enregistre l'instance + Method
    /**
     * Only method references are supported (no lambda expressions).<br>
     * The listener method must take exactly one parameter.<br>
     * No inline listeners, as {@code e -> handle(e)} or {@code e -> System.out.println()}
     */
    public <E> void addListener(EventListener<E> consumer) {
        if (consumer == null)
            throw new IllegalArgumentException("listener null");

        // Class<?> lambdaClass = consumer.getClass();
        // if (lambdaClass.isSynthetic())
        //     throw new IllegalArgumentException("Lamba expressions are not supported. Use a method reference (this::onEvent).");

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

    // post : appelle les listeners enregistrés pour la classe exacte
    public void post(Object event) {
        if (event == null) throw new IllegalArgumentException("event null");
        List<Listener> list = listeners.get(event.getClass());
        if (list == null) return;
        for (Listener l : new ArrayList<>(list)) { // copie pour sécurité si listeners modifiés durant post
            l.invoke(event);
        }
    }
}

