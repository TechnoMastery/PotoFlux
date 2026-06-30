package net.minheur.potoflux.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.mod.*;
import net.minheur.potoflux.loader.mod.errors.LoadModError;
import net.minheur.potoflux.loader.mod.errors.ModErrorReg;
import net.minheur.potoflux.loader.mod.update.ModUpdateReg;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;
import net.minheur.potoflux.utils.Json;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.reflections.vfs.Vfs;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This is the main class which handles the loading of potoflux and all mods related compounds
 */
public final class PotoFluxLoadingContext {
    /**
     * This unique instance of this class.<br>
     * Used to instance things that should only exist once.
     */
    private static final PotoFluxLoadingContext INSTANCE = new PotoFluxLoadingContext();
    /**
     * List of all listed mods (in the mod dir), with their annotation and their main class
     */
    private static final List<ModContainer> listedMods = new ArrayList<>();
    /**
     * All mods that are loaded (will actually be executed on potoflux launch)
     */
    private static final Map<String, Class<?>> loadedMods = new HashMap<>();
    /**
     * List of all modIds that cannot be claimed by mod, as they are used in potoflux core features
     */
    private static final List<String> illegalModIds = new ArrayList<>();
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
     * The unique modEventBus used.<br>
     * It handles registration to different events
     */
    private final ModEventBus modEventBus = new ModEventBus();

    /**
     * Make sure no one can create a second loading context.
     */
    private PotoFluxLoadingContext() {
    }

    /**
     * Getter for the only loading context instance
     *
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
        if (isDevEnv()) return;

        try {
            String target = "https://technomastery.github.io/PotoFluxAppData/ptfVersion/main.json";

            JsonElement lastest = Json.getFromObject(target, "lastestVersion");

            if (lastest == null) {
                PtfLogger.error("Could not get online version of potoflux !");
                return;
            }
            if (lastest.getAsString().equals(PotoFlux.getVersion())) return;

            JsonElement allPtfVersions = Json.getFromObject(target, "versions");
            if (allPtfVersions != null) {
                JsonObject versionObject = allPtfVersions.getAsJsonObject().getAsJsonObject(PotoFlux.getVersion());
                JsonElement type = versionObject.get("type");
                if (type != null && type.getAsString().equals("Release candidate")) return;
            }


            PtfLogger.info("New version of PotoFlux available ! (" + PotoFlux.getVersion() + " → " + lastest.getAsString() + ")");
            showUpdateContextDialog(lastest.getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            PtfLogger.error("Could not get lastest version online file !");
        }
    }

    /**
     * Show the user a dialog to inform that there's a new potoflux version available
     *
     * @param lastest the version to tell the user it's the lastest
     */
    private static void showUpdateContextDialog(String lastest) {
        boolean confirmed = UiUtils.showConfirmationDialog(
                new Label(Translations.get("potoflux:ptfUpdate.desc")),
                Translations.get("potoflux:ptfUpdate.title")
        );

        if (confirmed) {

            String url = "https://github.com/TechnoMastery/PotoFlux/releases/tag/" + lastest;
            boolean hasBrowse = Functions.browse(url);

            if (!hasBrowse) PtfLogger.error("Failed to open potoflux update page !");

        }
    }

    /**
     * Checks if the env is dev
     *
     * @return if the env is dev
     */
    public static boolean isDevEnv() {
        return isDevEnv;
    }

    /**
     * This should only be called once in the scope, setting the environment
     *
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
     * Checks if the env is prod
     *
     * @return if the env is prod
     */
    public static boolean isProdEnv() {
        return !isDevEnv;
    }

