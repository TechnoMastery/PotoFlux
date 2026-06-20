package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;

/**
 * This dialog asks an admin for a username, to delete his account.
 */
public class RmUserDialog extends Dialog<String> {

    /**
     * Grid that contains all content
     */
    private GridPane grid;

    /**
     * Field to input the email of the account to delete
     */
    private TextField rmUserEmail;

    /**
     * Creates the dialog
     */
    public RmUserDialog() {
        setTitle("Remove a user"); // todo
        initUI();
    }

    /**
     * Creates and setups the dialog
     */
    private void initUI() {

        setupButtons();
        setupForm();

        addEmail();

        getDialogPane().setContent(grid);

        ((javafx.scene.control.Button) getDialogPane().lookupButton(UiUtils.confirmButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                .setCancelButton(true);

        setupResult();
    }

    /**
     * Add the buttons to the dialog's button bar
     */
    private void setupButtons() {
        getDialogPane().getButtonTypes().addAll(
                UiUtils.cancelButton.get(),
                UiUtils.confirmButton.get()
        );
    }

    /**
     * Shows a confirmation popup to delete the account
     *
     * @return weather the user confirmed to removal
     */
    private boolean showConfirm() {
        return UiUtils.showConfirmationDialog(Functions.formatMessage(
                Translations.get("potoflux:tabs.account.rmUser.check"),
                rmUserEmail.getText()
        ));
    }

    /**
     * Adds the dialog's result converter
     */
    private void setupResult() {
        setResultConverter(buttonType -> {

            if (buttonType == UiUtils.confirmButton.get()) {

                if (!showConfirm()) return null;

                return rmUserEmail.getText().trim();

            } else return null;

        });
    }

    /**
     * Adds the email field for the account to remove
     */
    private void addEmail() {
        rmUserEmail = new TextField();
        rmUserEmail.setPrefWidth(250);

        grid.add(new Label(Translations.get("common:emailField")), 0, 0);
        grid.add(rmUserEmail, 1, 0);
    }

    /**
     * Sets up the {@linkplain #grid} layout
     */
    private void setupForm() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
    }

}
