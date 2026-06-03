package net.minheur.potoflux.settings;

import javafx.collections.FXCollections;
import net.minheur.potoflux.loader.mod.events.RegisterSettingEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.TabSides;
import net.minheur.potoflux.settings.types.CheckboxSetting;
import net.minheur.potoflux.settings.types.ComboSetting;
import net.minheur.potoflux.terminal.ASCIIs;
import net.minheur.potoflux.theme.Themes;
import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * List of all Potoflux's settings
 */
public class Settings {
    /**
     * Actual list of all settings
     */
    private final RegistryList<Setting> LIST = new RegistryList<>();
    /**
     * Weather the reg has been generated yet
     */
    private static boolean hasGenerated = false;

    /**
     * Only instance of the class
     */
    public static Settings INSTANCE;

    /**
     * Constructor that checks if the reg has already been created
     */
    private Settings() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    /**
     * Setting to change the lang of the app, between the values of {@link Lang}
     */
    public final Setting LANG = LIST.add(new Setting(fromModId("lang"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.lang"),
                    FXCollections.observableArrayList(Lang.values()),
                    Lang.EN
            ), true));
    /**
     * Setting to change the ASCII that get printed on startup<br>
     * Values is between {@link ASCIIs}
     */
    public final Setting ASCII = LIST.add(new Setting(fromModId("ascii"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.ascii"),
                    FXCollections.observableArrayList(ASCIIs.values()),
                    ASCIIs.BIG
            ), false));
    /**
     * Weather the terminal's ASCII need to be printed even if it isn't empty
     */
    public final Setting ASCII_ON_START = LIST.add(new Setting(fromModId("ascii_on_start"),
            new CheckboxSetting(
                    Translations.get("potoflux:prefs.ascii_on_start"),
                    true
            ), false));
    /**
     * Setting to change the theme of the app. The themes are listed in {@link Themes}
     */
    public final Setting THEME = LIST.add(new Setting(fromModId("theme"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.theme"),
                    FXCollections.observableArrayList(Themes.values()),
                    Themes.DEFAULT
            ), true));
    /**
     * Setting to change the placement of the tab's list. Is within {@link TabSides}
     */
    public final Setting TAB_PLACEMENT = LIST.add(new Setting(fromModId("tab_placement"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.tab_placement"),
                    FXCollections.observableArrayList(TabSides.values()),
                    TabSides.LEFT
            ), true));

    /**
     * Create and registers all items
     * @param event to add items to
     */
    public static void register(@NotNull RegisterSettingEvent event) {
        INSTANCE = new Settings();

        INSTANCE.LIST.register(event.reg);
    }
}
