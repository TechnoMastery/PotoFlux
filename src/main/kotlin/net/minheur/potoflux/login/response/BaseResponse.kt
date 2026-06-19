package net.minheur.potoflux.login.response

open class BaseResponse {
    @JvmField
    var success: Boolean = false

    @JvmField
    var error: String? = null
}
