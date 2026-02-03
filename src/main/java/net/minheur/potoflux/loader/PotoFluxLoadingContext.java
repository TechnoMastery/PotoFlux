package net.minheur.potoflux.loader;

import com.google.gson.reflect.TypeToken;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.Json;
import org.reflections.vfs.Vfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This is the main class which handles the loading of potoflux and all mods related compounds
 */
public class PotoFluxLoadingContext {
    /**
     * This unique instance of this class.<br>
     * Used to instance things that should only exist once.
     */
    private static final PotoFluxLoadingContext INSTANCE = new PotoFluxLoadingContext();
    /**
     * The unique modEventBus used.<br>
     * It handles registration to different events
     */
    private final ModEventBus modEventBus = new ModEventBus();

    /**
     * List of all listed mods (in the mod dir), with their annotation and their main class
     */
    private static final Map<Mod, Class<?>> listedMods = new HashMap<>();
    /**
     * All mods that are loaded (will actually be executed on potoflux launch)
     */
    private static final Map<String, Class<?>> loadedMods = new HashMap<>();
    /**
     * List of all mod IDs to load
     */
    private static final List<String> modsToLoad = new ArrayList<>();

    /**
     * List of all modIds that cannot be claimed by mod, as they are used in potoflux core features
     */
    private static final List<String> illegalModIds = new ArrayList<>();
    static {
        illegalModIds.add(PotoFlux.ID);
        illegalModIds.add("file");
        illegalModIds.add("common");
    }

    static {
        Vfs.setDefaultURLTypes(List.of(
                Vfs.DefaultUrlTypes.jarFile,
                Vfs.DefaultUrlTypes.jarUrl,
                Vfs.DefaultUrlTypes.directory
        ));
    }

    /**
     * This toggles if the app runs in a dev environment.<br>
     * Using dev env is important in mod loading.
     */
    private static boolean isDevEnv = false;
    /**
     * This toggle if the environment has been set : we don't want to switch to dev env in mid potoflux running
     */
    private static boolean isEnvSet = false;

    /**
     * Stores the class loader used for loading mod's class
     */
    private static URLClassLoader modsClassLoader = null;

    /**
     * Stores all optional features
     */
    private static final Properties optionalFeatures = new Properties();

    /**
     * Make sure no one can create a second loading context.
     */
    private PotoFluxLoadingContext() {}

    /**
     * Getter for the only loading context instance
     * @return the potoflux loading context instance
     */
    public static PotoFluxLoadingContext get() {
        return INSTANCE;
    }

    /**
     * Getter for the only mod event bus
     * @return the mod event bus
     */
    public ModEventBus getModEventBus() {
        return modEventBus;
    }

    /**
     * This should only be called once in the scope, setting the environment
     * @param pIsDevEnv if the environment is dev
     */
    public static void setDevEnv(boolean pIsDevEnv) {
        if (isEnvSet) {
            PtfLogger.error("Environment can't be set to dev twice !");
            return;
        }
        isDevEnv = pIsDevEnv;
        isEnvSet = true;
    }
    /**
     * Checks if the env has been set
     * @return if the env has been set
     */
    public static boolean isDevEnv() {
        return isDevEnv;
    }

    /**
     * Called to load optional features from the file
     */
    public static void loadFeatures() {
        Path featuresPath = PotoFlux.getProgramDir().resolve("optionalFeatures.properties");


        if (Files.notExists(featuresPath)) {
            try {
                Files.createDirectories(featuresPath.getParent());
                Files.createFile(featuresPath);
            } catch (IOException e) {
                e.printStackTrace();
                PtfLogger.error("Could not create optionalFeatures.properties !");
            }
        }
        else try (InputStream in = Files.newInputStream(featuresPath)) {
            optionalFeatures.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get optionalFeatures.properties !");
        }
    }
    /**
     * Getter for the optional features
     * @return the optional features
     */
    public static Properties getOptionalFeatures() {
        return optionalFeatures;
    }

