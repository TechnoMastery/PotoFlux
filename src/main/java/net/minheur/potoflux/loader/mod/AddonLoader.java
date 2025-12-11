package net.minheur.potoflux.loader.mod;

import java.util.Set;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import org.reflections.Reflections;
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
                Mod modAnnotation = clazz.getAnnotation(Mod.class);
                if (modAnnotation == null) {
                    System.err.println("Class " + clazz.getName() + " passed the check but is missing @Mod annotation !"); // should never append : we have here all classes annotated, got from reflection
                    continue;
                }

                if (PotoFluxLoadingContext.isModLoaded(modAnnotation)) {
                    System.err.println("Mod with modId '" + modAnnotation.modId() + "' is already loaded ! Skipping " + clazz.getName());
                    continue;
                }

                // creates the addon (constructeur par défaut)
                Object instance = clazz.getDeclaredConstructor().newInstance();
                // add it to the registry
                if (!PotoFluxLoadingContext.addMod(modAnnotation, clazz)) {
                    System.err.println("Failed to load mod : " + clazz.getName() + " ! Skipping...");
                    continue;
                }

                // laisse le constructeur faire son job : il peut accéder au bus via PotoFluxLoadingContext.get().getModEventBus()
                System.out.println("Loaded addon: " + clazz.getName() + ", modId '" + modAnnotation.modId() + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

