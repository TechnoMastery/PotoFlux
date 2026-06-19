package net.minheur.potoflux.login.response

import com.google.gson.annotations.SerializedName

class MdUserInfosResponse : BaseResponse() {
    @JvmField
    @SerializedName("email_changed")
    var emailChanged: Boolean = false

    @JvmField
    @SerializedName("f_name_changed")
    var firstNameChanged: Boolean = false

    @JvmField
    @SerializedName("l_name_changed")
    var lastNameChanged: Boolean = false

    @JvmField
    @SerializedName("rank_changed")
    var rankChanged: Boolean = false
}
