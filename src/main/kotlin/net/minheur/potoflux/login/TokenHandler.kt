package net.minheur.potoflux.login

import net.minheur.potoflux.logger.LogCategories
import net.minheur.potoflux.logger.PtfLogger
import java.io.IOException
import java.util.prefs.Preferences

/**
 * Saves, stores and retrieves connection token
 */
object TokenHandler {
    private val prefs: Preferences = Preferences.userNodeForPackage(TokenHandler::class.java)
    private const val KEY = "token"

    @JvmStatic
    fun save(token: String?) {
        if (token.isNullOrEmpty()) return
        prefs.put(KEY, token)
        PtfLogger.info("Saved token !", LogCategories.TOKEN)
    }

    @JvmStatic
    fun get(): String? = prefs.get(KEY, null)

    @JvmStatic
    fun clear() {
        prefs.remove(KEY)
        PtfLogger.info("Cleared local token !", LogCategories.TOKEN)
    }

    @JvmStatic
    fun has(): Boolean {
        val token = get()
        return !token.isNullOrEmpty()
    }

    @JvmStatic
    fun rmOnlineToken() {
        try {
            RequestPoster.rmToken(get())
        } catch (e: IOException) {
            e.printStackTrace()
            PtfLogger.error("Could not remove token", LogCategories.CONNEXION_POST)
        }
    }
}
