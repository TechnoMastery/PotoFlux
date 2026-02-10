package net.minheur.potoflux.loader;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.Json;
import org.reflections.vfs.Vfs;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.List;

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
     * Checks online for a newer version of potoflux.<br>
     * If there is one available, tells the user.
     */
    public static void checkUpdates() {
        try {
            String target = "https://technomastery.github.io/PotoFluxAppData/ptfVersion/main.json";

            String lastest = Json.getFromObject(target, "lastestVersion");

            if (lastest == null) {
                PtfLogger.error("Could not get online version of potoflux !");
                return;
            }
            if (lastest.equals(PotoFlux.getVersion())) return;

            PtfLogger.info("New version of PotoFlux available ! (" + PotoFlux.getVersion() + " → " + lastest + ")");
            showUpdateContextDialog(lastest);
        } catch (Exception e) {
            e.printStackTrace();
            PtfLogger.error("Could not get lastest version online file !");
        }
    }
    /**
     * Show the user a dialog to inform that there's a new potoflux version available
     * @param lastest the version to tell the user it's the lastest
     */
    private static void showUpdateContextDialog(String lastest) {
        int update = JOptionPane.showConfirmDialog(null, "New version of PotoFlux available !", "Update", JOptionPane.OK_CANCEL_OPTION);
        if (update == JOptionPane.OK_OPTION) {

            String url = "https://github.com/TechnoMastery/PotoFlux/releases/tag/" + lastest;
            boolean hasBrowse = Functions.browse(url);

            if (!hasBrowse) PtfLogger.error("Failed to open potoflux update page !");

        }
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
     * Checks if the env is dev
     * @return if the env is dev
     */
    public static boolean isDevEnv() {
        return isDevEnv;
    }
    /**
     * Checks if the env is prod
     * @return if the env is prod
     */
    public static boolean isProdEnv() {
        return !isDevEnv;
    }

    /**
     * Called to load optional features from the file
     */
    public static void loadFeatures() {
        Path featuresPath = PotoFlux.getProgramDir().resolve("optionalFeatures.properties");

        if (Files.notExists(featuresPath))
            createOptionalFeatures(featuresPath);

        else try (InputStream in = Files.newInputStream(featuresPath)) {

            optionalFeatures.load(in);

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get optionalFeatures.properties !");
        }
    }

    /**
     * Creates the {@code optionalFeatures.properties} file
     * @param featuresPath the path to create the file to
     */
    private static void createOptionalFeatures(Path featuresPath) {
        try {
            Files.createDirectories(featuresPath.getParent());
            Files.createFile(featuresPath);

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not create optionalFeatures.properties !");
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
    public static URLClassLoader mkModClassLoader() {
        Path modsDir = PotoFlux.getProgramDir().resolve("mods");

        try {
            Files.createDirectories(modsDir);
            List<URL> urls = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir, "*.jar")) {

                for (Path jar : stream)
                    registerJar(jar, urls);

            }

            return buildModClassLoader(urls);

        } catch (IOException e) {
            throw new RuntimeException("Failed to create mods classloader", e);
        }
    }

    /**
     * Creates a parametrized class loader with a set of URLs
     * @param urls the list of URLs loaded by the class loader
     * @return a built class loader with the URLs
     */
    @Nonnull
    private static URLClassLoader buildModClassLoader(List<URL> urls) {
        return new URLClassLoader(
                urls.toArray(new URL[0]),
                PotoFluxLoadingContext.class.getClassLoader() // parent = app
        );
    }

    /**
     * Registers a jar in the URLs
     * @param jarPath the path to the file to register
     * @param urls the list to add the jar to
     * @throws MalformedURLException if the URL is incorrect (from the path)
     */
    private static void registerJar(Path jarPath, List<URL> urls) throws MalformedURLException {
        URL jarURL = jarPath.toUri().toURL();
        urls.add(jarURL);
        PtfLogger.info("Jar detected: " + jarURL, LogCategories.MOD_LOADER);
    }

    /**
     * Gets the active {@link ClassLoader}.<br>
     * Used when switching from main to mod class loader
     * @return the active {@link ClassLoader}
     */
    public static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    /**
     * Turns on the mod class loader
     */
    public static void setModClassLoader() {
        Thread.currentThread().setContextClassLoader(
                getModsClassLoader()
        );
    }

    /**
     * Getter for the potoflux mods dir
     * @return the potoflux mods dir
     */
    public static Path getPotofluxModDir() {
        return PotoFlux.getProgramDir().resolve("mods");
    }

    /**
     * Getter for the mod class loader (prod)
     * @return the prod class loader
     */
    public static ClassLoader getModsClassLoader() {
        if (modsClassLoader == null)
            modsClassLoader = mkModClassLoader();

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
     * Fills the {@link #modsToLoad} list with the list from the file
     */
    private static void registerModList() {
        Path modListPath = PotoFlux.getProgramDir().resolve("modList.json");

        if (Files.notExists(modListPath))
            createModListFile(modListPath);

        try {
            List<String> loadedModIds = getLoadedModIds(modListPath);

            resetModToLoadWith(loadedModIds);

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            PtfLogger.error("Failed to read modList.json !", LogCategories.MOD_LOADER);
        }
    }
    /**
     * Gets a list of modIds to load from the path of the JSON file containing them
     * @param modListPath the path to the {@code modList.json}  file
     * @return the list of modIds to load
     * @throws IOException if couldn't read the content of the file
     */
    private static List<String> getLoadedModIds(Path modListPath) throws IOException {
        String content = Files.readString(modListPath);

        return Json.GSON.fromJson(
                content,
                new TypeToken<List<String>>() {}.getType()
        );
    }
    /**
     * Creates the {@code modList.json} file to the given path
     * @param modListPath where to create the mod list file
     */
    private static void createModListFile(Path modListPath) {
        try {
            Files.writeString(modListPath, "[]", StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create modList.json !", e);
        }
    }
    /**
     * Replaces all content of {@link #modsToLoad} with the given one
     * @param listToLoad content to print in {@link #modsToLoad}.
     */
    private static void resetModToLoadWith(List<String> listToLoad) {
        modsToLoad.clear();
        modsToLoad.addAll(listToLoad);
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
            Mod mod = entry.getKey();

            if (modsToLoad.contains(mod.modId()) || !hasCatalogTab) {

                List<String> compatibleVersions =
                        Arrays.stream(
                                mod.compatibleVersions()
                        ).toList();
                boolean isCompatible = false;

                // check if using online compatible
                if (compatibleVersions.contains("-1"))
                {

                    // check if online list exists
                    if (checkOnlineModList(mod)) continue;

                    // gets list
                    try {

                        JsonObject versionObject = Json.getOnlineJsonObject(mod.compatibleVersionUrl());

                        if (versionObject == null) {
                            modsToLoad.remove(mod.modId());
                            PtfLogger.error("Could not get corresponding online version for mod " + mod.modId() + ", for version " + mod.version(),
                                    LogCategories.MOD_LOADER);
                            continue;
                        }

                        List<String> compatibleVersionList = Json.listFromObject(versionObject, mod.version());

                        if (compatibleVersionList.isEmpty()) {
                            modsToLoad.remove(mod.modId());
                            PtfLogger.error("Empty online compatible version list for mod: " + mod.modId(), LogCategories.MOD_LOADER);
                            continue;
                        }

                        if (compatibleVersionList.contains(PotoFlux.getVersion())) isCompatible = true;

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        PtfLogger.error("Failed to get online compatible version list for mod: " + mod.modId(), LogCategories.MOD_LOADER);
                        continue;
                    }

                }
                else if (compatibleVersions.contains(PotoFlux.getVersion()))
                    isCompatible = true;

                if (isCompatible)
                {

                    try { // try to create mod

                        Object instance = entry.getValue().getDeclaredConstructor().newInstance();

                        loadedMods.put(
                                entry.getKey().modId(),
                                entry.getValue()
                        );
                        PtfLogger.info("Loaded mod: " + entry.getKey().modId() + " in version " + entry.getKey().version(), LogCategories.MOD_LOADER);

                    } catch (Exception e) {
                        e.printStackTrace();
                        PtfLogger.error("Couldn't instance mod: " + entry.getKey().modId());
                    }

                }
                else {
                    modsToLoad.remove(entry.getKey().modId());
                    PtfLogger.error("Can't load incompatible mod: " + entry.getKey().modId(), LogCategories.MOD_LOADER);
                }
            }
        }
    }

    private static boolean checkOnlineModList(Mod mod) {
        if (mod.compatibleVersionUrl().equals("NONE")) {
            modsToLoad.remove(mod.modId());
            PtfLogger.error("No compatible version list system set for mod: " + mod.modId(), LogCategories.MOD_LOADER);
            return true;
        }
        return false;
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
