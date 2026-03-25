package net.minheur.potoflux.login;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum Perms {
    VIEW_USERS("viewUsers", "See user's infos"),
    CREATE_USERS("mkUsers", "Create new users"),
    DELETE_USERS("rmUsers", "Delete users"),
    CHANGE_INFORMATIONS("mdInfos", "Change user's infos"),
    CHANGE_PASSWORD("mdPassword", "Change user's passwords");

    private final String sqlCode;
    private final String name;

    Perms(String sqlCode, String name) {
        this.sqlCode = sqlCode;
        this.name = name;
    }

    @Nonnull
    public String getCode() {
        return sqlCode;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public static Perms getFromCode(String sqlCode) {
        for (Perms p : Perms.values()) {
            if (p.sqlCode.equals(sqlCode))
                return p;
        }

        return null;
    }

    @Override
    @Nonnull
    public String toString() {
        return getName();
    }
}
