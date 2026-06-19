package net.minheur.potoflux.ui.dialogData

import net.minheur.potoflux.login.perms.Perms

class NewAccountData {
    @JvmField
    var email: String = ""

    @JvmField
    var firstName: String = ""

    @JvmField
    var lastName: String = ""

    @JvmField
    var password: String = ""

    @JvmField
    var rank: Int? = null

    // Use MutableList so Java sees java.util.List<Perms> (not wildcard)
    @JvmField
    var perms: MutableList<Perms>? = null
}
