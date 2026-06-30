package net.minheur.potoflux.ui.dialogData;

import net.minheur.potoflux.login.perms.Perms;

import java.util.List;

/**
 * New account data class.
 */
public class NewAccountData {
    /**
     * The email field.
     */
    public String email = "";
    /**
     * The first name field.
     */
    public String firstName = "";
    /**
     * The last name field.
     */
    public String lastName = "";
    /**
     * The password field.
     */
    public String password = "";
    /**
     * The rank field.
     */
    public Integer rank;
    /**
     * The perms field.
     */
    public List<Perms> perms;
}
