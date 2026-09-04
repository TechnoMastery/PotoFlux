package net.minheur.potoflux.boot;

import com.google.gson.Gson;
import net.minecraft.launchwrapper.Launch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public final class PotoFluxBoot {
    private static final Gson GSON = new Gson();

    private PotoFluxBoot() {
    }

    public static void main(String[] args) {
        boolean devEnv = args.length > 0 && "devEnv".equals(args[0]);
        System.setProperty(BootConstants.DEV_ENV_PROPERTY, Boolean.toString(devEnv));
        System.setProperty(BootConstants.ARGS_PROPERTY, encode(GSON.toJson(args)));

        BootModScanner.ScanResult scanResult = new BootModScanner(devEnv).scan();
        List<BootModMetadata> mods = new BootModSelector(getVersion(), scanResult.availableClasses()).select(scanResult.mods());
        System.setProperty(BootConstants.MODS_PROPERTY, encode(GSON.toJson(mods)));

        List<String> launchArgs = new ArrayList<>();
        launchArgs.add("--tweakClass");
        launchArgs.add(BootConstants.TWEAKER);
        Launch.main(launchArgs.toArray(new String[0]));
    }

    static String getVersion() {
        try {
            Properties props = new Properties();
            props.load(PotoFluxBoot.class.getResourceAsStream("/version.properties"));
            return props.getProperty("version", "UNKNOWN");
        } catch (IOException | NullPointerException e) {
            return "UNKNOWN";
        }
    }

    static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
