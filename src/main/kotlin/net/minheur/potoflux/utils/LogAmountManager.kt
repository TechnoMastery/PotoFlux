package net.minheur.potoflux.utils

import java.util.concurrent.atomic.AtomicBoolean
import java.util.prefs.Preferences

/**
 * Manages the log amount, storing and getting the value
 */
object LogAmountManager {
    private val data: Preferences = Preferences.userNodeForPackage(LogAmountManager::class.java)
    private const val KEY_LOG_AMOUNT = "log_amount"
    private val started = AtomicBoolean(false)

    @Synchronized
    @JvmStatic
    fun init() {
        if (!started.compareAndSet(false, true)) return
        val logAmount = getLogAmount() + 1
        data.putInt(KEY_LOG_AMOUNT, logAmount)
    }

    @JvmStatic
    fun getLogAmount(): Int = data.getInt(KEY_LOG_AMOUNT, 0)
}
