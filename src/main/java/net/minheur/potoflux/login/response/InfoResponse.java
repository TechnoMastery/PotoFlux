package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

/**
 * Info response class.<br>
 * It gives info on an account
 */
public class InfoResponse extends BaseResponse {
    /**
     * Accounts UUID
     */
    public String uuid;

    /**
     * Account's first name.
     */
    @SerializedName("first_name")
    public String firstName;

    /**
     * Account's last name.
     */
    @SerializedName("last_name")
    public String lastName;

    /**
     * Account's email.
     */
    @SerializedName("mail")
    public String email;

    /**
     * Account's perms.
     */
    public String[] perms;
    /**
     * Account's rank.
     */
    public int rank;
    /**
     * Weather account is locked.
     */
    public boolean locked;
}
