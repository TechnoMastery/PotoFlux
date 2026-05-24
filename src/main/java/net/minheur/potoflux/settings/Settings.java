package net.minheur.potoflux.settings;

import net.minheur.potoflux.loader.mod.events.RegisterSettingEvent;
import net.minheur.potoflux.registry.RegistryList;

public class Settings {
    private final RegistryList<Setting> LIST = new RegistryList<>();
    private static boolean hasGenerated = false;

    public static Settings INSTANCE;

    private Settings() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }



    public static void register(RegisterSettingEvent event) {
        INSTANCE = new Settings();

        INSTANCE.LIST.register(event.reg);
    }
}
