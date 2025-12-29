package net.minheur.potoflux.theme;

import net.minheur.potoflux.utils.logger.LogCategories;
import net.minheur.potoflux.utils.logger.PtfLogger;

public class ThemeHandler {
    private static Themes theme;
    private static final Themes defaultTheme = Themes.DARK;

    private static boolean hasPassedInit = false;

    public static void initTheme() {
        if (hasPassedInit) {
            PtfLogger.error("Can't init theme 2 times !", LogCategories.THEME);
            return;
        }

        theme = defaultTheme;
        hasPassedInit = true;
    }

    public static Themes getTheme() {
        return theme;
    }
}
