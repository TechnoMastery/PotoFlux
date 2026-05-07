package net.minheur.potoflux.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minheur.potoflux.utils.SmartSupplier;

import javax.annotation.CheckForNull;
import javax.swing.*;
import java.util.Objects;
import java.util.Optional;

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

        Alert alert = new Alert(Alert.AlertType.ERROR);

        // window
        alert.setTitle(Translations.get("common:error"));
        alert.setHeaderText("An error happened"); // todo
        alert.setContentText(message);

        // css
//        alert.getDialogPane().getStylesheets().add(
//                UiUtils.class.getResource("styles/panes/error.css").toExternalForm() todo: add default css
//        );

        // icon
//        Stage stage = ((Stage) alert.getDialogPane().getScene().getWindow());
//        stage.getIcons().add(new Image(
//                Objects.requireNonNull(UiUtils.class.getResourceAsStream("textures/panes/error.png")) todo: create image
//        ));

        // show
        alert.showAndWait();
    }

    public static void showMessagePane(String message) {
        JOptionPane.showMessageDialog(
                getAppAnchor(),
                message,
                Translations.get("common:info"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static <R> R showInputDialog(
            @NotNull R[] options, int defaultIndex,
            @Nullable String title,
            @Nullable String header,
            @Nullable String content,
            @Nullable String iconDir, @Nullable String cssDir
    ) {
        ChoiceDialog<R> dialog = new ChoiceDialog<>(
                options[defaultIndex], options
        );
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        if (iconDir != null) {
            Stage dialogStage = ((Stage) dialog.getDialogPane().getScene().getWindow());
            dialogStage.getIcons().add(new Image(
                    Objects.requireNonNull(UiUtils.class.getResourceAsStream(iconDir))
            ));
        }

        if (cssDir != null)
            dialog.getDialogPane().getStylesheets().add(
                    UiUtils.class.getResource(cssDir).toExternalForm()
            );

        Optional<R> result = dialog.showAndWait();

        return result.orElse(null);
    }
    public static <R> R showInputDialog(
            @NotNull R[] options, int defaultIndex,
            @Nullable String title,
            @Nullable String header,
            @Nullable String content,
            String cssDir
    ) {
        return showInputDialog(
                options, defaultIndex,
                title, header, content,
                null, cssDir
        );
    }
    public static <R> R showInputDialog(
            @NotNull R[] options, int defaultIndex,
            @Nullable String title,
            @Nullable String header,
            @Nullable String content
    ) {
        return showInputDialog(
                options, defaultIndex,
                title, header, content,
                null, null
        );
    }

    @Deprecated
    @CheckForNull
    public static JFrame getAppAnchor() {
        return PotoFlux.app == null ?
                null : PotoFlux.app.getFrame();
    }

}
