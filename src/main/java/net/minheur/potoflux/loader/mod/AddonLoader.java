package net.minheur.potoflux.loader.mod;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Set;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.utils.logger.LogCategories;
import net.minheur.potoflux.utils.logger.PtfLogger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

public class AddonLoader {

    public void loadAddons() {
        Collection<URL> urls = PotoFluxLoadingContext.getScanUrls();
        Set<Class<?>> addons;

        if (!urls.isEmpty()) {
            ClassLoader modClassLoader = new URLClassLoader(
                    urls.toArray(new URL[0]),
                    Thread.currentThread().getContextClassLoader()
            );

            Reflections reflections = new Reflections(
                    new ConfigurationBuilder()
                            .setUrls(urls)
                            .addClassLoaders(modClassLoader)
                            .setScanners(
                                    new SubTypesScanner(false),
                                    new TypeAnnotationsScanner()
                            )
            );

            addons = reflections.getTypesAnnotatedWith(Mod.class);
        } else {
            PtfLogger.info("No mods found, skipping Reflection scan.", LogCategories.MOD_LOADER);
            return;
        }

        if (addons.isEmpty()) {
            PtfLogger.info("No mods found after Reflection scan.", LogCategories.MOD_LOADER);
            return;
        }

        for (Class<?> clazz : addons) {
            try {
                Mod modAnnotation = clazz.getAnnotation(Mod.class);
                if (modAnnotation == null) {
                    PtfLogger.warning("Class " + clazz.getName() + " passed the check but is missing @Mod annotation ! Skipping...",
                            LogCategories.MOD_LOADER); // should never append : we have here all classes annotated, got from reflection
                    continue;
                }

                if (PotoFluxLoadingContext.isModLoaded(modAnnotation)) {
                    PtfLogger.warning("Mod with modId '" + modAnnotation.modId() + "' is already loaded, or it's modId is illegal ! Skipping " + clazz.getName(),
                            LogCategories.MOD_LOADER);
                    continue;
                }

                // creates the addon (constructeur par défaut)
                Object instance = clazz.getDeclaredConstructor().newInstance();
                // add it to the registry
                if (!PotoFluxLoadingContext.addMod(modAnnotation, clazz)) {
                    PtfLogger.error("Failed to load mod : " + clazz.getName() + " ! Skipping...", LogCategories.MOD_LOADER);
                    continue;
                }

                // laisse le constructeur faire son job : il peut accéder au bus via PotoFluxLoadingContext.get().getModEventBus()
                PtfLogger.info("Loaded addon: " + clazz.getName() + ", modId '" + modAnnotation.modId() + "'", LogCategories.MOD_LOADER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

