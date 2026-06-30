package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

/**
 * Modify user infos response class.
 */
public class MdUserInfosResponse extends BaseResponse {
    /**
     * Weather the email changed.
     */
    @SerializedName("email_changed")
    public boolean emailChanged = false;

    /**
     * Weather first name changed.
     */
    @SerializedName("f_name_changed")
    public boolean firstNameChanged = false;

    /**
     * Weather the last name changed.
     */
    @SerializedName("l_name_changed")
    public boolean lastNameChanged = false;

    /**
     * Weather the rank changed.
     */
    @SerializedName("rank_changed")
    public boolean rankChanged = false;
}
