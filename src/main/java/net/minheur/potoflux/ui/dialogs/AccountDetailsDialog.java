package net.minheur.potoflux.ui.dialogs;

import net.minheur.potoflux.login.*;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.login.response.MdUserInfosResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.Json;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.Functions.formatMessage;
import static net.minheur.potoflux.ui.UiUtils.showErrorPane;
import static net.minheur.potoflux.ui.UiUtils.showMessagePane;

public class AccountDetailsDialog extends JDialog {

    private final Account account;
    private final List<Perms> actualPerms = new ArrayList<>();

    private JPanel panel;
    private GridBagConstraints gbc;

    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JSpinner rankSpinner;

    private JPanel buttonsPanel;
    private JButton okButton;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton changePasswordButton;
    private JButton lockButton;

    public AccountDetailsDialog(Frame owner, Account account) {
        super(owner, "Account details", true);
        this.account = account;

        setupPanel();
        addEmail();
        addName();
        addRank();

        setupButtons();

        setContentPane(panel);
        pack();
        setLocationRelativeTo(owner);

        fillActualPerms();
        reload();

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
        okButton = new JButton(Translations.get("common:ok"));
        confirmButton = new JButton(Translations.get("common:confirm"));
        cancelButton = new JButton(Translations.get("common:cancel"));
        changePasswordButton = new JButton(Translations.get("potoflux:tabs.account.mdUserPassword.button"));
        lockButton = new JButton(Translations.get("potoflux:tabs.account.lock.button"));

        okButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> dispose());

        confirmButton.addActionListener(e -> {
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
                dispose();
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
                dispose();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.failed"));
                dispose();
                return;
            }

            MdUserInfosResponse response = Json.GSON.fromJson(content, MdUserInfosResponse.class);

            if (!response.success) {
                showErrorPane(
                        switch (response.error) {
                            case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                            case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                            case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                            case "new_email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                            case "nothing_changed" -> Translations.get("potoflux:tabs.account.mdUserInfos.noMods");
                            case "insufficient_rank" -> Translations.get("potoflux:tabs.account.error.insufficientRank");
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

            dispose();

        });

        changePasswordButton.addActionListener(e -> {
            JTextField newPasswordField = new JTextField();

            int check = JOptionPane.showConfirmDialog(
                    this,
                    newPasswordField,
                    Translations.get("potoflux:tabs.account.mdUserPassword.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (check == JOptionPane.CANCEL_OPTION) {
                dispose();
                return;
            }

            String newPassword = newPasswordField.getText();

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
                dispose();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.failed"));
                dispose();
                return;
            }

            BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);

            if (!response.success) {
                showErrorPane(
                        switch (response.error) {
                            case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                            case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                            case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                            case "insufficient_rank" -> Translations.get("potoflux:tabs.account.error.insufficientRank");
                            default -> response.error;
                        }
                );
                return;
            }

            showMessagePane(formatMessage(
                    Translations.get("potoflux:tabs.account.mdUserPassword.done"),
                    account.email, newPassword
            ));

        });

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        buttonsPanel.add(lockButton);
        buttonsPanel.add(changePasswordButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(okButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        panel.add(buttonsPanel, gbc);
    }

    private void addRank() {
        rankSpinner = new JSpinner(new SpinnerNumberModel(account.rank, 0, 100, 1));
        addRow("Rank", rankSpinner, gbc);
    }

    private void addRow(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);

        gbc.gridy++;
    }

    private void addName() {
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 5, 0));

        firstNameField = new JTextField(account.firstName);
        lastNameField = new JTextField(account.lastName);

        namePanel.add(firstNameField);
        namePanel.add(lastNameField);

        addRow("Name", namePanel, gbc);
    }

    private void addEmail() {
        emailField = new JTextField(account.email);
        addRow("Email", emailField, gbc);
    }

    private void setupPanel() {
        panel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
    }
}
