package net.minheur.potoflux.login.response

import com.google.gson.annotations.SerializedName

class RmUserResponse : BaseResponse() {
    @JvmField
    @SerializedName("target_rank")
    var targetRank: Int = 0
}
