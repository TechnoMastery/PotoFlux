package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Registers all perms of the app
 */
public enum Perms {
    VIEW_USERS("viewUsers", "potoflux:perms.viewUsers", PermRuns::seeUsersInfos),
    CREATE_USERS("mkUsers", "potoflux:perms.mkUsers", PermRuns::addUser),
    DELETE_USERS("rmUsers", "potoflux:perms.rmUsers", PermRuns::rmUser),
    CHANGE_INFORMATIONS("mdInfos", "potoflux:perms.mdUserInfos", "potoflux:perms.executesInDetailsMenu"),
    CHANGE_PASSWORD("mdPasswords", "potoflux:perms.mdUserPasswords", "potoflux:perms.executesInDetailsMenu"),
    LOCK("lockAccounts", "potoflux:perms.lock", "potoflux:perms.executesInDetailsMenu"),
    LOCK_ACCOUNT_CREATION("lockMkAccounts", "potoflux:perms.lockAccountCreation", PermRuns::setAccountCreationState);

    private final @NotNull String sqlCode;
    private final @NotNull Supplier<String> name;
    private final @Nullable Runnable permAction;
    private final @Nullable Supplier<String> noRunFallback;

    Perms(@NotNull String sqlCode, String nameTranslationKey) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);

        this.permAction = null;
        this.noRunFallback = null;
    }

    Perms(@NotNull String sqlCode, String nameTranslationKey, @NotNull Runnable permAction) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);
        this.permAction = permAction;

        this.noRunFallback = null;
    }

    Perms(@NotNull String sqlCode, String nameTranslationKey, @NotNull String noRunFallback) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);
        this.noRunFallback = () -> Translations.get(noRunFallback);

        this.permAction = null;
    }

    public @NotNull String getCode() {
        return sqlCode;
    }

    public @NotNull String getName() {
        return name.get();
    }

    public @Nullable Runnable getPermAction() {
        return permAction;
    }
    public @Nullable String getNoRunFallback() {
        if (noRunFallback == null) return null;
        return noRunFallback.get();
    }

    public static @Nullable Perms getFromCode(String sqlCode) {
        for (Perms p : Perms.values()) {
            if (p.sqlCode.equals(sqlCode))
                return p;
        }

        return null;
    }

    @Override
    public @NotNull String toString() {
        return getName();
    }
}
