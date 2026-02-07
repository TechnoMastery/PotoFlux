package net.minheur.potoflux.loader.mod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.Nonnull;

import static net.minheur.potoflux.loader.PotoFluxLoadingContext.*;

public class AddonLoader {

    private ClassLoader normalClassLoader;
    private Set<Class<?>> addons;

    public void loadAddons() {

        // ===== MOD CONTEXT CLASSLOADER (PROD ONLY) =====
        boolean isModClassLoaderActive = false;
        if (isProdEnv()) {

            saveNormalClassLoader();
            setModClassLoader();

            isModClassLoaderActive = true;

        }

        try {

            if (isProdEnv()) {

                // ===== PROD =====
                addons = getModProdEnv();

            } else {

                // ===== DEV =====
                addons = getModsDevEnv();

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

                    // add it to the registry
                    if (!PotoFluxLoadingContext.listMod(modAnnotation, clazz)) {
                        PtfLogger.error("Failed to list mod : " + clazz.getName() + " ! Skipping...", LogCategories.MOD_LOADER);
                        continue;
                    }

                    // laisse le constructeur faire son job : il peut accéder au bus via PotoFluxLoadingContext.get().getModEventBus()
                    PtfLogger.info("Listed addon: " + clazz.getName() + ", modId '" + modAnnotation.modId() + "'", LogCategories.MOD_LOADER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } finally {
            // replace normal classLoader
            if (isModClassLoaderActive) setNormalClassLoader();
        }
    }

    private Set<Class<?>> getModsDevEnv() {
        Collection<URL> urls = getDevScanUrls();

        if (urls.isEmpty()) {
            PtfLogger.info("No mod file found !", LogCategories.MOD_LOADER);
            return new HashSet<>();
        }

        Reflections reflections = getDevReflection(urls);

        return reflections.getTypesAnnotatedWith(Mod.class);
    }

    @Nonnull
    private static Reflections getDevReflection(Collection<URL> urls) {
        return new Reflections(

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
    }

    private void saveNormalClassLoader() {
        normalClassLoader = getCurrentClassLoader();
    }

    private void setNormalClassLoader() {
        if (normalClassLoader == null) throw new IllegalThreadStateException("Normal class loader not saved ! Thread class loader stuck as mod !");

        Thread.currentThread().setContextClassLoader(
                normalClassLoader
        );
    }

    public static Set<Class<?>> getModProdEnv() {
        ClassLoader modsClassLoader = getModsClassLoader();

        setModClassLoader();

        Set<Class<?>> addons = new HashSet<>();
        Path modsDir = PotoFlux.getProgramDir().resolve("mods");

        // stream = all jar files
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir, "*.jar");) {
            // for each, make the jarFile
            for (Path jarPath : stream) try (JarFile jar = new JarFile(jarPath.toFile())) {
                PtfLogger.info("Scanning jar " + jar.getName(), LogCategories.MOD_LOADER);

                // list all jar entries (so classes)
                Enumeration<JarEntry> entries = jar.entries();
                // process classes
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if (!entry.getName().endsWith(".class")) continue; // continue on all non-class files

                    String className = entry.getName()
                            .replace('/', '.')
                            .replace(".class", "");

                    try {
                        // turn to class
                        Class<?> clazz = Class.forName(className, false, modsClassLoader);

                        // if @Mod is present, add to addons
                        if (clazz.isAnnotationPresent(Mod.class)) {
                            PtfLogger.info("Found @Mod class: " + clazz.getName(), LogCategories.MOD_LOADER);
                            addons.add(clazz);
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError ignored) {}
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addons;
    }
}

