package net.minheur.potoflux.ui;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.minheur.potoflux.actionRuns.LogicDelayedPopupsRegistry;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * A utility class for UI related
 */
public final class UiUtils {

    /**
     * Button type preset for the {@code OK} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> okButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:ok"), ButtonBar.ButtonData.OK_DONE)
    );
    /**
     * Button type preset for the {@code CONFIRM} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> confirmButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:confirm"), ButtonBar.ButtonData.OK_DONE)
    );
    /**
     * Button type preset for the {@code CANCEL} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> cancelButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:cancel"), ButtonBar.ButtonData.CANCEL_CLOSE)
    );
    /**
     * Button type preset for the {@code CLOSE} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> closeButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:close"), ButtonBar.ButtonData.CANCEL_CLOSE)
    );
    /**
     * Button type preset for the {@code YES} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> yesButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:yes"), ButtonBar.ButtonData.YES)
    );
    /**
     * Button type preset for the {@code NO} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> noButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:no"), ButtonBar.ButtonData.NO)
    );
    /**
     * Button type preset for the {@code APPLY} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> applyButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:apply"), ButtonBar.ButtonData.APPLY)
    );
    /**
     * Button type preset for the {@code FINISH} action. Include traduction
     */
    public static final SmartSupplier<ButtonType> finishButton = new SmartSupplier<>(() ->
            new ButtonType(Translations.get("common:finish"), ButtonBar.ButtonData.FINISH)
    );

    /**
     * Makes sure the class cannot be instanced
     */
    private UiUtils() {
    }

    /**
     * Show an {@link Alert} to the user<br>
     * If {@link LogicDelayedPopupsRegistry#isOpened()} is {@code true}, meaning we are actually running the logic action runs, adds the alert to the delayed popup reg.
     *
     * @param type    the {@link Alert.AlertType} of the alert
     * @param message what will be displayed in the main message area
     * @param title   displayed in the alert's bar
     * @param header  what is displayed in the upper area
     * @param cssDir  path to the file containing the styles for the alert
     * @param iconDir path to the icon's file used as the header's graphic
     */
    public static void showAlert(
            Alert.AlertType type,
            String message, String title, String header,
            @Nullable String cssDir,
            @Nullable String iconDir
    ) {
        if (LogicDelayedPopupsRegistry.isOpened())
            LogicDelayedPopupsRegistry.addItem(
                    () -> pureShowAlert(
                            type,
                            message, title, header,
                            cssDir, iconDir
                    )
            );
        else pureShowAlert(type, message, title, header, cssDir, iconDir);
    }

    /**
     * Show an {@link Alert} to the user<br>
     *
     * @param type    the {@link Alert.AlertType} of the alert
     * @param message what will be displayed in the main message area
     * @param title   displayed in the alert's bar
     * @param header  what is displayed in the upper area
     * @param cssDir  path to the file containing the styles for the alert
     * @param iconDir path to the icon's file used as the header's graphic
     */
    private static void pureShowAlert(
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

        switch (type) {
            case INFORMATION, ERROR, WARNING -> {
                alert.getDialogPane().getButtonTypes().clear();
                alert.getDialogPane().getButtonTypes().add(okButton.get());
            }
            case CONFIRMATION -> {
                alert.getDialogPane().getButtonTypes().clear();
                alert.getDialogPane().getButtonTypes().addAll(yesButton.get(), noButton.get());
            }
        }

        // show
        alert.showAndWait();
    }

    /**
     * Displays an error pane
     *
     * @param message displayed to the user, actual error
     */
    public static void showErrorPane(String message, String header) {
        showAlert(
                Alert.AlertType.ERROR,
                message, Translations.get("common:error"), header,
                null, null // todo: create css & icon files
        );
    }
    public static void showErrorPane(String message) {
        showErrorPane(message, "An error happened");
    }

    /**
     * Displays an information pane
     *
     * @param message displayed to the user, actual info
     */
    public static void showMessagePane(String message, String header) {
        showAlert(
                Alert.AlertType.INFORMATION,
                message, Translations.get("common:info"), header,
                null, null // todo: create css & icon files
        );
    }
    public static void showMessagePane(String message) {
        showMessagePane(message, null); // todo: find something to write here
    }

    /**
     * Displays a confirmation dialog
     *
     * @param content     message node to display as confirmation
     * @param header      text displayed in the alert's header
     * @param preciseType if specified, overrides the {@linkplain Alert.AlertType#CONFIRMATION} default value
     * @return weather the user confirmed
     */
    public static boolean showConfirmationDialog(
            Node content,
            @Nullable String header,
            @Nullable Alert.AlertType preciseType
    ) {
        Alert alert = new Alert(
                preciseType == null ? Alert.AlertType.CONFIRMATION : preciseType,
                "",
                noButton.get(),
                yesButton.get()
        );
        alert.setTitle(Translations.get("common:confirm"));
        alert.getDialogPane().setContent(content);
        alert.setHeaderText(header);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton.get();
    }

    /**
     * Displays a confirmation dialog
     *
     * @param content message node to display as confirmation
     * @param header  text displayed in the alert's header
     * @return weather the user confirmed
     */
    public static boolean showConfirmationDialog(Node content, @Nullable String header) {
        return showConfirmationDialog(content, header, null);
    }

    /**
     * Displays a confirmation dialog
     *
     * @param content message node to display as confirmation
     * @return weather the user confirmed
     */
    public static boolean showConfirmationDialog(Node content) {
        return showConfirmationDialog(content, null);
    }

    /**
     * Displays a confirmation dialog
     *
     * @param message text to display as confirmation
     * @return weather the user confirmed
     */
    public static boolean showConfirmationDialog(String message) {
        return showConfirmationDialog(new Label(message));
    }

    /**
     * Displays an input dialog
     *
     * @param options      array of options that the user can choose from.
     * @param defaultIndex index on what we can find the default selected value
     * @param title        of the dialog
     * @param header       text displayed in the dialog's header
     * @param content      text displayed as query
     * @param iconDir      path to the icon file for the alert
     * @param cssDir       path to the file containing the styles for the alert
     * @param <R>          type of the input. This type gets returned and the options are also of this type
     * @return which item has been selected, or {@code null} if the user canceled
     */
    public static <R> @Nullable R showInputDialog(
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

    /**
     * Displays an input dialog
     *
     * @param options      array of options that the user can choose from.
     * @param defaultIndex index on what we can find the default selected value
     * @param title        of the dialog
     * @param header       text displayed in the dialog's header
     * @param content      text displayed as query
     * @param cssDir       path to the file containing the styles for the alert
     * @param <R>          type of the input. This type gets returned and the options are also of this type
     * @return which item has been selected, or {@code null} if the user canceled
     */
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

    /**
     * Displays an input dialog
     *
     * @param options      array of options that the user can choose from.
     * @param defaultIndex index on what we can find the default selected value
     * @param title        of the dialog
     * @param header       text displayed in the dialog's header
     * @param content      text displayed as query
     * @param <R>          type of the input. This type gets returned and the options are also of this type
     * @return which item has been selected, or {@code null} if the user canceled
     */
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

    public static boolean askRetry() {
        return UiUtils.showConfirmationDialog(
                new Label("Do you want to retry?"), // todo
                "The execution of the action failed." // todo
        );
    }

    /**
     * Hides a node, and also makes sure it isn't managed so it don't take empty room
     *
     * @param node component to disable
     */
    public static void hideNode(@NotNull Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    /**
     * Show a node, used to invert action of {@link #hideNode(Node)}
     *
     * @param node component to enable
     */
    public static void showNode(@NotNull Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

}
