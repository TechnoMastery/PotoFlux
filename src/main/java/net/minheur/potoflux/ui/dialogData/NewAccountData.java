package net.minheur.potoflux.ui.dialogData;

import net.minheur.potoflux.login.perms.Perms;

import java.util.List;

public class NewAccountData {
    public String email = "";
    public String firstName = "";
    public String lastName = "";
    public String password = "";
    public Integer rank;
    public List<Perms> perms;
}
