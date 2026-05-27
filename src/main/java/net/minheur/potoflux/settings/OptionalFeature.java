package net.minheur.potoflux.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionalFeature {
    private final Type type;
    private @Nullable String stringValue;
    private @Nullable Integer intValue;
    private @Nullable Boolean boolValue;

    public OptionalFeature(@NotNull String stringValue) {
        this.stringValue = stringValue;
        this.type = Type.STRING;
    }
    public OptionalFeature(@NotNull Integer intValue) {
        this.intValue = intValue;
        this.type = Type.INT;
    }
    public OptionalFeature(@NotNull Boolean boolValue) {
        this.boolValue = boolValue;
        this.type = Type.BOOL;
    }

    public Object get() {
        return switch (type) {
            case STRING -> stringValue;
            case INT -> intValue;
            case BOOL -> boolValue;
        };
    }

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

    public Type getType() {
        return type;
    }

    public enum Type {
        STRING,
        INT,
        BOOL
    }
}
