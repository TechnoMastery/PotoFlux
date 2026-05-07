package net.minheur.potoflux.ui;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.SmartSupplier;

import javax.annotation.CheckForNull;
import javax.swing.*;

/**
 * A utility class for UI related
 */
public final class UiUtils {

    public static final SmartSupplier<ButtonType> okButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:ok"), ButtonBar.ButtonData.OK_DONE)
    );
    public static final SmartSupplier<ButtonType> cancelButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:cancel"), ButtonBar.ButtonData.CANCEL_CLOSE)
    );
    public static final SmartSupplier<ButtonType> yesButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:yes"), ButtonBar.ButtonData.YES)
    );
    public static final SmartSupplier<ButtonType> noButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:no"), ButtonBar.ButtonData.NO)
    );
    public static final SmartSupplier<ButtonType> applyButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:apply"), ButtonBar.ButtonData.NO)
    );
    public static final SmartSupplier<ButtonType> finishButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:finish"), ButtonBar.ButtonData.NO)
    );

    private UiUtils() {}

    public static void showErrorPane(String message) {
        JOptionPane.showMessageDialog(
                getAppAnchor(),
                message,
                Translations.get("common:error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showMessagePane(String message) {
        JOptionPane.showMessageDialog(
                getAppAnchor(),
                message,
                Translations.get("common:info"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Deprecated
    @CheckForNull
    public static JFrame getAppAnchor() {
        return PotoFlux.app == null ?
                null : PotoFlux.app.getFrame();
    }

}
