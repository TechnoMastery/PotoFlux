package net.minheur.potoflux.loader.mod;

public class ModContainer {
    public Mod mod;
    public Class<?> clazz;
    public ModState state = ModState.NOT_PROCESSED;

    public ModContainer(Mod mod, Class<?> clazz) {
        this.mod = mod;
        this.clazz = clazz;
    }
}
