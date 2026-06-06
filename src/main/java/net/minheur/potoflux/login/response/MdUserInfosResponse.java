package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

public class MdUserInfosResponse extends BaseResponse {

    /**
     * Says if the user's email has been changed.<br>
     * Used mainly in debugging / checking for illegal requests
     */
    @SerializedName("email_changed")
    public boolean emailChanged;
    /**
     * Says if the user's first name has been changed.<br>
     * Used mainly in debugging / checking for illegal requests
     */
    @SerializedName("f_name_changed")
    public boolean firstNameChanged;
    /**
     * Says if the user's last name has been changed.<br>
     * Used mainly in debugging / checking for illegal requests
     */
    @SerializedName("l_name_changed")
    public boolean lastNameChanged;
    /**
     * Says if the user's rank has been changed.<br>
     * Used mainly in debugging / checking for illegal requests
     */
    @SerializedName("rank_changed")
    public boolean rankChanged;

}
