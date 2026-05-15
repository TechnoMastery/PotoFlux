package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.minheur.potoflux.ui.UiUtils;
import net.minheur.potoflux.ui.dialogData.NewAccountData;

/**
 * This dialog is used when you create your own account
 */
public class CreateAccountDialog extends Dialog<NewAccountData> {

    private GridPane grid;

    private TextField emailField;
    private PasswordField passwordField;
    private TextField firstName;
    private TextField lastName;

    public CreateAccountDialog() {
        setTitle("Create Account"); // todo
        initGui();
    }

    private void initGui() {

        setupButtons();
        setupForm();

        addEmail();
        addPassword();
        addFirstName();
        addLastName();

        getDialogPane().setContent(grid);

        ((Button) getDialogPane().lookupButton(UiUtils.okButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                .setCancelButton(true);

        setupResult();
    }

    private void setupResult() {
        setResultConverter(buttonType -> {

            if (buttonType == UiUtils.okButton.get()) {
                NewAccountData result = new NewAccountData();

                result.email = emailField.getText();
                result.password = passwordField.getText();
                result.firstName = firstName.getText();
                result.lastName = lastName.getText();

                return result;

            } else return null;

        });
    }

    private void setupButtons() {

        getDialogPane().getButtonTypes().addAll(
                UiUtils.cancelButton.get(),
                UiUtils.okButton.get()
        );

    }

    private void addLastName() {
        lastName = new TextField();
        lastName.setPrefWidth(250);

        grid.add(new Label("Last name : "), 0, 3);
        grid.add(lastName, 1, 3);
    }

    private void addFirstName() {
        firstName = new TextField();
        firstName.setPrefWidth(250);

        grid.add(new Label("First name : "), 0, 2); // todo
        grid.add(firstName, 1, 2);
    }

    private void addPassword() {
        passwordField = new PasswordField();
        passwordField.setPrefWidth(250);

        grid.add(new Label("Password : "), 0, 1); // todo
        grid.add(passwordField, 1, 1);
    }

    private void addEmail() {
        emailField = new TextField();
        emailField.setPrefWidth(250);

        grid.add(new Label("Email : "), 0, 0); // todo
        grid.add(emailField, 1, 0);
    }

    private void setupForm() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
    }

}
