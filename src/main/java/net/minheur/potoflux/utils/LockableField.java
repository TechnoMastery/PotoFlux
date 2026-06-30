package net.minheur.potoflux.utils;

/**
 * LockableField holds a value that can be locked to prevent modification
 */
public class LockableField<T> {
    /**
     * The data field.
     */
    private T data;
    /**
     * Weather {@linkplain #data} is locked
     */
    private final boolean locked;

    /**
     * Constructs a new LockableField.
     */
    public LockableField(T data, boolean locked) {
        this.data = data;
        this.locked = locked;
    }

    /**
     * Gets {@linkplain #data}
     * @return {@link #data}
     */
    public T get() {
        return data;
    }

    /**
     * Sets {@linkplain #data}, if not {@linkplain #locked}
     * @param value new value to use
     * @return weather value was set ({@code false} if {@linkplain #locked} is {@code true})
     */
    public boolean set(T value) {
        if (locked) return false;
        data = value;
        return true;
    }

    /**
     * Gets if {@linkplain #data} is locked.
     * @return {@link #locked}
     */
    public boolean getIsLocked() {
        return locked;
    }
}
