package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Registers all perms of the app
 */
public enum Perms {
    /**
     * Perm required to view all users, as well as their account information.
     */
    VIEW_USERS("viewUsers", "potoflux:perms.viewUsers", PermRuns::seeUsersInfos),
    /**
     * Perm required to create new users
     */
    CREATE_USERS("mkUsers", "potoflux:perms.mkUsers", PermRuns::addUser),
    /**
     * Perm required to delete accounts
     */
    DELETE_USERS("rmUsers", "potoflux:perms.rmUsers", PermRuns::rmUser),
    /**
     * Perm required to information of accounts.<br>
     * Require {@link #VIEW_USERS} perm as well.
     */
    CHANGE_INFORMATIONS("mdInfos", "potoflux:perms.mdUserInfos", "potoflux:perms.executesInDetailsMenu"),
    /**
     * Perm required to reset users passwords, in case they forgot it.<br>
     * Require {@link #VIEW_USERS} perm as well.
     */
    CHANGE_PASSWORD("mdPasswords", "potoflux:perms.mdUserPasswords", "potoflux:perms.executesInDetailsMenu"),
    /**
     * Perm required to lock accounts, disabling people to modify / delete it
     */
    LOCK("lockAccounts", "potoflux:perms.lock", "potoflux:perms.executesInDetailsMenu"),
    /**
     * Perm required to set if users are allowed to self-create an account
     */
    LOCK_ACCOUNT_CREATION("lockMkAccounts", "potoflux:perms.lockAccountCreation", PermRuns::setAccountCreationState);

    /**
     * Code used in the database to store the perm.
     */
    private final @NotNull String sqlCode;
    /**
     * Supplier of a {@link Translations#get(String)} method, to store its display value without generating it.
     */
    private final @NotNull Supplier<String> name;
    /**
     * This is executed when the perm is run by the user
     */
    private final @Nullable Runnable permAction;
    /**
     * Supplier of a {@link Translations#get(String)} displayed if the user try to execute a perm that have no {@link #permAction} setup.
     */
    private final @Nullable Supplier<String> noRunFallback;

    /**
     * Constructor to build a simple perm, with no {@link #permAction} or {@link #noRunFallback}.
     * @param sqlCode used in the database
     * @param nameTranslationKey content to use when getting the translation for the perm's name.
     */
    Perms(@NotNull String sqlCode, String nameTranslationKey) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);

        this.permAction = null;
        this.noRunFallback = null;
    }

    /**
     * Constructor used to build a perm with an {@link #permAction}.
     * @param sqlCode used in the database
     * @param nameTranslationKey content to use when getting the translation for the perm's name.
     * @param permAction {@link Runnable} executed when the perm is run.
     */
    Perms(@NotNull String sqlCode, String nameTranslationKey, @NotNull Runnable permAction) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);
        this.permAction = permAction;

        this.noRunFallback = null;
    }

    /**
     * Constructor used to build a perm with a {@link #noRunFallback}
     * @param sqlCode used in the database
     * @param nameTranslationKey content to use when getting the translation for the perm's name.
     * @param noRunFallback content to use when getting the translation for the perm's fallback, displayed when the perm is run.
     */
    Perms(@NotNull String sqlCode, String nameTranslationKey, @NotNull String noRunFallback) {
        this.sqlCode = sqlCode;
        this.name = () -> Translations.get(nameTranslationKey);
        this.noRunFallback = () -> Translations.get(noRunFallback);

        this.permAction = null;
    }

    /**
     * Getter for the perm's SQL code
     * @return the perm's SQL code
     */
    public @NotNull String getCode() {
        return sqlCode;
    }

    /**
     * Getter for the perm's name. It is built, getting the actual translated name.
     * @return the perm's name
     */
    public @NotNull String getName() {
        return name.get();
    }

    /**
     * Getter for the perm's action.
     * @return the perm's action
     */
    public @Nullable Runnable getPermAction() {
        return permAction;
    }
    /**
     * Getter for the perm's fallback in case of no perm action.<br>
     * If not null, will be getting the actual translated sentence.
     * @return the perm's fallback, or null if its supplier is
     */
    public @Nullable String getNoRunFallback() {
        if (noRunFallback == null) return null;
        return noRunFallback.get();
    }

    /**
     * Gets the {@link Perms} from an sqlCode.<br>
     * Used when listing perms sent by the database
     * @param sqlCode the code to look the perm for
     * @return the {@link Perms} corresponding to the code, {@code null} if not found
     */
    public static @Nullable Perms getFromCode(String sqlCode) {
        for (Perms p : Perms.values()) {
            if (p.sqlCode.equals(sqlCode))
                return p;
        }

        return null;
    }

    /**
     * Make sure we give the name.<br>
     * Used in combo boxes, to have an actual translated name
     * @return {@link #getName()}
     */
    @Override
    public @NotNull String toString() {
        return getName();
    }
}
