package net.minheur.potoflux.loader.mod;

import java.util.Set;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import org.reflections.Reflections; // si tu veux utiliser Reflections ; sinon tu peux lister les JARs manuellement
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

public class AddonLoader {

    public void loadAddons() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(PotoFluxLoadingContext.getScanUrls())
                        .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
        );
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

