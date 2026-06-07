package net.minheur.potoflux.ui.dialogs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.minheur.potoflux.login.*;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.login.response.MdUserInfosResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.Json;
import net.minheur.potoflux.utils.LockableField;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static net.minheur.potoflux.Functions.formatMessage;
import static net.minheur.potoflux.ui.UiUtils.*;

/**
 * When you ask for a user's details, this dialog pops up
 */
public class AccountDetailsDialog extends Dialog<Void> {

    /**
     * Parent dialog of the details, used when {@link #closeParent()}
     */
    private final Dialog<?> parent;

    /**
     * Specific account that this dialog is for
     */
    private final Account account;
    /**
     * List of perms owned by the admin, gotten from {@link ConnectionHandler#account}
     */
    private final List<Perms> actualPerms = new ArrayList<>();

    /**
     * Grid containing all components for the dialog
     */
    private GridPane grid;

    /**
     * Field for the {@linkplain #account}'s email
     */
    private TextField emailField;
    /**
     * Field for the {@linkplain #account}'s first name
     */
    private TextField firstNameField;
    /**
     * Field for the {@linkplain #account}'s last name
     */
    private TextField lastNameField;
    /**
     * Field for the {@linkplain #account}'s rank
     */
    private Spinner<Integer> rankSpinner;

    /**
     * Actual displayed list of all perms, containing perms owned by the {@linkplain #account}  and by the admin
     */
    private ListView<Perms> permsList;
    /**
     * Map of all perms, being ticked or not.
     */
    private Map<Perms, LockableField<BooleanProperty>> selectedPermMap;

    /**
     * Button to open the password reset popup
     */
    private ButtonType changePasswordButton;
    /**
     * Button to toggle locked state of the {@linkplain #account}
     */
    private ButtonType lockButton;

    /**
     * Creates the dialog, adds the buttons, the components and sets up the layout
     * @param parent the {@link AllUsersDialog} that called this one
     * @param account to display details of
     */
    public AccountDetailsDialog(Dialog<?> parent, Account account) {
        setTitle("Account details"); // todo
        this.parent = parent;
        this.account = account;

        setupButtons();
        setupPanel();

        setupColumns();

        addEmail();
        addName();
        addRank();
        addPerms();

        getDialogPane().setContent(grid);

        fillActualPerms();
        reload();

    }

    /**
     * Creates the constraints of the different colums
     */
    private void setupColumns() {
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();

        col2.setHgrow(Priority.ALWAYS);
        col3.setHgrow(Priority.ALWAYS);

        col2.setFillWidth(true);
        col3.setFillWidth(true);

        grid.getColumnConstraints().addAll(col1, col2, col3);
    }

    /**
     * Adds the {@link #permsList} into the {@linkplain #grid}<br>
     * Will add all perms owned by the {@linkplain #account}, wich are all ticked, and all the ones of the admin (got from {@link #actualPerms}).<br>
     * The admin's perm are ticked if the user already has them, otherwise they aren't ticked. If the user has ticked perms that the admin don't, it is disabled and cannot be changed.
     */
    private void addPerms() {
        permsList = new ListView<>();
        ObservableList<Perms> items = FXCollections.observableArrayList();

        List<Perms> targetPerms = Arrays.asList(account.perms);
        List<Perms> currentPerms = Arrays.asList(ConnectionHandler.account.perms);

        for (Perms perm : Perms.values())
            if (
                    targetPerms.contains(perm) || // if target account has the perm
                    currentPerms.contains(perm) // if current account can add the perm
            ) items.add(perm);
        permsList.setItems(items);

        selectedPermMap = new HashMap<>();

        for (Perms perm : items) {
            BooleanProperty selected = new SimpleBooleanProperty(targetPerms.contains(perm)); // pre-select if target has it -- get as boolean property
            boolean locked = !currentPerms.contains(perm); // can't change it if current user doesn't have it
            selectedPermMap.put(perm, new LockableField<>(selected, locked));
        }

        permsList.setCellFactory(listView -> new CheckBoxListCell<>(
                item -> selectedPermMap.get(item).get()
        ) {

            @Override
            public void updateItem(Perms item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                LockableField<BooleanProperty> field = selectedPermMap.get(item);
                ((CheckBox) getGraphic()).selectedProperty().bindBidirectional(field.get());
                getGraphic().setDisable(field.getIsLocked());
            }

        });

        permsList.setFocusTraversable(true);
        permsList.setPrefSize(200, 250);

        grid.add(permsList, 0, 3);
        GridPane.setColumnSpan(permsList, 3);
    }

