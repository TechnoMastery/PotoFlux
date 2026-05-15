package net.minheur.potoflux.ui.dialogData;

import net.minheur.potoflux.login.perms.Perms;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NewAccountData {
    public @NotNull String email;
    public @NotNull String firstName;
    public @NotNull String lastName;

    public @Nullable String password;
    public @Nullable Integer rank;
    public @Nullable List<Perms> perms;
}
