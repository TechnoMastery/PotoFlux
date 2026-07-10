package net.minheur.potoflux.theme;

import atlantafx.base.theme.*;
import net.minheur.potoflux.loader.mod.events.RegisterThemesEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.SmartSupplier;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * List of all themes that can be chosen from in the settings
 */
public class Themes {

    private static final RegistryList<PotofluxTheme> LIST = new RegistryList<>();

    public static final SmartSupplier<PotofluxTheme> DRACULA = LIST.add(() -> new PotofluxTheme(
            fromModId("dracula"), Translations.get("potoflux:theme.dracula"), new Dracula()
    ));
    public static final SmartSupplier<PotofluxTheme> CUPERTINO_DARK = LIST.add(() -> new PotofluxTheme(
            fromModId("cuper_dark"), Translations.get("potoflux:theme.cupertino.dark"), new CupertinoDark()
    ));
    public static final SmartSupplier<PotofluxTheme> CUPERTINO_LIGHT = LIST.add(() -> new PotofluxTheme(
            fromModId("cuper_light"), Translations.get("potoflux:theme.cupertino.light"), new CupertinoLight()
    ));
    public static final SmartSupplier<PotofluxTheme> NORD_DARK = LIST.add(() -> new PotofluxTheme(
            fromModId("nord_dark"), Translations.get("potoflux:theme.nord.dark"), new NordDark()
    ));
    public static final SmartSupplier<PotofluxTheme> NORD_LIGHT = LIST.add(() -> new PotofluxTheme(
            fromModId("nord_light"), Translations.get("potoflux:theme.nord.light"), new NordLight()
    ));
    public static final SmartSupplier<PotofluxTheme> PRIMER_DARK = LIST.add(() -> new PotofluxTheme(
            fromModId("primer_dark"), Translations.get("potoflux:theme.primer.dark"), new PrimerDark()
    ));
    public static final SmartSupplier<PotofluxTheme> PRIMER_LIGHT = LIST.add(() -> new PotofluxTheme(
            fromModId("primer_light"), Translations.get("potoflux:theme.primer.light"), new PrimerLight()
    ));

    public static void register(RegisterThemesEvent event) {
        LIST.register(event.reg);
    }
}
