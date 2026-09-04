package net.minheur.potoflux.boot;

final class BootConstants {
    static final String MODS_PROPERTY = "potoflux.boot.mods";
    static final String ARGS_PROPERTY = "potoflux.boot.args";
    static final String DEV_ENV_PROPERTY = "potoflux.boot.devEnv";
    static final String MOD_ANNOTATION_DESCRIPTOR = "Lnet/minheur/potoflux/loader/mod/Mod;";
    static final String RUNTIME_LAUNCHER = "net.minheur.potoflux.PotoFluxRuntimeLauncher";
    static final String TWEAKER = "org.spongepowered.asm.launch.PotoFluxMixinTweaker";

    private BootConstants() {
    }
}
