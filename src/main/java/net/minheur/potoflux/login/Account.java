package net.minheur.potoflux.login;

import net.minheur.potoflux.login.perms.Perms;

public class Account {

    public String uuid;
    public String firstName;
    public String lastName;
    public String email;
    public Perms[] perms;
    public int rank;
    public boolean locked;

}
