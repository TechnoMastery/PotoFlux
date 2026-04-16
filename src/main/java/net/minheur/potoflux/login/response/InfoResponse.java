package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

public class InfoResponse extends BaseResponse {

    public String uuid;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("mail")
    public String email;
    public String[] perms;
    public int rank;
    public boolean locked;

}
