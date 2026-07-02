package net.minheur.potoflux.styles;

import net.minheur.potoflux.loader.mod.events.RegisterStylesheetsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Potoflux stylesheets class.
 */
public class PtfStylesheets {
    /**
     * The stylesheet list
     */
    private static final RegistryList<StylesheetEntry> LIST = new RegistryList<>();

    /**
     * The account notification stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> ACCOUNT_NOTIFICATION = LIST.add(() -> new StylesheetEntry(fromModId("account_notification"),
            buildExternal("/styles/tabs/account/notifications.css")));
    /**
     * The mod list entry stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> MOD_LIST_ENTRY = LIST.add(() -> new StylesheetEntry(fromModId("mod_list_entry"),
            buildExternal("/styles/tabs/mods/listEntry.css")));
    /**
     * The main stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> MAIN = LIST.add(() -> new StylesheetEntry(fromModId("main"),
            buildExternal("/styles/tabs/main.css")));
    /**
     * The menu stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> MENU = LIST.add(() -> new StylesheetEntry(fromModId("menu"),
            buildExternal("/styles/tabs/menu.css")));
    /**
     * The home tab stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> HOME_TAB = LIST.add(() -> new StylesheetEntry(fromModId("home_tab"),
            buildExternal("/styles/tabs/home.css")));
    /**
     * The terminal tab stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> TERMINAL_TAB = LIST.add(() -> new StylesheetEntry(fromModId("terminal_tab"),
            buildExternal("/styles/tabs/terminal.css")));
    /**
     * The account tab stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> ACCOUNT_TAB = LIST.add(() -> new StylesheetEntry(fromModId("account_tab"),
            buildExternal("/styles/tabs/account.css")));
    /**
     * The mods tab stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> MODS_TAB = LIST.add(() -> new StylesheetEntry(fromModId("mods_tab"),
            buildExternal("/styles/tabs/mods.css")));
    /**
     * The settings tab stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> SETTINGS_TAB = LIST.add(() -> new StylesheetEntry(fromModId("settings_tab"),
            buildExternal("/styles/tabs/settings.css")));
    /**
     * The debug tab stylesheet field.
     */
    public static final SmartSupplier<StylesheetEntry> DEBUG_TAB = LIST.add(() -> new StylesheetEntry(fromModId("debug_tab"),
            buildExternal("/styles/tabs/debug.css")));

    /**
     * Builds the external link to the stylesheet
     * @param target the target
     * @return the built link as external
     */
    private static String buildExternal(String target) {
        return Objects.requireNonNull(
                PtfStylesheets.class.getResource(target)
        ).toExternalForm();
    }

    /**
     * Registers all entries to main reg
     */
    public static void register(@NotNull RegisterStylesheetsEvent event) {
        LIST.register(event.reg);
    }
}
