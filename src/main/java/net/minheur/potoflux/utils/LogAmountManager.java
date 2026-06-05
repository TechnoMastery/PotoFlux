package net.minheur.potoflux.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

/**
 * Manages the log amount, storing and getting the value
 */
public class LogAmountManager {
    /**
     * Link to the prefs containing the log amount
     */
    private static final Preferences data = Preferences.userNodeForPackage(LogAmountManager.class);

    /**
     * Key to get the log amount
     */
    private static final String KEY_LOG_AMOUNT = "log_amount";
    /**
     * Boolean checking if the app has started and if the amount already got increased
     */
    private static final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * Gets the {@linkplain #data}, increases the log amount then stores it back
     */
    public static synchronized void init() {
        if (!started.compareAndSet(false, true)) return;

        int logAmount = getLogAmount() +1;
        data.putInt(KEY_LOG_AMOUNT, logAmount);
    }

    /**
     * Gets the actual log amount
     * @return actual log amount
     */
    public static int getLogAmount() {
        return data.getInt(KEY_LOG_AMOUNT, 0);
    }
}
