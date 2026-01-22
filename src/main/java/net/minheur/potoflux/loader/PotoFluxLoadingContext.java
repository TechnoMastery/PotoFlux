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

public class PotoFluxLoadingContext {
    private static final PotoFluxLoadingContext INSTANCE = new PotoFluxLoadingContext();
    private final ModEventBus modEventBus = new ModEventBus();

    private static final Map<Mod, Class<?>> listedMods = new HashMap<>();
    private static final Map<String, Class<?>> loadedMods = new HashMap<>();
    private static final List<String> modsToLoad = new ArrayList<>();

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

    private static boolean isDevEnv = false;
    private static boolean isEnvSet = false;

    private static URLClassLoader modsClassLoader = null;

    private static final Properties optionalFeatures = new Properties();

    private PotoFluxLoadingContext() {}

    public static PotoFluxLoadingContext get() {
        return INSTANCE;
    }

    public ModEventBus getModEventBus() {
        return modEventBus;
    }

    public static void setDevEnv(boolean pIsDevEnv) {
        if (isEnvSet) {
            PtfLogger.error("Environment can't be set to dev twice !");
            return;
        }
        isDevEnv = pIsDevEnv;
        isEnvSet = true;
    }
    public static boolean isDevEnv() {
        return isDevEnv;
    }

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
    public static Properties getOptionalFeatures() {
        return optionalFeatures;
    }

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

    public static ClassLoader getModsClassLoader() {
        return modsClassLoader;
    }

    public static boolean isModLoaded(Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return loadedMods.containsKey(mod.modId());
    }
    public static boolean isModLoaded(String modId) {
        if (illegalModIds.contains(modId)) return true;
        return loadedMods.containsKey(modId);
    }

    public static boolean isModListed(Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return listedMods.containsKey(mod);
    }
    public static boolean isModListed(String modId) {
        if (illegalModIds.contains(modId)) return true;
        for (Mod entry : listedMods.keySet())
            if (entry.modId().equals(modId)) return true;
        return false;
    }
    public static String getModVersion(String modId) {
        if (!isModListed(modId)) return null;

        for (Mod entry : listedMods.keySet())
            if (entry.modId().equals(modId)) return entry.version();

        return null;
    }

    public static boolean listMod(Mod mod, Class<?> modClass) {
        if (isModLoaded(mod)) return false;
        listedMods.put(mod, modClass);
        return true;
    }
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
