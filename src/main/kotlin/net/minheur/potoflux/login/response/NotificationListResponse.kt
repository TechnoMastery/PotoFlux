package net.minheur.potoflux.login.response

import com.google.gson.JsonObject

class NotificationListResponse : BaseResponse() {
    @JvmField
    var notifications: Array<JsonObject>? = null
}
