package net.minheur.potoflux.settings.types;

public enum PreferencesTypes {
    STRING(String.class),
    BOOLEAN(Boolean.class),
    INT(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BYTE_ARRAY(byte[].class);

    private final Class<?> valueClass;

    PreferencesTypes(Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

}
