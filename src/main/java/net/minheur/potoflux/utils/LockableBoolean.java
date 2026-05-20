package net.minheur.potoflux.utils;

public class LockableBoolean {
    private boolean data;
    private final boolean locked;

    public LockableBoolean(boolean locked, boolean data) {
        this.locked = locked;
        this.data = data;
    }
}
