package net.minheur.potoflux.loader;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.utils.logger.LogCategories;
import net.minheur.potoflux.utils.logger.PtfLogger;
import org.reflections.vfs.Vfs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PotoFluxLoadingContext {
    private static final PotoFluxLoadingContext INSTANCE = new PotoFluxLoadingContext();
    private final ModEventBus modEventBus = new ModEventBus();
    private static final Map<String, Class<?>> loadedMods = new HashMap<>();
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

    public static Collection<URL> getScanUrls() {
        try {
            // DEV
            if (isDevEnv()) {
                Path classes = Paths.get(
                        System.getProperty("user.dir"),
                        "build", "classes", "java", "main"
                );
                return List.of(classes.toUri().toURL());
            } else return null;

        //     // PROD
        //     Path appDir = PotoFlux.getProgramDir();
        //     Path modsDir = appDir.resolve("mods");

        //     Files.createDirectories(modsDir);

        //     List<URL> urls = new ArrayList<>();
        //     try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir, "*.jar")) {
        //         for (Path jar : stream) urls.add(jar.toUri().toURL());
        //     }

        //     urls.forEach(u -> PtfLogger.info("Scanning URLs: " + u, LogCategories.MOD_LOADER));

        //     return urls;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: mkClassLoader()

    public static Set<Class<?>> getAddons() {
        File modDir = PotoFlux.getProgramDir().resolve("mods").toFile();
        File[] jarFiles = modDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) {
            return new HashSet<>();
        }

        URL[] urls = Arrays.stream(jarFiles)
                .map(f -> {
                    try {
                        return f.toURI().toURL();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);

        URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());

        Set<Class<?>> addons = new HashSet<>();

        for (File jarFile : jarFiles) {
            try (JarFile jar = new JarFile(jarFile)) {

                PtfLogger.info("Found jar " + jar.getName(), LogCategories.MOD_LOADER);

                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();

                    if (name.endsWith(".class")) {
                        // convert path to class name
                        String className = name.replace("/", ".").replace(".class", "");

                        PtfLogger.info("Found class " + className, LogCategories.MOD_LOADER, "mightSpam");

                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            if (clazz.isAnnotationPresent(Mod.class)) {
                                PtfLogger.info("Found @Mod class: " + clazz.getName(), LogCategories.MOD_LOADER);
                                addons.add(clazz);
                            }
                        } catch (ClassNotFoundException | NoClassDefFoundError e) {
                            e.printStackTrace();
                            PtfLogger.error("Failed loading a class !", LogCategories.MOD_LOADER);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                PtfLogger.error("Failed to convert to jar !", LogCategories.MOD_LOADER);
            }
        }
        return addons;
    }

    public static boolean isModLoaded(Mod mod) {
        if (illegalModIds.contains(mod.modId())) return true;
        return loadedMods.containsKey(mod.modId());
    }
    public static boolean isModLoaded(String modId) {
        if (modId.equals(PotoFlux.ID)) return true;
        return loadedMods.containsKey(modId);
    }
    public static boolean addMod(Mod mod, Class<?> modClass) {
        if (isModLoaded(mod)) return false;
        loadedMods.put(mod.modId(), modClass);
        return true;
    }
}
