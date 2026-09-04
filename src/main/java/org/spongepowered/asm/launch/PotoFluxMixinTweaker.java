package org.spongepowered.asm.launch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minheur.potoflux.boot.BootModMetadata;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class PotoFluxMixinTweaker implements ITweaker {
    private static final String MODS_PROPERTY = "potoflux.boot.mods";
    private static final String ARGS_PROPERTY = "potoflux.boot.args";
    private static final String RUNTIME_LAUNCHER = "net.minheur.potoflux.PotoFluxRuntimeLauncher";
    private static final Gson GSON = new Gson();
    private static final Type MOD_LIST_TYPE = new TypeToken<List<BootModMetadata>>() {
    }.getType();

    public PotoFluxMixinTweaker() {
        MixinBootstrap.start();
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        MixinBootstrap.doInit(CommandLineOptions.ofArgs(args));
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.addClassLoaderExclusion("net.minheur.potoflux.boot.");
        classLoader.addTransformerExclusion("net.minheur.potoflux.boot.");

        for (BootModMetadata mod : readMods()) {
            URL sourceUrl = getJarUrl(mod);
            if (sourceUrl != null) classLoader.addURL(sourceUrl);
        }

        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        for (BootModMetadata mod : readMods()) {
            if (!mod.loadable) continue;
            for (String mixinConfig : mod.mixinConfigs) {
                if (mixinConfig == null || mixinConfig.isBlank()) continue;
                Mixins.addConfiguration(mixinConfig);
            }
        }

        MixinBootstrap.inject();
    }

    @Override
    public String getLaunchTarget() {
        return RUNTIME_LAUNCHER;
    }

    @Override
    public String[] getLaunchArguments() {
        return readArgs().toArray(new String[0]);
    }

    private static URL getJarUrl(BootModMetadata mod) {
        if (mod.sourcePath == null || !mod.sourcePath.endsWith(".jar")) return null;
        try {
            return new File(mod.sourcePath).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid mod jar URL: " + mod.sourcePath, e);
        }
    }

    private static List<String> readArgs() {
        String raw = System.getProperty(ARGS_PROPERTY);
        if (raw == null || raw.isBlank()) return List.of();
        String[] args = GSON.fromJson(decode(raw), String[].class);
        return args == null ? List.of() : List.of(args);
    }

    private static List<BootModMetadata> readMods() {
        String raw = System.getProperty(MODS_PROPERTY);
        if (raw == null || raw.isBlank()) return List.of();
        List<BootModMetadata> mods = GSON.fromJson(decode(raw), MOD_LIST_TYPE);
        return mods == null ? List.of() : new ArrayList<>(mods);
    }

    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
