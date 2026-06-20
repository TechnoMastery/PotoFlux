package net.minheur.potoflux.styles;

import net.minheur.potoflux.loader.mod.events.RegisterStylesheetsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class PtfStylesheets {
    private static final RegistryList<StylesheetEntry> LIST = new RegistryList<>();

    public static final SmartSupplier<StylesheetEntry> ACCOUNT_NOTIFICATION = LIST.add(() -> new StylesheetEntry(fromModId("account_notification"),
            buildExternal("/styles/tabs/account/notifications.css")));
    public static final SmartSupplier<StylesheetEntry> MOD_LIST_ENTRY = LIST.add(() -> new StylesheetEntry(fromModId("mod_list_entry"),
            buildExternal("/styles/tabs/mods/listEntry.css")));

    private static String buildExternal(String target) {
        return Objects.requireNonNull(
                PtfStylesheets.class.getResource(target)
        ).toExternalForm();
    }

    public static void register(@NotNull RegisterStylesheetsEvent event) {
        LIST.register(event.reg);
    }
}
