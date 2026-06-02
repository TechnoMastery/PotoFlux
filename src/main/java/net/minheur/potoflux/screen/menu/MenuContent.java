package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.loader.mod.events.RegisterMenuEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.menu.definers.AccountMenu;
import net.minheur.potoflux.screen.menu.definers.MenuDefiners;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Lists all potoflux's menus
 */
public class MenuContent {
    /**
     * Actua list of menus
     */
    private final RegistryList<PotoMenuItem> LIST = new RegistryList<>();
    /**
     * Weather the list has been generated
     */
    private static boolean hasGenerated = false;
    /**
     * Only instance of the class
     */
    public static MenuContent INSTANCE;

    /**
     * The constructor makes sure the reg can only be generated once
     */
    private MenuContent() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    // --- file ---
    public final PotoMenuItem FILE = LIST.add(new PotoMenuItem(fromModId("file"), MenuDefiners.getFileMenu()));
    public final PotoMenuItem ACCOUNT = LIST.add(new PotoMenuItem(fromModId("account"), new AccountMenu()));

    public static void register(@NotNull RegisterMenuEvent event) {
        INSTANCE = new MenuContent();

        INSTANCE.LIST.register(event.reg);
    }
}
