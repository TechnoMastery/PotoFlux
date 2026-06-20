package net.minheur.potoflux.settings.types;

/**
 * Enum with all types of setting allowed by the preference system
 */
public enum PreferencesTypes {
    /**
     * The data stored is a {@link String}.
     */
    STRING(String.class),
    /**
     * The data stored is a {@link Boolean}.
     */
    BOOLEAN(Boolean.class),
    /**
     * The data stored is an {@link Integer}.
     */
    INT(Integer.class),
    /**
     * The data stored is a {@link Long}.
     */
    LONG(Long.class),
    /**
     * The data stored is a {@link Float}.
     */
    FLOAT(Float.class),
    /**
     * The data stored is a {@link Double}.
     */
    DOUBLE(Double.class),
    /**
     * The data stored is a {@code byte[]}.
     */
    BYTE_ARRAY(byte[].class);

    /**
     * Class used to check if an {@linkplain Object} is the correct type
     */
    private final Class<?> valueClass;

    /**
     * Creates a type with its class
     *
     * @param valueClass used to check a value type
     */
    PreferencesTypes(Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    /**
     * Getter for the {@linkplain #valueClass}, allowing to check
     *
     * @return {@link #valueClass}
     */
    public Class<?> getValueClass() {
        return valueClass;
    }

}
