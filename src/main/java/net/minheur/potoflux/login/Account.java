package net.minheur.potoflux.login;

import net.minheur.potoflux.login.perms.Perms;

/**
 * A class that stores every useful info about an account.<br>
 * The user stored by the class can be identified with it's {@link #uuid}
 */
public class Account {

    /**
     * UUID of the user stored in this class
     */
    public String uuid;
    /**
     * First name of the user stored in this class
     */
    public String firstName;
    /**
     * Last name of the user stored in this class
     */
    public String lastName;
    /**
     * Email of the user stored in this class.<br>
     * This is also the user's id, used when logging in
     */
    public String email;
    /**
     * Actual perms of the user stored in this class
     */
    public Perms[] perms;
    /**
     * Rank of the user stored in this class
     */
    public int rank;
    /**
     * Weather the current user's account is locked
     */
    public boolean locked;

}
