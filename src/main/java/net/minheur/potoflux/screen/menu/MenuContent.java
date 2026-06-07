package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.loader.mod.events.RegisterMenuEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.menu.definers.AccountMenu;
import net.minheur.potoflux.screen.menu.definers.MenuDefiners;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Lists all potoflux's menus
 */
public class MenuContent {
    /**
     * Actua list of menus
     */
    private static final RegistryList<PotoMenuItem> LIST = new RegistryList<>();

    // --- file ---
    /**
     * Item for the file menu
     */
    public static final SmartSupplier<PotoMenuItem> FILE = LIST.add(() -> new PotoMenuItem(fromModId("file"), MenuDefiners.getFileMenu()));
    /**
     * Item for the account menu
     */
    public static final SmartSupplier<PotoMenuItem> ACCOUNT = LIST.add(() -> new PotoMenuItem(fromModId("account"), new AccountMenu()));

    /**
     * Instances the reg and puts all into the event
     * @param event to add items to
     */
    public static void register(@NotNull RegisterMenuEvent event) {
        LIST.register(event.reg);
    }
}
