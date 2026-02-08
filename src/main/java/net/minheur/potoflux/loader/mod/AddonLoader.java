package net.minheur.potoflux.loader.mod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

            fillAddons();

            if (addons.isEmpty()) {
                PtfLogger.info("No mods found !", LogCategories.MOD_LOADER);
                return;
            }

            for (Class<?> clazz : addons)
                listModIfValid(clazz);

        } finally {
            // replace normal classLoader
            if (isModClassLoaderActive) setNormalClassLoader();
        }
    }

    /**
     * First Checks if the mod is valid, then list it if so.
     * @param clazz the main class of the mod to load (should be annotated with {@link Mod})
     */
    private static void listModIfValid(Class<?> clazz) {
        Mod modAnnotation = clazz.getAnnotation(Mod.class);

        if (isModIncorrectAfterCheck(
                clazz, modAnnotation
        )) return;

        boolean hasBeenListed = listMod(modAnnotation, clazz);

        if (hasBeenListed)
            PtfLogger.info("Listed addon: " + clazz.getName() + ", modId '" + modAnnotation.modId() + "'", LogCategories.MOD_LOADER);

        else PtfLogger.error("Failed to list mod : " + clazz.getName() + " ! Skipping...", LogCategories.MOD_LOADER);
    }

    /**
     * Checks the mod for validity to listing
     * @param clazz the potential mod's main class
     * @param modAnnotation the mod's {@link Mod} annotation
     * @return if the mod couldn't be listed
     */
    private static boolean isModIncorrectAfterCheck(Class<?> clazz, Mod modAnnotation) {
        return noModAnnotation(clazz, modAnnotation) ||
                isModIdIncorrect(clazz, modAnnotation);
    }

    /**
     * Checks if the annotation of the mod is correct (not already loaded and not illegal)
     * @param clazz the potential mod's main class
     * @param modAnnotation the {@link Mod} annotation of the class.
     * @return if the modId is incorrect
     */
    private static boolean isModIdIncorrect(Class<?> clazz, Mod modAnnotation) {
        if (PotoFluxLoadingContext.isModLoaded(modAnnotation)) {
            PtfLogger.warning("Mod with modId '" + modAnnotation.modId() + "' is already loaded, or it's modId is illegal ! Skipping " + clazz.getName(),
                    LogCategories.MOD_LOADER);
            return true;

        }
        else return false;
    }

    /**
     * Checks if the mod has the {@link Mod} annotation.<br>
     * Every classes getting there should have it, because reflection is filtered with it.
     * @param clazz the potential mod's main class
     * @param modAnnotation the {@link Mod} annotation of the class.
     * @return if the {@code modAnnotation} exists
     */
    private static boolean noModAnnotation(Class<?> clazz, Mod modAnnotation) {
        if (modAnnotation == null) {

            PtfLogger.warning("Class " + clazz.getName() + " passed the check but is missing @Mod annotation ! Skipping...",
                    LogCategories.MOD_LOADER); // should never append : we have here all classes annotated, got from reflection
            return true;

        }
        else return false;
    }

    private void fillAddons() {
        if (isProdEnv()) {

            // ===== PROD =====
            addons = getModProdEnv();

        } else {

            // ===== DEV =====
            addons = getModsDevEnv();

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
        setModClassLoader();

        Set<Class<?>> addons = new HashSet<>();

        // stream = all jar files
        try (DirectoryStream<Path> stream = getModStream()) {

            // for each, make the jarFile
            for (Path jarPath : stream)
                try (JarFile jar = getJar(jarPath)) {

                    PtfLogger.info("Scanning jar " + jar.getName(), LogCategories.MOD_LOADER);

                    // list all jar entries (so classes)
                    Enumeration<JarEntry> entries = jar.entries();

                    // process classes
                    while (entries.hasMoreElements()) {

                        JarEntry entry = entries.nextElement();
                        if (isNotClass(entry)) continue; // continue on all non-class files

                        String className = getClassName(entry);

                        try {
                            // turn to class
                            Class<?> clazz = getClassForName(className);

                            if (isModPresent(clazz)) {

                                PtfLogger.info("Found @Mod class: " + clazz.getName(), LogCategories.MOD_LOADER);
                                addons.add(clazz);

                            }

                        } catch (ClassNotFoundException | NoClassDefFoundError e) {
                            e.printStackTrace();
                            PtfLogger.error("A class doesn't exist or is not loaded !", LogCategories.MOD_LOADER);
                        }

                    }
                }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addons;
    }

    @Nonnull
    private static JarFile getJar(Path jarPath) throws IOException {
        return new JarFile(jarPath.toFile());
    }

    private static boolean isNotClass(JarEntry entry) {
        return !entry.getName().endsWith(".class");
    }

    @Nonnull
    private static DirectoryStream<Path> getModStream() throws IOException {
        return Files.newDirectoryStream(getPotofluxModDir(), "*.jar");
    }

    @Nonnull
    private static Class<?> getClassForName(String className) throws ClassNotFoundException {
        return Class.forName(className, false, getModsClassLoader());
    }

    private static boolean isModPresent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Mod.class);
    }

    @Nonnull
    private static String getClassName(JarEntry entry) {
        return entry.getName()
                .replace('/', '.')
                .replace(".class", "");
    }
}

