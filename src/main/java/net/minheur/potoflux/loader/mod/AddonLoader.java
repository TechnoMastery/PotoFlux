package net.minheur.potoflux.loader.mod;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static net.minheur.potoflux.loader.PotoFluxLoadingContext.*;

/**
 * Addon loader class: loads all addons for {@link PotoFluxLoadingContext}
 */
public class AddonLoader {

    /**
     * The available classes, listed in the mods dir's jars
     */
    private static final List<String> availableClasses = new ArrayList<>();

    /**
     * The normal class loader, to restore it once loading done.
     */
    private ClassLoader normalClassLoader;
    /**
     * The addons, returned to {@link PotoFluxLoadingContext}.
     */
    private Set<Class<?>> addons;

    /**
     * First Checks if the mod is valid, then list it if so.
     *
     * @param clazz the main class of the mod to load (should be annotated with {@link Mod})
     */
    private static void listModIfValid(@NotNull Class<?> clazz) {
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
     *
     * @param clazz         the potential mod's main class
     * @param modAnnotation the mod's {@link Mod} annotation
     * @return if the mod couldn't be listed
     */
    private static boolean isModIncorrectAfterCheck(Class<?> clazz, Mod modAnnotation) {
        return noModAnnotation(clazz, modAnnotation) ||
                isModIdIncorrect(clazz, modAnnotation);
    }

    /**
     * Checks if the annotation of the mod is correct (not already loaded and not illegal)
     *
     * @param clazz         the potential mod's main class
     * @param modAnnotation the {@link Mod} annotation of the class.
     * @return if the modId is incorrect
     */
    private static boolean isModIdIncorrect(Class<?> clazz, Mod modAnnotation) {
        if (PotoFluxLoadingContext.isModLoaded(modAnnotation)) {
            PtfLogger.warning("Mod with modId '" + modAnnotation.modId() + "' is already loaded, or it's modId is illegal ! Skipping " + clazz.getName(),
                    LogCategories.MOD_LOADER);
            return true;

        } else return false;
    }

    /**
     * Checks if the mod has the {@link Mod} annotation.<br>
     * Every classes getting there should have it, because reflection is filtered with it.
     *
     * @param clazz         the potential mod's main class
     * @param modAnnotation the {@link Mod} annotation of the class.
     * @return if the {@code modAnnotation} exists
     */
    private static boolean noModAnnotation(Class<?> clazz, Mod modAnnotation) {
        if (modAnnotation == null) {

            PtfLogger.warning("Class " + clazz.getName() + " passed the check but is missing @Mod annotation ! Skipping...",
                    LogCategories.MOD_LOADER); // should never append : we have here all classes annotated, got from reflection
            return true;

        } else return false;
    }

    private static @NotNull Reflections getDevReflection(Collection<URL> urls) {
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

    /**
     * Gets the mod in prod env.
     * @return prod env's mods
     */
    public static @NotNull Set<Class<?>> getModProdEnv() {
        setModClassLoader();

        Set<Class<?>> addons = new HashSet<>();

        // stream = all jar files
        try {

            // for each, make the jarFile
            for (Path jarPath : getModStream())
                try (JarFile jar = getJar(jarPath)) {

                    PtfLogger.info("Scanning jar " + jar.getName(), LogCategories.MOD_LOADER);

                    // list all jar entries (so classes)
                    Enumeration<JarEntry> entries = jar.entries();

                    // process classes
                    while (entries.hasMoreElements()) {

                        JarEntry entry = entries.nextElement();
                        if (isNotClass(entry)) continue; // continue on all non-class files

                        String className = getClassName(entry);

                        availableClasses.add(className);

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

    /**
     * Gets the {@linkplain JarFile} for a given path.
     * @param jarPath path to the jar file
     * @return the {@link JarFile}
     * @throws IOException if the file couldn't be accessed or doesn't exist
     */
    @Contract("_ -> new")
    private static @NotNull JarFile getJar(@NotNull Path jarPath) throws IOException {
        return new JarFile(jarPath.toFile());
    }

    /**
     * Checks if a {@linkplain JarEntry} is not a class.
     * @return whether the {@linkplain JarEntry} is not class
     */
    private static boolean isNotClass(@NotNull JarEntry entry) {
        return !entry.getName().endsWith(".class");
    }

    /**
     * Gets the mod stream.
     * @return the mod stream
     * @throws IOException on file listing fail
     */
    private static @NotNull List<Path> getModStream() throws IOException {
        try (Stream<Path> stream = Files.walk(getPotofluxModDir())) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .toList();
        }
    }

    /**
     * Gets a class from its name, in the mod class loader.
     * @param className the class's name
     * @return the mod class loader's class for the specified name
     */
    private static @NotNull Class<?> getClassForName(String className) throws ClassNotFoundException {
        return Class.forName(className, false, getModsClassLoader());
    }

    /**
     * Checks if @{@link Mod} is present on a class.
     * @param clazz the class to check
     * @return whether it mod present
     */
    @Contract(pure = true)
    private static boolean isModPresent(@NotNull Class<?> clazz) {
        return clazz.isAnnotationPresent(Mod.class);
    }

    private static @NotNull String getClassName(@NotNull JarEntry entry) {
        return entry.getName()
                .replace('/', '.')
                .replace(".class", "");
    }

    /**
     * Gets the available classes.
     * @return the available classes
     */
    public static List<String> getAvailableClasses() {
        return availableClasses;
    }

    /**
     * Checks if a class is in {@linkplain #availableClasses}.
     * @param path the path to the class
     * @return whether it class available
     */
    public static boolean isClassAvailable(String path) {
        return availableClasses.contains(path);
    }

    /**
     * Loads the addons.
     */
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
     * Fills the addons, which differs depending on the environment ({@code dev} or {@code prod})
     */
    private void fillAddons() {
        if (isProdEnv()) {

            // ===== PROD =====
            addons = getModProdEnv();

        } else {

            // ===== DEV =====
            addons = getModsDevEnv();

        }
    }

    /**
     * Gets the mods in dev env.
     * @return the mods dev env
     */
    private Set<Class<?>> getModsDevEnv() {
        Collection<URL> urls = getDevScanUrls();

        if (urls.isEmpty()) {
            PtfLogger.info("No mod file found !", LogCategories.MOD_LOADER);
            return new HashSet<>();
        }

        Reflections reflections = getDevReflection(urls);

        return reflections.getTypesAnnotatedWith(Mod.class);
    }

    /**
     * Saves the normal class loader, to restore it later
     */
    private void saveNormalClassLoader() {
        normalClassLoader = getCurrentClassLoader();
    }

    /**
     * Sets the normal class loader, after mod loading
     */
    private void setNormalClassLoader() {
        if (normalClassLoader == null)
            throw new IllegalThreadStateException("Normal class loader not saved ! Thread class loader stuck as mod !");

        Thread.currentThread().setContextClassLoader(
                normalClassLoader
        );
    }
}