    /**
     * Used to get the URLs to scan for mods in dev environment
     *
     * @return the URLs to scan for mod in dev env
     */
    public static @NotNull Collection<URL> getDevScanUrls() {
        if (!isDevEnv()) return Collections.emptyList();

        try {
            Collection<URL> urls = new HashSet<>();

            // 1. current dev mod
            Path classes = Paths.get(
                    System.getProperty("user.dir"),
                    "build", "classes", "java", "main"
            );
            urls.add(classes.toUri().toURL());

            // 2. gradle deps
            String classpath = System.getProperty("java.class.path");

            for (String path : classpath.split(File.pathSeparator))
                urls.add(Paths.get(path).toUri().toURL());

            return urls;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create and fill a class loader in prod environment
     *
     * @return a class loader filled with prod mods
     */
    public static @NotNull URLClassLoader mkModClassLoader() {
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
     *
     * @param urls the list of URLs loaded by the class loader
     * @return a built class loader with the URLs
     */
    @Contract("_ -> new")
    private static @NotNull URLClassLoader buildModClassLoader(@NotNull List<URL> urls) {
        return new URLClassLoader(
                urls.toArray(new URL[0]),
                PotoFluxLoadingContext.class.getClassLoader() // parent = app
        );
    }

    /**
     * Registers a jar in the URLs
     *
     * @param jarPath the path to the file to register
     * @param urls    the list to add the jar to
     * @throws MalformedURLException if the URL is incorrect (from the path)
     */
    private static void registerJar(@NotNull Path jarPath, @NotNull List<URL> urls) throws MalformedURLException {
        URL jarURL = jarPath.toUri().toURL();
        urls.add(jarURL);
        PtfLogger.info("Jar detected: " + jarURL, LogCategories.MOD_LOADER);
    }

    /**
     * Gets the active {@link ClassLoader}.<br>
     * Used when switching from main to mod class loader
     *
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
     *
     * @return the potoflux mods dir
     */
    public static @NotNull Path getPotofluxModDir() {
        return PotoFlux.getProgramDir().resolve("mods");
    }

    /**
     * Getter for the mod class loader (prod)
     *
     * @return the prod class loader
     */
    public static ClassLoader getModsClassLoader() {
        if (modsClassLoader == null)
            modsClassLoader = mkModClassLoader();

        return modsClassLoader;
    }

    /**
     * Checks if a mod is loaded
     *
     * @param mod the mod to check loading status
     * @return if the mod is loaded
     */
    public static boolean isModLoaded(@NotNull Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return loadedMods.containsKey(mod.modId());
    }

    /**
     * Checks if a mod is loaded
     *
     * @param modId the modID to check loading status
     * @return if the mod is loaded
     */
    public static boolean isModLoaded(String modId) {
        if (illegalModIds.contains(modId)) return true;
        return loadedMods.containsKey(modId);
    }

    /**
     * Checks if a mod is listed (known)
     *
     * @param mod the mod to check if listed
     * @return if the mod is listed
     */
    public static boolean isModListed(@NotNull Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return isModListed(mod.modId());
    }

    /**
     * Checks if a mod is listed (known)
     *
     * @param modId the modID to check if listed
     * @return if the mod is listed
     */
    public static boolean isModListed(String modId) {
        if (illegalModIds.contains(modId)) return true;
        for (ModContainer entry : listedMods)
            if (entry.mod.modId().equals(modId)) return true;
        return false;
    }

    /**
     * Gets a version of a mod.
     *
     * @param modId mod to get version
     * @return the mod version if listed, {@code null} else
     */
    public static @Nullable String getModVersion(String modId) {
        if (!isModListed(modId)) return null;

        for (ModContainer entry : listedMods)
            if (entry.mod.modId().equals(modId)) return entry.mod.version();

        return null;
    }

    /**
     * Registers a mod to the listed
     *
     * @param mod      mod to list
     * @param modClass the main class (annotated with {@link Mod}) of the mod
     * @return if the mod has been listed successfully
     */
    public static boolean listMod(Mod mod, Class<?> modClass) {
        if (isModLoaded(mod)) return false;
        listedMods.add(new ModContainer(mod, modClass));
        return true;
    }

    /**
     * Load mods that in {@link #listedMods} and compatible.
     */
    public static void loadMods() {

        for (ModContainer entry : listedMods)
            loadMod(entry);

        ModErrorReg.close();
        ModUpdateReg.close();
    }

    private static @Nullable Boolean getIsCompatible(@NotNull Mod mod) {
        List<String> compatibleVersions =
                Arrays.stream(
                        mod.compatibleVersions()
                ).toList();
        boolean isCompatible = false;

        // check if using online compatible
        if (modUsesOnlineList(compatibleVersions)) {

            // check if online list exists
            if (checkOnlineListExists(mod)) return null;

            // gets list
            List<String> compatibleVersionList = getOnlineCompatibleList(mod);
            if (compatibleVersionList == null) return null;

            if (compatibleVersionList.contains(PotoFlux.getVersion())) isCompatible = true;

            checkUpdate(mod, isCompatible);

        } else if (compatibleVersions.contains(PotoFlux.getVersion()))
            isCompatible = true;
        return isCompatible;
    }

    /**
     * Checks for updates on a mod.
     * @param isCompatible is the mod compatible with actual Potoflux
     */
    private static void checkUpdate(@NotNull Mod mod, boolean isCompatible) {
        JsonObject mainObject = Json.getOnlineJsonObject(mod.compatibleVersionUrl());
        if (mainObject == null) return;
        JsonObject lastestObject = mainObject.getAsJsonObject("lastestForPtf");

        JsonElement element = lastestObject.get(PotoFlux.getVersion());
        if (element == null) return;
        String declaredLastest = element.getAsString();

        if (declaredLastest != null && !declaredLastest.equals(mod.version()))
            ModUpdateReg.add(mod, isCompatible, declaredLastest);
    }

    /**
     * Opens the update dialog for a mod
     * @param mod the mod
     * @param isCompatible weather the mod is compatible with actual Potoflux
     * @param lastest the lastest mod version available
     */
    public static void openUpdateDialog(Mod mod, boolean isCompatible, String lastest) {
        String message;

        if (isCompatible)
            message = Functions.formatMessage(Translations.get("potoflux:modUpdate.query.compatible"),
                    mod.modId(), mod.version(), lastest);
        else message = Functions.formatMessage(Translations.get("potoflux:modUpdate.query.notCompatible"),
                mod.modId(), lastest);

        boolean confirmed = UiUtils.showConfirmationDialog(
                new Label(message),
                Translations.get("potoflux:modUpdate.query.title"),
                isCompatible ? null : Alert.AlertType.WARNING
        );

        if (confirmed) {
            PtfLogger.info("User wants to update mod " + mod.modId(), LogCategories.MOD_UPDATE);
            openInstallModPage(mod, lastest);
        }
    }

    /**
     * Opens the download page for a mod.
     * @param version the version
     */
    private static void openInstallModPage(@NotNull Mod mod, String version) {
        JsonObject mainObject = Json.getOnlineJsonObject(mod.compatibleVersionUrl());
        if (mainObject == null) return;
        String installUrl = mainObject.get("installUrl").getAsString();

        if (installUrl == null || installUrl.equals("NONE")) {
            PtfLogger.error("No update link set.", LogCategories.MOD_UPDATE);
            JOptionPane.showMessageDialog(
                    null,
                    Translations.get("potoflux:modUpdate.dl.noLink"),
                    Translations.get("potoflux:modUpdate.dl.noLink.title"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String finalUrl = installUrl + version;
        boolean browsed = Functions.browse(finalUrl);

        if (browsed)
            PtfLogger.info("Opened install url in browser", LogCategories.MOD_UPDATE);
        else {
            PtfLogger.error("Failed to open install url in browser", LogCategories.MOD_UPDATE);
            JOptionPane.showMessageDialog(
                    null,
                    Translations.get("potoflux:modUpdate.dl.failed"),
                    Translations.get("potoflux:modUpdate.dl.failed.title"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Tries to load a mod.
     * @return the mod to load
     */
    private static LoadResult loadMod(@NotNull ModContainer entry) {

        // checks the state of the actual mod
        switch (entry.state) {
            case LOADING -> {
                PtfLogger.error(
                        "Circular dependency detected: " +
                                entry.mod.modId(),
                        LogCategories.MOD_LOADER
                );
                entry.state = ModState.circularLastest;
                return LoadResult.CIRCULAR;
            }
            case INCOMPATIBLE -> {
                return LoadResult.INCOMPATIBLE;
            }
            case CIRCULAR -> {
                return LoadResult.ALREADY_CIRCULAR;
            }
            case MISSING_DEPENDENCIES, DEPENDENCY_WRONG_VERSION -> {
                return LoadResult.DEPENDENCY_FAILED;
            }
            case FAILED -> {
                return LoadResult.FAILED;
            }
            case LOADED -> {
                return LoadResult.LOADED;
            }
            case circularLastest -> {
                PtfLogger.error("Found last of circular: " + entry.mod.modId(), LogCategories.MOD_DEPENDENCIES);
                entry.state = ModState.CIRCULAR;
                ModErrorReg.add(entry.mod, entry.state);
                return LoadResult.ALREADY_CIRCULAR;
            }
        }

        // start loading mod
        entry.state = ModState.LOADING;
        Mod mod = entry.mod;

        // compatibility check
        Boolean isCompatible = getIsCompatible(mod);
        if (isCompatible == null) {
            entry.state = ModState.FAILED;
            ModErrorReg.add(mod, entry.state);
            return LoadResult.FAILED;
        }
        if (!isCompatible) {
            entry.state = ModState.INCOMPATIBLE;
            ModErrorReg.add(mod, entry.state);
            return LoadResult.INCOMPATIBLE;
        }

        // load all deps
        for (String depFormated : mod.dependenciesIds()) {

            // unwraps dep infos
            Dependency dep = new Dependency(depFormated); // dep asked
            String depId = dep.id; // dep asked's id
            ModContainer actualDep = getListedMod(depId); // actual listed mod for the asked dep (null if not there)

            // checks dep presence
            if (actualDep == null) {
                PtfLogger.error(
                        "Missing dependency '" + depId + "' for mod " + mod.modId(),
                        LogCategories.MOD_DEPENDENCIES
                );
                entry.state = ModState.MISSING_DEPENDENCIES;
                ModErrorReg.add(new LoadModError(mod, entry.state, dep));
                return LoadResult.DEPENDENCY_FAILED;
            }

            // checks dep version
            String actualDepVersion = actualDep.mod.version();
            if (!dependencyIsCompatible(actualDepVersion, dep)) {
                PtfLogger.error(
                        "Mod" + mod.modId() + " require '" + dep.id + "' " +
                                (dep.minVersion.equals(dep.maxVersion) ? dep.minVersion :
                                        "between " + dep.minVersion + " and " + dep.maxVersion) +
                                ". Currently, it is " + actualDepVersion
                );
                entry.state = ModState.DEPENDENCY_WRONG_VERSION;
                ModErrorReg.add(new LoadModError(mod, entry.state, dep, actualDepVersion));
                return LoadResult.DEPENDENCY_FAILED;
            }

            // loads dep
            LoadResult depLoadingResult = loadMod(actualDep);
            switch (depLoadingResult) { // checks result
                case CIRCULAR -> {
                    PtfLogger.error(
                            "Mod " + mod.modId() + " is part of a circular dependency !", LogCategories.MOD_DEPENDENCIES
                    );
                    entry.state = ModState.CIRCULAR;
                    ModErrorReg.add(mod, entry.state);
                    return LoadResult.CIRCULAR;
                }
                case ALREADY_CIRCULAR -> {
                    PtfLogger.error("Mod " + mod.modId() + " failed because dependency " + depId + " was circular");
                    entry.state = ModState.MISSING_DEPENDENCIES;
                    ModErrorReg.add(new LoadModError(mod, entry.state, dep));
                    return LoadResult.DEPENDENCY_FAILED;
                }
                case DEPENDENCY_FAILED -> {
                    PtfLogger.error("Mod " + mod.modId() + " failed because dependency " + depId + " is missing dependencies");
                    entry.state = ModState.MISSING_DEPENDENCIES;
                    ModErrorReg.add(new LoadModError(mod, entry.state, dep));
                    return LoadResult.DEPENDENCY_FAILED;
                }
                case INCOMPATIBLE -> {
                    PtfLogger.error("Mod " + mod.modId() + " failed because dependency " + depId + " is incompatible");
                    entry.state = ModState.MISSING_DEPENDENCIES;
                    ModErrorReg.add(new LoadModError(mod, entry.state, dep));
                    return LoadResult.DEPENDENCY_FAILED;
                }
                case FAILED -> {
                    PtfLogger.error("Mod " + mod.modId() + " failed because dependency " + depId + " failed");
                    entry.state = ModState.MISSING_DEPENDENCIES;
                    ModErrorReg.add(new LoadModError(mod, entry.state, dep));
                    return LoadResult.DEPENDENCY_FAILED;
                }
            }

        }

        // check external deps
        if (!isDevEnv) // skip if dev env
            for (String depId : mod.externalDependencies()) {
                if (AddonLoader.isClassAvailable(depId)) continue;

                entry.state = ModState.FAILED;
                ModErrorReg.add(new LoadModError(mod, entry.state));
                return LoadResult.FAILED;

            }

        try { // try to create mod

            Object instance = entry.clazz.getDeclaredConstructor().newInstance();

            loadedMods.put(
                    entry.mod.modId(),
                    entry.clazz
            );
            PtfLogger.info("Loaded mod: " + entry.mod.modId() + " in version " + entry.mod.version(), LogCategories.MOD_LOADER);
            entry.state = ModState.LOADED;
            return LoadResult.LOADED;

        } catch (Exception e) {
            e.printStackTrace();
            PtfLogger.error("Couldn't instance mod: " + entry.mod.modId());
            entry.state = ModState.FAILED;
            ModErrorReg.add(mod, entry.state);
            return LoadResult.FAILED;
        }
    }

    /**
     * Weather a mod's dependency is compatible with actual mod
     * @param actualDepVersion the actual dependency's version
     * @param dep dependency required, containing target version
     * @return weather the dep is compatible
     */
    private static boolean dependencyIsCompatible(String actualDepVersion, @NotNull Dependency dep) {
        int intDepVersion = Integer.parseInt(actualDepVersion);
        int min = Integer.parseInt(dep.minVersion);
        int max = Integer.parseInt(dep.maxVersion);

        return intDepVersion <= max && intDepVersion >= min;
    }

    private static @Nullable ModContainer getListedMod(String modId) {
        for (ModContainer container : listedMods)
            if (container.mod.modId().equals(modId)) return container;
        return null;
    }

    /**
     * Checks if a mod uses online list.
     * @param compatibleVersions the compatible versions
     * @return weather the mod uses online list
     */
    @Contract(pure = true)
    private static boolean modUsesOnlineList(@NotNull List<String> compatibleVersions) {
        return compatibleVersions.contains("-1");
    }

    /**
     * Gets the online compatible list.
     * @param mod the mod
     * @return the mod's online compatible list
     */
    private static @Nullable List<String> getOnlineCompatibleList(Mod mod) {
        try {

            JsonObject mainObject = Json.getOnlineJsonObject(mod.compatibleVersionUrl());
            if (checkOnlineListNotnull(mainObject, mod)) return null;

            JsonObject versionsObject = mainObject.getAsJsonObject("versions");
            if (checkOnlineListNotnull(versionsObject, mod)) return null;

            JsonObject versionObject = versionsObject.getAsJsonObject(mod.version());
            if (checkOnlineListNotnull(versionObject, mod)) return null;

            List<String> compatibleVersionList = Json.listFromObject(versionObject, "compatList");
            if (checkOnlineListEmpty(compatibleVersionList, mod)) return null;

            return compatibleVersionList;

        } catch (Exception e) {
            e.printStackTrace();
            PtfLogger.error("Failed to get online compatible version list for mod: " + mod.modId(), LogCategories.MOD_LOADER);
            return null;
        }
    }

    /**
     * Checks if the online list is empty.
     * @param compatibleVersionList the compatible version list
     * @param mod the mod
     * @return weather the online list is empty
     */
    private static boolean checkOnlineListEmpty(@NotNull List<String> compatibleVersionList, Mod mod) {
        if (compatibleVersionList.isEmpty()) {
            PtfLogger.error("Empty online compatible version list for mod: " + mod.modId(), LogCategories.MOD_LOADER);
            return true;
        } else return false;
    }

    /**
     * Checks the online list isn't null.
     * @param versionObject the version object
     * @param mod the mod
     * @return weather the online list is null
     */
    private static boolean checkOnlineListNotnull(JsonObject versionObject, Mod mod) {
        if (versionObject == null) {
            PtfLogger.error("Could not get corresponding online version for mod " + mod.modId(),
                    LogCategories.MOD_LOADER);
            return true;
        } else return false;
    }

    /**
     * Checks if the online list exists.
     * @return weather the online list exists
     */
    private static boolean checkOnlineListExists(@NotNull Mod mod) {
        if (mod.compatibleVersionUrl().equals("NONE")) {
            PtfLogger.error("No compatible version list system set for mod: " + mod.modId(), LogCategories.MOD_LOADER);
            return true;
        } else return false;
    }

    /**
     * Getter for a list of all loaded mods
     *
     * @return a list of all loaded mod IDs
     */
    public static @NotNull @Unmodifiable List<String> getLoadedMods() {
        return loadedMods.keySet().stream().toList();
    }

    /**
     * Gets the listed mods.
     * @return copy of {@link #listedMods}
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull List<ModContainer> getListedMods() {
        return new ArrayList<>(listedMods); // safety copy
    }

    /**
     * Getter for the only mod event bus
     *
     * @return the mod event bus
     */
    public ModEventBus getModEventBus() {
        return modEventBus;
    }
}
