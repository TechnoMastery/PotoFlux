package net.minheur.potoflux.loader.mod;

import java.util.Set;
import org.reflections.Reflections; // si tu veux utiliser Reflections ; sinon tu peux lister les JARs manuellement

public class AddonLoader {

    public void loadAddons() {
        // tu peux remplacer Reflections("") par un scan limité à ton package si tu veux
        Reflections reflections = new Reflections(""); // scan whole classpath
        Set<Class<?>> addons = reflections.getTypesAnnotatedWith(Mod.class);

        for (Class<?> clazz : addons) {
            try {
                // instancie l'addon (constructeur par défaut)
                Object instance = clazz.getDeclaredConstructor().newInstance();

                // laisse le constructeur faire son job : il peut accéder au bus via PotoFluxLoadingContext.get().getModEventBus()
                System.out.println("Loaded addon: " + clazz.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

