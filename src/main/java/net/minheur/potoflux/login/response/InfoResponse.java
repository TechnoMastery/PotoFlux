package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response when you ask for a specific user's info.<br>
 * You can then use it to fill an {@link net.minheur.potoflux.login.Account} class
 */
public class InfoResponse extends BaseResponse {

    /**
     * Stores the target user's uuid
     */
    public String uuid;
    /**
     * Stores the first name of the targeted user
     */
    @SerializedName("first_name")
    public String firstName;
    /**
     * Stores the last name of the targeted user
     */
    @SerializedName("last_name")
    public String lastName;
    /**
     * Store the email adress of the user
     */
    @SerializedName("mail")
    public String email;
    /**
     * Array containing all codes of {@link net.minheur.potoflux.login.perms.Perms} detained by the user.
     */
    public String[] perms;
    /**
     * Rank of the user.<br>
     * 0 is full powers, 100 is last
     */
    public int rank;
    /**
     * Tells if the user is locked. If so, it cannot be modified without having the {@link net.minheur.potoflux.login.perms.Perms#LOCK} perm.
     */
    public boolean locked;

}