    /**
     * Reloads the UI, toggling visible or editable depending on the actual user permissions
     */
    private void reload() {
        if (actualPerms.contains(Perms.CHANGE_INFORMATIONS)) {

            hideNode(getDialogPane().lookupButton(okButton.get()));
            showNode(getDialogPane().lookupButton(confirmButton.get()));
            showNode(getDialogPane().lookupButton(cancelButton.get()));

            emailField.setEditable(true);
            firstNameField.setEditable(true);
            lastNameField.setEditable(true);
            rankSpinner.setEditable(true);

        } else {

            showNode(getDialogPane().lookupButton(okButton.get()));
            hideNode(getDialogPane().lookupButton(confirmButton.get()));
            hideNode(getDialogPane().lookupButton(cancelButton.get()));

            emailField.setEditable(false);
            firstNameField.setEditable(false);
            lastNameField.setEditable(false);
            rankSpinner.setEditable(false);

        }

        getDialogPane().lookupButton(changePasswordButton).setVisible(
                actualPerms.contains(Perms.CHANGE_PASSWORD)
        );
        getDialogPane().lookupButton(lockButton).setVisible(
            actualPerms.contains(Perms.LOCK)
        );
        ((Button) getDialogPane().lookupButton(lockButton)).setText(Translations.get(
                "potoflux:tabs.account." + (account.locked ? "unlock" : "lock") + ".button"
        ));
    }

    /**
     * Fills the actual user perms from {@link ConnectionHandler#account} to {@link #actualPerms}
     */
    private void fillActualPerms() {
        Perms[] permsArray = ConnectionHandler.account.perms;
        actualPerms.addAll(List.of(permsArray));
    }

    /**
     * Creates {@linkplain #lockButton} and {@linkplain #changePasswordButton}, add all to the button bar then define their action
     */
    private void setupButtons() {
        lockButton = new ButtonType(Translations.get("potoflux:tabs.account.lock.button"), ButtonBar.ButtonData.OTHER);
        changePasswordButton = new ButtonType(Translations.get("potoflux:tabs.account.mdUserPassword.button"), ButtonBar.ButtonData.OTHER);

        getDialogPane().getButtonTypes().addAll(
                lockButton,
                changePasswordButton,
                cancelButton.get(),
                confirmButton.get(),
                okButton.get()
        );

        getDialogPane().lookupButton(confirmButton.get()).addEventFilter(ActionEvent.ACTION, e -> {
            // --- get all fields ---
            String mail = emailField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            int rank = rankSpinner.getValue();
            Perms[] perms = selectedPermMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().get().get())
                    .map(Map.Entry::getKey)
                    .toArray(Perms[]::new);

            // --- get if fields are modified ---
            boolean isMailModified = !account.email.equals(mail);
            boolean isFirstNameModified = !account.firstName.equals(firstName);
            boolean isLastNameModified = !account.lastName.equals(lastName);
            boolean isRankModified = account.rank != rank;
            boolean arePermsModified = account.perms == perms;

            // --- if nothing changed, return ---
            if (!(isMailModified || isFirstNameModified || isLastNameModified || isRankModified || arePermsModified)) {
                closeParent();
                return;
            }

            // --- setup confirm ---
            StringBuilder confirmSb = new StringBuilder(Translations.get("potoflux:tabs.account.mdUserInfos.confirm"));

            if (isMailModified) confirmSb.append("\n").append(Translations.get("common:email"));
            if (isFirstNameModified) confirmSb.append("\n").append(Translations.get("common:firstName"));
            if (isLastNameModified) confirmSb.append("\n").append(Translations.get("common:lastName"));
            if (isRankModified) confirmSb.append("\n").append(Translations.get("common:rank"));
            if (arePermsModified) confirmSb.append("\n").append(Translations.get("common:perms"));

            boolean confirmed = showConfirmationDialog(confirmSb.toString());
            if (!confirmed) {
                closeParent();
                return;
            }

            // --- post request ---
            String content;
            try {
                content = RequestPoster.mdUserInfos(
                        TokenHandler.get(), // current token
                        account.uuid, // target user's uuid
                        // new data
                        isMailModified ? mail : null,
                        isFirstNameModified ? firstName : null,
                        isLastNameModified ? lastName : null,
                        isRankModified ? rank : 0,
                        Arrays.stream(perms)
                                .map(perm -> perm.getCode())
                                .toArray(String[]::new)
                );
            } catch (InvalidTokenException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
                TokenHandler.clear();
                closeParent();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.failed"));
                closeParent();
                return;
            }

            // --- get response ---
            MdUserInfosResponse response = Json.GSON.fromJson(content, MdUserInfosResponse.class);

            // --- handle error ---
            if (!response.success) {
                showErrorPane(
                        response.error == null ? content :
                                switch (response.error) {
                                    case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                                    case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                                    case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                                    case "new_email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                                    case "nothing_changed" -> Translations.get("potoflux:tabs.account.mdUserInfos.noMods");
                                    case "insufficient_rank" -> Translations.get("potoflux:tabs.account.error.insufficientRank");
                                    case "target_locked" -> Translations.get("potoflux:tabs.account.error.targetLocked");
                                    default -> response.error;
                                }
                );
                if (response.error.equals("not_exists") || response.error.equals("token_expired"))
                    TokenHandler.clear();
                closeParent();
                return;
            }

            // --- build result alert ---
            StringBuilder doneSb = new StringBuilder(Translations.get("potoflux:tabs.account.mdUserInfos.done"));

            if (response.emailChanged) doneSb.append("\n").append(Translations.get("common:email"));
            if (response.firstNameChanged) doneSb.append("\n").append(Translations.get("common:firstName"));
            if (response.lastNameChanged) doneSb.append("\n").append(Translations.get("common:lastName"));
            if (response.rankChanged) doneSb.append("\n").append(Translations.get("common:rank"));

            if (response.emailChanged) doneSb.append("\n").append(Translations.get("potoflux:tabs.account.mdUserInfos.idRemember"));

            showMessagePane(doneSb.toString());
            closeParent();
        });

