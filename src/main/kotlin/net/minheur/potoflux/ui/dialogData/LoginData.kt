package net.minheur.potoflux.ui.dialogData

class LoginData(private val _username: String, private val _password: String) {
    // Provide Java-style record accessors used in existing Java code
    fun username(): String = _username
    fun password(): String = _password
}
