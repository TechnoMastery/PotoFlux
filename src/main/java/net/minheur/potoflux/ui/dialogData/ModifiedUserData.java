package net.minheur.potoflux.ui.dialogData;

import net.minheur.potoflux.login.perms.Perms;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModifiedUserData {

    public @Nullable String email;
    public @Nullable String firstName;
    public @Nullable String lastName;
    public @Nullable Integer rank;
    public @Nullable List<Perms> perms;

}
