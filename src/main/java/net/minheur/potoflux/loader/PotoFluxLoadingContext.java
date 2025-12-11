package net.minheur.potoflux.loader;

import net.minheur.potoflux.loader.mod.ModEventBus;

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

    public static String getScanPath() {
        if (isDevEnv()) {
            String projectDir = System.getProperty("user.dir");
            return projectDir + "build/classes/java/main";
        } else return "./mods/";
    }
}