        getDialogPane().lookupButton(changePasswordButton).addEventFilter(ActionEvent.ACTION, e -> {

            TextInputDialog passwordAskDialog = new TextInputDialog();
            passwordAskDialog.setTitle(Translations.get("potoflux:tabs.account.mdUserPassword.title"));
            passwordAskDialog.setHeaderText("Please enter new password for user:"); // todo

            passwordAskDialog.getDialogPane().getButtonTypes().clear();
            passwordAskDialog.getDialogPane().getButtonTypes().addAll(
                    cancelButton.get(),
                    confirmButton.get()
            );
            ((Button) passwordAskDialog.getDialogPane().lookupButton(cancelButton.get()))
                    .setCancelButton(true);
            ((Button) passwordAskDialog.getDialogPane().lookupButton(confirmButton.get()))
                    .setDefaultButton(true);

            Optional<String> result = passwordAskDialog.showAndWait();
            if (result.isEmpty()) {
                e.consume();
                return;
            }

            String newPassword = result.get();

            String content;
            try {
                content = RequestPoster.mdUserPassword(
                        TokenHandler.get(),
                        account.uuid,
                        newPassword
                );
            } catch (InvalidTokenException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
                e.consume();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.failed"));
                e.consume();
                return;
            }

            BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);

            if (!response.success) {
                showErrorPane(
                        response.error == null ? content :
                        switch (response.error) {
                            case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                            case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                            case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                            case "insufficient_rank" -> Translations.get("potoflux:tabs.account.error.insufficientRank");
                            case "target_locked" -> Translations.get("potoflux:tabs.account.error.targetLocked");
                            default -> response.error;
                        }
                );
                e.consume();
                return;
            }

            showMessagePane(formatMessage(
                    Translations.get("potoflux:tabs.account.mdUserPassword.done"),
                    account.email, newPassword
            ));
            e.consume();

        });

        getDialogPane().lookupButton(lockButton).addEventFilter(ActionEvent.ACTION, e -> {
            boolean newState = !this.account.locked;

            String content;
            try {
                content = RequestPoster.lockUser(
                        TokenHandler.get(),
                        account.uuid,
                        newState
                );
            } catch (InvalidTokenException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
                TokenHandler.clear();
                e.consume();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.failed"));
                e.consume();
                return;
            }

            BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);

            if (!response.success) {
                showErrorPane(
                        response.error == null ? content :
                        switch (response.error) {
                            case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                            case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                            case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                            case "insufficient_rank" ->
                                    Translations.get("potoflux:tabs.account.error.insufficientRank");
                            default -> response.error;
                        }
                );
                if (response.error.equals("not_exists") || response.error.equals("token_expired"))
                    TokenHandler.clear();
                e.consume();
                return;
            }

            showMessagePane(Translations.get("potoflux:tabs.account." + (newState ? "lock" : "unlock") + ".done"));
            closeParent();

        });

        ((Button) getDialogPane().lookupButton(cancelButton.get()))
                .setCancelButton(true);
        ((Button) getDialogPane().lookupButton(okButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(confirmButton.get()))
                .setDefaultButton(true);

    }

    /**
     * Closes the {@link #parent}
     */
    private void closeParent() {
        parent.close();
    }

    /**
     * Adds the {@link #rankSpinner} to the {@linkplain #grid}
     */
    private void addRank() {
        rankSpinner = new Spinner<>();
        rankSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0, 100, account.rank, 1
                )
        );
        rankSpinner.setPrefWidth(100);

        grid.add(new Label("Rank: "), 0, 2);
        grid.add(rankSpinner, 1, 2);
        GridPane.setColumnSpan(rankSpinner, 2);
    }

    /**
     * Adds {@link #firstNameField} and {@link #lastNameField} to the {@linkplain #grid}
     */
    private void addName() {
        firstNameField = new TextField(account.firstName);
        lastNameField = new TextField(account.lastName);

        grid.add(new Label("Name: "), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(lastNameField, 2, 1);
    }

    /**
     * Adds the {@link #emailField} to the {@linkplain #grid}
     */
    private void addEmail() {
        emailField = new TextField(account.email);

        grid.add(new Label("Email: "), 0, 0);
        grid.add(emailField, 1, 0);
        GridPane.setColumnSpan(emailField, 2);
    }

    /**
     * Sets up the layout of the main panel
     */
    private void setupPanel() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
    }
}