    /**
     * Used to get the URLs to scan for mods in dev environment
     * @return the URLs to scan for mod in dev env
     */
    public static Collection<URL> getDevScanUrls() {
        if (!isDevEnv()) return Collections.emptyList();

        try {
            Path classes = Paths.get(
                    System.getProperty("user.dir"),
                    "build", "classes", "java", "main"
            );
            return List.of(classes.toUri().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create and fill a class loader in prod environment
     * @return a class loader filled with prod mods
     */
    private static URLClassLoader mkModClassLoader() {
        Path modsDir = PotoFlux.getProgramDir().resolve("mods");

        try {
            Files.createDirectories(modsDir);

            List<URL> urls = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir, "*.jar")) {
                for (Path jar : stream) {
                    URL url = jar.toUri().toURL();
                    urls.add(url);
                    PtfLogger.info("Mod jar detected: " + url, LogCategories.MOD_LOADER);
                }
            }

            return new URLClassLoader(
                    urls.toArray(new URL[0]),
                    PotoFluxLoadingContext.class.getClassLoader() // parent = app
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to create mods classloader", e);
        }
    }

    /**
     * Get all addons existing in the mods folder, using the mod class loader from {@link #mkModClassLoader()}
     * @return all the @{@link Mod} annotated classes (mods)
     */
    public static Set<Class<?>> getAddons() {
        if (modsClassLoader == null) {
            modsClassLoader = mkModClassLoader();
            Thread.currentThread().setContextClassLoader(modsClassLoader);
        }

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

    /**
     * Getter for the mod class loader (prod)
     * @return the prod class loader
     */
    public static ClassLoader getModsClassLoader() {
        return modsClassLoader;
    }

    /**
     * Checks if a mod is loaded
     * @param mod the mod to check loading status
     * @return if the mod is loaded
     */
    public static boolean isModLoaded(Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return loadedMods.containsKey(mod.modId());
    }
    /**
     * Checks if a mod is loaded
     * @param modId the modID to check loading status
     * @return if the mod is loaded
     */
    public static boolean isModLoaded(String modId) {
        if (illegalModIds.contains(modId)) return true;
        return loadedMods.containsKey(modId);
    }

    /**
     * Checks if a mod is listed (known)
     * @param mod the mod to check if listed
     * @return if the mod is listed
     */
    public static boolean isModListed(Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return listedMods.containsKey(mod);
    }
    /**
     * Checks if a mod is listed (known)
     * @param modId the modID to check if listed
     * @return if the mod is listed
     */
    public static boolean isModListed(String modId) {
        if (illegalModIds.contains(modId)) return true;
        for (Mod entry : listedMods.keySet())
            if (entry.modId().equals(modId)) return true;
        return false;
    }
    /**
     * Gets a version of a mod.
     * @param modId mod to get version
     * @return the mod version if listed, {@code null} else
     */
    public static String getModVersion(String modId) {
        if (!isModListed(modId)) return null;

        for (Mod entry : listedMods.keySet())
            if (entry.modId().equals(modId)) return entry.version();

        return null;
    }

    /**
     * Registers a mod to the listed
     * @param mod mod to list
     * @param modClass the main class (annotated with {@link Mod}) of the mod
     * @return if the mod has been listed successfully
     */
    public static boolean listMod(Mod mod, Class<?> modClass) {
        if (isModLoaded(mod)) return false;
        listedMods.put(mod, modClass);
        return true;
    }
    /**
     * File the {@link #modsToLoad} list with the list from the file
     */
    private static void registerModList() {
        Path modListPath = PotoFlux.getProgramDir().resolve("modList.json");

        if (Files.notExists(modListPath)) {
            try {
                Files.writeString(modListPath, "[]", StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create modList.json !", e);
            }
        }

        try {
            String content = Files.readString(modListPath);

            List<String> loadedModIds = Json.GSON.fromJson(
                    content,
                    new TypeToken<List<String>>() {}.getType()
            );

            modsToLoad.clear();
            modsToLoad.addAll(loadedModIds);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            PtfLogger.error("Failed to read modList.json !", LogCategories.MOD_LOADER);
        }
    }
    /**
     * Load mods that in {@link #listedMods} and in the {@link #modsToLoad}.<br>
     * TODO: if the optional feature {@code catalogTab} is not enabled, all listed mods will be loaded.
     */
    public static void loadMods() {
        registerModList();

        boolean hasCatalogTab = Boolean.parseBoolean(getOptionalFeatures().getProperty("catalogTab"));

        if (modsToLoad.isEmpty() && hasCatalogTab) return;

        for (Map.Entry<Mod, Class<?>> entry : listedMods.entrySet()) {
            if (modsToLoad.contains(entry.getKey().modId()) || !hasCatalogTab) {
                if (Arrays.stream(
                        entry.getKey().compatibleVersions()
                ).toList().contains(
                        PotoFlux.getVersion()
                )) {
                    loadedMods.put(
                            entry.getKey().modId(),
                            entry.getValue()
                    );
                    PtfLogger.info("Loaded mod: " + entry.getKey().modId(), LogCategories.MOD_LOADER);
                }
                else {
                    modsToLoad.remove(entry.getKey().modId());
                    PtfLogger.error("Can't load incompatible mod: " + entry.getKey().modId(), LogCategories.MOD_LOADER);
                }
            }
        }
    }
    /**
     * Getter for a list of all loaded mods
     * @return a list of all loaded mod IDs
     */
    public static List<String> getLoadedMods() {
        return loadedMods.keySet().stream().toList();
    }
    /**
     * Used to save the mod list before exiting.
     */
    public static void saveModList() {
        Path modListPath = PotoFlux.getProgramDir().resolve("modList.json");

        String content = Json.GSON.toJson(modsToLoad);

        if (Files.notExists(modListPath)) {
            try {
                Files.writeString(modListPath, content, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create modList.json !", e);
            }
        }
    }
}
