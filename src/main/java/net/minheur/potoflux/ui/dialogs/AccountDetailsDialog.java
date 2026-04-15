package net.minheur.potoflux.ui.dialogs;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.*;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;

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

        changePasswordButton.setEnabled(
                actualPerms.contains(Perms.CHANGE_PASSWORD)
        );
    }

    private void fillActualPerms() {
        Perms[] permsArray = ConnectionHandler.account.perms;
        actualPerms.addAll(List.of(permsArray));
    }

    private void setupButtons() {
        okButton = new JButton("OK");
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");
        changePasswordButton = new JButton("Password");

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
            StringBuilder sb = new StringBuilder(Translations.get("potoflux:tabs.account.mdUserInfos.confirm"));

            if (isMailModified) sb.append("\n").append(Translations.get("common:email"));
            if (isFirstNameModified) sb.append("\n").append(Translations.get("common:firstName"));
            if (isLastNameModified) sb.append("\n").append(Translations.get("common:lastName"));
            if (isRankModified) sb.append("\n").append(Translations.get("common:rank"));

            int check = JOptionPane.showConfirmDialog(
                    PotoFlux.app.getFrame(),
                    sb.toString(),
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

            // TODO

            dispose();

        });

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

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
