package net.minheur.potoflux.login

import net.minheur.potoflux.login.perms.Perms

class Account {
    @JvmField
    var uuid: String? = null

    @JvmField
    var firstName: String? = null

    @JvmField
    var lastName: String? = null

    @JvmField
    var email: String? = null

    @JvmField
    var perms: Array<Perms>? = null

    @JvmField
    var rank: Int = 0

    @JvmField
    var locked: Boolean = false
}
