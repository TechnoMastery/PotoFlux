package net.minheur.potoflux.ui.dialogs;

import net.minheur.potoflux.login.Account;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.perms.Perms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        if (!actualPerms.contains(Perms.CHANGE_INFORMATIONS)) {
            okButton.setVisible(true);
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);

            getRootPane().setDefaultButton(okButton);

            emailField.setEditable(false);
            firstNameField.setEditable(false);
            lastNameField.setEditable(false);
            rankSpinner.setEnabled(false);
        }
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
