package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.translations.Translations;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum Perms {
    VIEW_USERS("viewUsers", "See user's infos", PermRuns::seeUsersInfos),
    CREATE_USERS("mkUsers", "Create new users", PermRuns::addUser),
    DELETE_USERS("rmUsers", "Delete users", PermRuns::rmUser),
    CHANGE_INFORMATIONS("mdInfos", "Change user's infos"),
    CHANGE_PASSWORD("mdPasswords", "Change user's passwords"),
    LOCK("lockAccounts", "Lock or unlock accounts");

    private final String sqlCode;
    private final Supplier<String> name;
    @CheckForNull
    private final Runnable permAction;
    @CheckForNull
    private final String noRunFallback;

    Perms(String sqlCode, String nameTranslationKey) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);

        this.permAction = null;
        this.noRunFallback = null;
    }

    Perms(String sqlCode, String nameTranslationKey, @Nonnull Runnable permAction) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);
        this.permAction = permAction;

        this.noRunFallback = null;
    }

    Perms(String sqlCode, String nameTranslationKey, @Nonnull String noRunFallback) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);
        this.noRunFallback = noRunFallback;

        this.permAction = null;
    }

    @Nonnull
    public String getCode() {
        return sqlCode;
    }

    @Nonnull
    public String getName() {
        return name.get();
    }

    @CheckForNull
    public Runnable getPermAction() {
        return permAction;
    }
    @CheckForNull
    public String getNoRunFallback() {
        return noRunFallback;
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
