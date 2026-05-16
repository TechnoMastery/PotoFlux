package net.minheur.potoflux.ui.dialogs;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.minheur.potoflux.login.*;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.login.response.MdUserInfosResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;
import net.minheur.potoflux.ui.dialogData.ModifiedUserData;
import net.minheur.potoflux.utils.Json;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minheur.potoflux.Functions.formatMessage;
import static net.minheur.potoflux.ui.UiUtils.showErrorPane;
import static net.minheur.potoflux.ui.UiUtils.showMessagePane;

/**
 * When you ask for a user's details, this dialogs pops up
 */
public class AccountDetailsDialog extends Dialog<ModifiedUserData> {

    private final Dialog<?> parent;

    private final Account account;
    private final List<Perms> actualPerms = new ArrayList<>();

    private GridPane grid;

    private TextField emailField;
    private TextField firstNameField;
    private TextField lastNameField;
    private Spinner<Integer> rankSpinner;

    public AccountDetailsDialog(Dialog<?> parent, Account account) {
        setTitle("Account details"); // todo
        this.parent = parent;
        this.account = account;

        setupButtons();
        setupPanel();

        addEmail();
        addName();
        addRank();

        getDialogPane().setContent(grid);

        setupResult();

        fillActualPerms();
        reload();

    }

    private void setupResult() {
        setResultConverter(button -> {

            if (button == UiUtils.confirmButton.get()) {

                ModifiedUserData data = new ModifiedUserData();
                data.email = emailField.getText();
                data.firstName = firstNameField.getText();
                data.lastName = lastNameField.getText();
                data.rank = rankSpinner.getValue();
                // todo: perms
                return data;

            } else return null;

        });
    }

    private void reload() {
        if (actualPerms.contains(Perms.CHANGE_INFORMATIONS)) {
            okButton.setVisible(false);
            confirmButton.setVisible(true);
            cancelButton.setVisible(true);

            getRootPane().setDefaultButton(confirmButton);

            emailField.setEditable(true);
            firstNameField.setEditable(true);
            lastNameField.setEditable(true);
            rankSpinner.setEnabled(true);
        } else {
            okButton.setVisible(true);
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);

            getRootPane().setDefaultButton(okButton);

            emailField.setEditable(false);
            firstNameField.setEditable(false);
            lastNameField.setEditable(false);
            rankSpinner.setEnabled(false);
        }

        changePasswordButton.setVisible(
                actualPerms.contains(Perms.CHANGE_PASSWORD)
        );
        lockButton.setVisible(
            actualPerms.contains(Perms.LOCK)
        );
        lockButton.setText(Translations.get(
                "potoflux:tabs.account." + (account.locked ? "unlock" : "lock") + ".button"
        ));
    }

    private void fillActualPerms() {
        Perms[] permsArray = ConnectionHandler.account.perms;
        actualPerms.addAll(List.of(permsArray));
    }

    private void setupButtons() {
        ButtonType lockButton = new ButtonType(Translations.get("potoflux:tabs.account.lock.button"), ButtonBar.ButtonData.OTHER);
        ButtonType changePasswordButton = new ButtonType(Translations.get("potoflux:tabs.account.mdUserPassword.button"), ButtonBar.ButtonData.OTHER);

        getDialogPane().getButtonTypes().addAll(
                lockButton,
                changePasswordButton,
                UiUtils.cancelButton.get(),
                UiUtils.confirmButton.get(),
                UiUtils.okButton.get()
        );

        confirmButton.addActionListener(e -> { // todo: move to result converter
            String mail = emailField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            int rank = ((int) rankSpinner.getValue());

            boolean isMailModified = !account.email.equals(mail);
            boolean isFirstNameModified = !account.firstName.equals(firstName);
            boolean isLastNameModified = !account.lastName.equals(lastName);
            boolean isRankModified = account.rank != rank;

            // if nothing changed, return
            if (!(isMailModified || isFirstNameModified || isLastNameModified || isRankModified)) {
                closeParent();
                return;
            }

            // confirm
            StringBuilder confirmSb = new StringBuilder(Translations.get("potoflux:tabs.account.mdUserInfos.confirm"));

            if (isMailModified) confirmSb.append("\n").append(Translations.get("common:email"));
            if (isFirstNameModified) confirmSb.append("\n").append(Translations.get("common:firstName"));
            if (isLastNameModified) confirmSb.append("\n").append(Translations.get("common:lastName"));
            if (isRankModified) confirmSb.append("\n").append(Translations.get("common:rank"));

            int check = JOptionPane.showConfirmDialog(
                    this,
                    confirmSb.toString(),
                    Translations.get("common:confirm"),
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (check == JOptionPane.CANCEL_OPTION) {
                dispose();
                return;
            }

            // post request
            String content;
            try {
                content = RequestPoster.mdUserInfos(
                        TokenHandler.get(), // current token
                        account.uuid, // target user's uuid
                        // new data
                        isMailModified ? mail : null,
                        isFirstNameModified ? firstName : null,
                        isLastNameModified ? lastName : null,
                        isRankModified ? rank : 0
                );
            } catch (InvalidTokenException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
                closeParent();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.failed"));
                closeParent();
                return;
            }

            MdUserInfosResponse response = Json.GSON.fromJson(content, MdUserInfosResponse.class);

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
                dispose();
                return;
            }

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
                    UiUtils.cancelButton.get(),
                    UiUtils.confirmButton.get()
            );
            ((Button) passwordAskDialog.getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                    .setCancelButton(true);
            ((Button) passwordAskDialog.getDialogPane().lookupButton(UiUtils.confirmButton.get()))
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
                e.consume();
                return;
            }

            showMessagePane(Translations.get("potoflux:tabs.account." + (newState ? "lock" : "unlock") + ".done"));
            closeParent();

        });

        ((Button) getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                .setCancelButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.okButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.confirmButton.get()))
                .setDefaultButton(true);

    }

    private void closeParent() {
        parent.close();
    }

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
    }

    private void addName() {
        firstNameField = new TextField(account.firstName);
        lastNameField = new TextField(account.lastName);

        grid.add(new Label("Name: "), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(lastNameField, 2, 1);
    }

    private void addEmail() {
        emailField = new TextField(account.email);

        grid.add(new Label("Email: "), 0, 0);
        grid.add(emailField, 1, 0);
    }

    private void setupPanel() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
    }
}
