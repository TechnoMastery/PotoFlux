package net.minheur.potoflux.utils;

/**
 * This defines a random object, but it can be locked (so unchangeable)
 * @param <T> the actual Object stored by the field
 */
public class LockableField<T> {
    /**
     * The actual {@link T} value stored in the field
     */
    private T data;
    /**
     * Weather the field is locked. If so, it cannot be changed via {@link #set}
     */
    private final boolean locked;

    /**
     * Creates a  field, with the initial value and if it is locked
     * @param data initial value, of type {@linkplain T}
     * @param locked weather the value is locked
     */
    public LockableField(T data, boolean locked) {
        this.locked = locked;
        this.data = data;
    }

    /**
     * Gets the stored value
     * @return {@link #data}
     */
    public T get() {
        return data;
    }

    /**
     * Sets the value, if it is not {@linkplain #locked}
     * @param value new value of the field
     * @return weather it been set. {@code false} if {@linkplain #locked}
     */
    public boolean set(T value) {
        if (locked) return false;
        data = value;
        return true;
    }

    /**
     * Checks if the field is locked
     * @return {@link #locked}
     */
    public boolean getIsLocked() {
        return locked;
    }
}
