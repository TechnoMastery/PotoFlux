package net.minheur.potoflux.ui.dialogData;

import net.minheur.potoflux.login.perms.Perms;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Data for a new account
 */
public class NewAccountData {
    /**
     * Email of the new account
     */
    public @NotNull String email;
    /**
     * First name of the user
     */
    public @NotNull String firstName;
    /**
     * Last name of the user
     */
    public @NotNull String lastName;
    /**
     * Password for the new account
     */
    public @NotNull String password;

    /**
     * Rank for the new account
     */
    public @Nullable Integer rank;
    /**
     * Perms granted to the new account
     */
    public @Nullable List<Perms> perms;
}
