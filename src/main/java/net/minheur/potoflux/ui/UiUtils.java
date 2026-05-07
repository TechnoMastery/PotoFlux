package net.minheur.potoflux.ui;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckForNull;
import javax.swing.*;
import java.util.Objects;
import java.util.Optional;

/**
 * A utility class for UI related
 */
public class UiUtils {

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
