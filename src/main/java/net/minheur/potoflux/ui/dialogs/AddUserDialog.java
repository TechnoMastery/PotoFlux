package net.minheur.potoflux.ui.dialogs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.ui.UiUtils;
import net.minheur.potoflux.ui.dialogData.NewAccountData;

import java.util.*;

import static net.minheur.potoflux.login.ConnectionHandler.account;

/**
 * Used by admins when creating a new account for someone
 */
public class AddUserDialog extends Dialog<NewAccountData> {

    private GridPane grid;

    private TextField emailField;
    private PasswordField passwordField;
    private TextField firstName;
    private TextField lastName;
    private Spinner<Integer> rankSpinner;

    private ListView<Perms> permsList;
    private Map<Perms, BooleanProperty> selectedPermMap;

    public AddUserDialog() {
        setTitle("Add user"); // todo
        initUI();
    }

    private void initUI() {

        setupButton();
        setupForm();

        addEmail();
        addPassword();
        addFirstName();
        addLastName();
        addRank();
        addPermList();

        getDialogPane().setContent(grid);

        ((Button) getDialogPane().lookupButton(UiUtils.confirmButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                .setCancelButton(true);

        setupResult();
    }

    private void addRank() {
        rankSpinner = new Spinner<>();
        rankSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0, 100, account.rank +1, 1
                )
        );
        rankSpinner.setPrefWidth(100);

        grid.add(new Label("Rank: "), 0, 4);
        grid.add(rankSpinner, 1, 4);
    }

    private void setupButton() {
        getDialogPane().getButtonTypes().addAll(
                UiUtils.cancelButton.get(),
                UiUtils.confirmButton.get()
        );
    }

    private void addPermList() {
        permsList = new ListView<>();
        ObservableList<Perms> items = FXCollections.observableArrayList();

        for (Perms perm : Perms.values())
            if (Arrays.asList(account.perms).contains(perm))
                items.add(perm);
        permsList.setItems(items);

        selectedPermMap = new HashMap<>();

        for (Perms perm : items)
            selectedPermMap.put(perm, new SimpleBooleanProperty(false));
        permsList.setCellFactory(CheckBoxListCell.forListView(
                selectedPermMap::get
        ));

        permsList.setFocusTraversable(true);
        permsList.setPrefSize(200, 250);

        grid.add(permsList, 0, 5);
        GridPane.setColumnSpan(permsList, 2);
    }

    private void addLastName() {
        lastName = new TextField();
        lastName.setPrefWidth(250);

        grid.add(new Label("Last name: "), 0, 3);
        grid.add(lastName, 1, 3);
    }

    private void addFirstName() {
        firstName = new TextField();
        firstName.setPrefWidth(250);

        grid.add(new Label("First name: "), 0, 2);
        grid.add(firstName, 1, 2);
    }

    private void addPassword() {
        passwordField = new PasswordField();
        passwordField.setPrefWidth(250);

        grid.add(new Label("Password: "), 0, 1);
        grid.add(passwordField, 1, 1);
    }

    private void addEmail() {
        emailField = new TextField();
        emailField.setPrefWidth(250);

        grid.add(new Label("Email: "), 0, 0);
        grid.add(emailField, 1, 0);
    }

    private void setupForm() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
    }

    private void setupResult() {
        setResultConverter(buttonType -> {

            if (buttonType == UiUtils.confirmButton.get()) {
                NewAccountData result = new NewAccountData();

                result.email = emailField.getText();
                result.password = passwordField.getText();
                result.firstName = firstName.getText();
                result.lastName = lastName.getText();
                result.rank = rankSpinner.getValue();
                result.perms = selectedPermMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().get())
                        .map(Map.Entry::getKey)
                        .toList();

                return result;

            } else return null;

        });
    }

}
