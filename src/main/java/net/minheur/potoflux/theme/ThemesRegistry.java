package net.minheur.potoflux.theme;

import net.minheur.potoflux.registry.AbstractRegistry;

public class ThemesRegistry extends AbstractRegistry<PotofluxTheme> {

    public PotofluxTheme getFromKey(String key) {
        for (PotofluxTheme theme : getAll())
            if (theme.returnValue().equals(key))
                return theme;
        return Themes.DRACULA.get();
    }

}
