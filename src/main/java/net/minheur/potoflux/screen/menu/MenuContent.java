package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.loader.mod.events.RegisterMenuEvent;
import net.minheur.potoflux.registry.RegistryList;

public class MenuContent {
    private final RegistryList<PotoMenuItem> LIST = new RegistryList<>();
    private static boolean hasGenerated = false;
    public static MenuContent INSTANCE;

    private MenuContent() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }



    public static void register(RegisterMenuEvent event) {
        INSTANCE = new MenuContent();

        INSTANCE.LIST.register(event.reg);
    }
}
