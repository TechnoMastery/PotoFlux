package net.minheur.potoflux.loader.mod;

import net.minheur.potoflux.loader.mod.events.SubscribeEvent;

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
    public void addListener(Object methodRefOrLambda) {
        if (methodRefOrLambda == null) throw new IllegalArgumentException("listener null");

        Method implMethod = LambdaUtils.getImplMethod(methodRefOrLambda);
        if (implMethod == null) {
            throw new IllegalArgumentException("Impossible d'extraire la méthode cible du listener");
        }

        // la méthode doit accepter exactement 1 param
        if (implMethod.getParameterCount() != 1) {
            throw new IllegalArgumentException("Listener method must take exactly 1 parameter");
        }

        Class<?> eventType = implMethod.getParameterTypes()[0];

        // trouver l'instance « target » : pour les method references non statiques,
        // on peut tenter d'extraire l'instance via reflection sur la lambda (implMethodRef is trickier).
        // Ici on suppose que la methodRef est une method reference à une méthode d'instance,
        // donc la classe du lambda contient un champ capturé (synthetic) qui référence 'this'.
        Object targetInstance = LambdaUtils.getCapturingInstance(methodRefOrLambda);

        List<Listener> list = listeners.computeIfAbsent(eventType, k -> new ArrayList<>());
        list.add(new Listener(targetInstance, implMethod));
    }

    // for subscribed events
    public void registerClass(Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(SubscribeEvent.class)) continue;

            if (!Modifier.isStatic(m.getModifiers()))
                throw new IllegalArgumentException("@SubscribeEvent method must be static");

            if (m.getParameterCount() != 1)
                throw new IllegalArgumentException("@SubscribeEvent method must take exactly 1 parameter");

            Class<?> eventType = m.getParameterTypes()[0];
            m.setAccessible(true);

            listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(new Listener(null, m));
        }
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

