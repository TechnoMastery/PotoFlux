package net.minheur.potoflux.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Container for a feature's value
 */
public class OptionalFeature {
    /**
     * Type of the feature.<br>
     * The feature needs to be one of its 3 values.<br>
     * It will tell what value to use between {@linkplain #stringValue}, {@linkplain #intValue} of {@linkplain #boolValue}
     */
    private final Type type;
    /**
     * Value field used if {@linkplain #type} is {@link Type#STRING}
     */
    private @Nullable String stringValue;
    /**
     * Value field used if {@linkplain #type} is {@link Type#INT}
     */
    private @Nullable Integer intValue;
    /**
     * Value field used if {@linkplain #type} is {@link Type#BOOL}
     */
    private @Nullable Boolean boolValue;

    /**
     * Creates a value of {@linkplain String}
     * @param stringValue value of the feature
     */
    public OptionalFeature(@NotNull String stringValue) {
        this.stringValue = stringValue;
        this.type = Type.STRING;
    }
    /**
     * Creates a value of {@linkplain Integer}
     * @param intValue value of the feature
     */
    public OptionalFeature(@NotNull Integer intValue) {
        this.intValue = intValue;
        this.type = Type.INT;
    }
    /**
     * Creates a value of {@linkplain Boolean}
     * @param boolValue value of the feature
     */
    public OptionalFeature(@NotNull Boolean boolValue) {
        this.boolValue = boolValue;
        this.type = Type.BOOL;
    }

    /**
     * Gets the value as an {@link Object}
     * @return the value depending on the {@linkplain #type}
     */
    public Object get() {
        return switch (type) {
            case STRING -> stringValue;
            case INT -> intValue;
            case BOOL -> boolValue;
        };
    }

    /**
     * Modifies the value, from an {@link Object}
     * @param value to set in the field corresponding to {@linkplain #type}
     * @throws ClassCastException if the value given cannot be put into the right field
     */
    public void set(Object value) {
        switch (type) {
            case INT -> {
                if (value instanceof Integer i) intValue = i;
            }
            case BOOL -> {
                if (value instanceof Boolean b) boolValue = b;
            }
            case STRING -> {
                if (value instanceof String s) stringValue = s;
            }
        }
    }

    /**
     * Getter for the {@linkplain #type}
     * @return {@link #type}
     */
    public Type getType() {
        return type;
    }

    /**
     * Enum to store the type of value
     */
    public enum Type {
        /**
         * If the feature value is a {@link String}
         */
        STRING,
        /**
         * If the feature value is an {@link Integer}
         */
        INT,
        /**
         * If the feature value is a {@link Boolean}
         */
        BOOL
    }
}
