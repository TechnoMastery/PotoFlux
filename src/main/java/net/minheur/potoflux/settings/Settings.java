package net.minheur.potoflux.settings;

import javafx.collections.FXCollections;
import net.minheur.potoflux.loader.mod.events.RegisterSettingEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.settings.types.CheckboxSetting;
import net.minheur.potoflux.settings.types.ComboSetting;
import net.minheur.potoflux.terminal.ASCIIs;
import net.minheur.potoflux.theme.Themes;
import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Settings {
    private final RegistryList<Setting> LIST = new RegistryList<>();
    private static boolean hasGenerated = false;

    public static Settings INSTANCE;

    private Settings() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    public final Setting LANG = LIST.add(new Setting(fromModId("lang"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.lang"),
                    FXCollections.observableArrayList(Lang.values()),
                    Lang.EN
            ), true));
    public final Setting ASCII = LIST.add(new Setting(fromModId("ascii"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.ascii"),
                    FXCollections.observableArrayList(ASCIIs.values()),
                    ASCIIs.BIG
            ), false));
    public final Setting ASCII_ON_START = LIST.add(new Setting(fromModId("ascii_on_start"),
            new CheckboxSetting(
                    Translations.get("potoflux:prefs.ascii_on_start"),
                    true
            ), false));
    public final Setting THEME = LIST.add(new Setting(fromModId("theme"),
            new ComboSetting<>(
                    Translations.get("potoflux:prefs.theme"),
                    FXCollections.observableArrayList(Themes.values()),
                    Themes.DEFAULT
            ), true));

    public static void register(RegisterSettingEvent event) {
        INSTANCE = new Settings();

        INSTANCE.LIST.register(event.reg);
    }
}
