package net.minheur.potoflux.ui.dialogs;

import net.minheur.potoflux.login.Account;

import javax.swing.*;
import java.awt.*;

public class AccountDetailsDialog extends JDialog {

    private final Account account;

    private JPanel panel;
    private GridBagConstraints gbc;

    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JSpinner rankSpinner;

    private JButton okButton;

    public AccountDetailsDialog(Frame owner, Account account) {
        super(owner, "Account details", true);
        this.account = account;

        setupPanel();
        addEmail();
        addName();
        addRank();

        setupButton();

        setContentPane(panel);
        pack();
        setLocationRelativeTo(owner);
    }

    private void setupButton() {
        okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(okButton, gbc);
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
