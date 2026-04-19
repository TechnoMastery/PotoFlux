package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

public class MdUserInfosResponse extends BaseResponse {

    @SerializedName("email_changed")
    public boolean emailChanged;
    @SerializedName("f_name_changed")
    public boolean firstNameChanged;
    @SerializedName("l_name_changed")
    public boolean lastNameChanged;
    @SerializedName("rank_changed")
    public boolean rankChanged;

}
