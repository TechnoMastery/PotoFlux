package net.minheur.potoflux.loader.mod;

import java.net.URL;
import java.util.*;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class AddonLoader {

    public void loadAddons() {
        Set<Class<?>> addons;

        // ===== CONTEXT CLASSLOADER (PROD ONLY) =====
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        boolean switched = false;

        if (!PotoFluxLoadingContext.isDevEnv()) {

            Thread.currentThread().setContextClassLoader(
                    PotoFluxLoadingContext.getModsClassLoader()
            );
            switched = true;

        }

        try {

            if (PotoFluxLoadingContext.isDevEnv()) {

                // ===== DEV =====

                Collection<URL> urls = PotoFluxLoadingContext.getDevScanUrls();

                if (urls.isEmpty()) {
                    PtfLogger.info("No mod file found !", LogCategories.MOD_LOADER);
                    return;
                }

                Reflections reflections = new Reflections(
                        new ConfigurationBuilder()
                                .setUrls(urls)
                                .addClassLoaders(
                                        Thread.currentThread().getContextClassLoader()
                                )
                                .setScanners(
                                        Scanners.TypesAnnotated,
                                        Scanners.SubTypes
                                )
                );

                addons = reflections.getTypesAnnotatedWith(Mod.class);

            } else {

                // ===== PROD =====

                addons = PotoFluxLoadingContext.getAddons();

            }

            if (addons.isEmpty()) {
                PtfLogger.info("No mods found !", LogCategories.MOD_LOADER);
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

        } finally {
            // replace classLoader
            if (switched) Thread.currentThread().setContextClassLoader(old);
        }
    }
}

