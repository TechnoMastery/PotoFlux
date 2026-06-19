package net.minheur.potoflux.ui.dialogData

import net.minheur.potoflux.login.perms.Perms

class ModifiedUserData {
    @JvmField
    var email: String? = null

    @JvmField
    var firstName: String? = null

    @JvmField
    var lastName: String? = null

    @JvmField
    var rank: Int? = null

    // MutableList for better Java compatibility
    @JvmField
    var perms: MutableList<Perms>? = null
}
