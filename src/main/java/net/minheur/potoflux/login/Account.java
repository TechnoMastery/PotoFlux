package net.minheur.potoflux.login;

import net.minheur.potoflux.login.perms.Perms;

/**
 * Class storing an account
 */
public class Account {
    /**
     * The uuid of the account.
     */
    public String uuid;
    /**
     * The first name of the account.
     */
    public String firstName;
    /**
     * The last name of the account.
     */
    public String lastName;
    /**
     * The email of the account.
     */
    public String email;
    /**
     * The perms of the account.
     */
    public Perms[] perms;
    /**
     * The rank of the account.
     */
    public int rank;
    /**
     * The locked of the account.
     */
    public boolean locked;
}
