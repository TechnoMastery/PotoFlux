package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

public class InfoResponse {

    public boolean success;
    public String error;
    public String uuid;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("mail")
    public String email;
    public String[] perms;

}
