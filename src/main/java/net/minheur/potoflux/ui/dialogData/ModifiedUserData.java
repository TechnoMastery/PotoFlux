package net.minheur.potoflux.ui.dialogData;

import net.minheur.potoflux.login.perms.Perms;

import java.util.List;

/**
 * Modified user data class.
 */
public class ModifiedUserData {
    /**
     * The new email field.
     */
    public String email;
    /**
     * The new first name field.
     */
    public String firstName;
    /**
     * The new last name field.
     */
    public String lastName;
    /**
     * The new rank field.
     */
    public Integer rank;
    /**
     * The new perms field.
     */
    public List<Perms> perms;
}
