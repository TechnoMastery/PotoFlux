package net.minheur.potoflux.login.response

import com.google.gson.annotations.SerializedName

class IsAccountCreationEnabledResponse {
    @JvmField
    @SerializedName("is_enabled")
    var isEnabled: Boolean = false
}
