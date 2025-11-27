package net.minheur.potoflux.mod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minheur.potoflux.mod.ModdingUtils.getLambdaMethod;

public class ModEventBus {
    private final Map<Class<?>, List<Consumer<Object>>> listeners = new HashMap<>();

    public <T> void addListener(Consumer<T> listener) {
        Method m = getLambdaMethod(listener);

        if (m == null || m.getParameterCount() != 1) throw new IllegalArgumentException("invalid event listener");

        Class<T> eventType = m.getParameterTypes()[0];

        listeners.computeIfAbsent(eventType, e -> new ArrayList<>())
                .add(listener);
    }
}
