package net.minheur.potoflux.login.response

import com.google.gson.annotations.SerializedName

class InfoResponse : BaseResponse() {
    @JvmField
    var uuid: String? = null

    @JvmField
    @SerializedName("first_name")
    var firstName: String? = null

    @JvmField
    @SerializedName("last_name")
    var lastName: String? = null

    @JvmField
    @SerializedName("mail")
    var email: String? = null

    @JvmField
    var perms: Array<String>? = null

    @JvmField
    var rank: Int = 0

    @JvmField
    var locked: Boolean = false
}
