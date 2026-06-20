package net.minheur.potoflux.utils;

/**
 * LockableField holds a value that can be locked to prevent modification
 */
public class LockableField<T> {
    private T data;
    private final boolean locked;

    public LockableField(T data, boolean locked) {
        this.data = data;
        this.locked = locked;
    }

    public T get() {
        return data;
    }

    public boolean set(T value) {
        if (locked) return false;
        data = value;
        return true;
    }

    public boolean getIsLocked() {
        return locked;
    }
}
