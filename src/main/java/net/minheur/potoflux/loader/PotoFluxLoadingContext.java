package net.minheur.potoflux.loader;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModEventBus;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    private PotoFluxLoadingContext() {}

    public static PotoFluxLoadingContext get() {
        return INSTANCE;
    }

    public ModEventBus getModEventBus() {
        return modEventBus;
    }

    public static boolean isDevEnv() {
        String cp = System.getProperty("java.class.path");

        return cp.contains("build\\classes\\java\\main")
                || cp.contains("build\\resources\\main");
    }

    public static Collection<URL> getScanUrls() {
        if (isDevEnv()) {
            String projectDir = System.getProperty("user.dir");
            Path classes = Paths.get(projectDir, "build", "classes", "java", "main");
            try {
                return List.of(classes.toUri().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            File modsDir = new File("mods");
            File[] jars = modsDir.listFiles((d, n) -> n.endsWith(".jar"));

            List<URL> urls = new ArrayList<>();
            if (jars != null) {
                for (File jar : jars) try {
                    urls.add(jar.toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            return urls;
        }
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
