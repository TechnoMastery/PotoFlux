package net.minheur.potoflux.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

public class LogAmountManager {
    private static final Preferences data = Preferences.userNodeForPackage(LogAmountManager.class);

    private static final String KEY_LOG_AMOUNT = "log_amount";
    private static final AtomicBoolean started = new AtomicBoolean(false);

    public static synchronized void init() {
        if (!started.compareAndSet(false, true)) return;

        int logAmount = getLogAmount() +1;
        data.putInt(KEY_LOG_AMOUNT, logAmount);
    }

    public static int getLogAmount() {
        return data.getInt(KEY_LOG_AMOUNT, 0);
    }
}
