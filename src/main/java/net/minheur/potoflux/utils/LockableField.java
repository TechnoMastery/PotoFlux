package net.minheur.potoflux.utils;

public class LockableField<T> {
    private T data;
    private final boolean locked;

    public LockableField(T data, boolean locked) {
        this.locked = locked;
        this.data = data;
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
