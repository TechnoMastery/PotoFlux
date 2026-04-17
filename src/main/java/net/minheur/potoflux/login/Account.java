package net.minheur.potoflux.login;

import net.minheur.potoflux.login.perms.Perms;

/**
 * A class that stores every useful info about an account
 */
public class Account {

    public String uuid;
    public String firstName;
    public String lastName;
    public String email;
    public Perms[] perms;
    public int rank;
    public boolean locked;

}
