package net.minheur.potoflux.loader;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import org.reflections.vfs.Vfs;

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

    private static URLClassLoader modsClassLoader = null;

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
        if (modId.equals(PotoFlux.ID)) return true;
        return loadedMods.containsKey(modId);
    }
    public static boolean addMod(Mod mod, Class<?> modClass) {
        if (isModLoaded(mod)) return false;
        loadedMods.put(mod.modId(), modClass);
        return true;
    }
}
