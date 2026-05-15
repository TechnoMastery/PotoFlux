package net.minheur.potoflux.ui;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.minheur.potoflux.Functions;
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
    public static final SmartSupplier<ButtonType> validateButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:validate"), ButtonBar.ButtonData.OK_DONE)
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

    public static void showAlert(
            Alert.AlertType type,
            String message, String title, String header,
            @Nullable String cssDir,
            @Nullable String iconDir
    ) {
        Alert alert = new Alert(type);

        // window
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        // css
        if (cssDir != null) alert.getDialogPane().getStylesheets().add(
                UiUtils.class.getResource(cssDir).toExternalForm()
        );

        // icon
        if (iconDir != null) {
            Stage stage = ((Stage) alert.getDialogPane().getScene().getWindow());
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(UiUtils.class.getResourceAsStream(iconDir))
            ));
        }

        // show
        alert.showAndWait();
    }

    public static void showErrorPane(String message) {
        showAlert(
                Alert.AlertType.ERROR,
                message, Translations.get("common:error"), "An error happened", // TODO
                null, null // todo: create css & icon files
        );
    }
    public static void showMessagePane(String message) {
        showAlert(
                Alert.AlertType.INFORMATION,
                message, Translations.get("common:info"), null, // todo: find something to write here
                null, null // todo: create css & icon files
        );
    }

    public static boolean showConfirmationDialog(
            Node content,
            @Nullable String header
    ) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle(Translations.get("common:confirm"));
        alert.getDialogPane().setContent(content);
        alert.setHeaderText(header);

        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO))
                .setDefaultButton(true);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES))
                .setDefaultButton(false);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
    public static boolean showConfirmationDialog(Node content) {
        return showConfirmationDialog(content, null);
    }
    public static boolean showConfirmationDialog(String message) {
        return showConfirmationDialog(new Label(message));
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
