package net.minheur.potoflux.loader;

import net.minheur.potoflux.loader.mod.ModEventBus;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotoFluxLoadingContext {
    private static final PotoFluxLoadingContext INSTANCE = new PotoFluxLoadingContext();
    private final ModEventBus modEventBus = new ModEventBus();

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
}
