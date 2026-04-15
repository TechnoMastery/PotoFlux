package net.minheur.potoflux.ui.dialogs;

import javax.swing.*;
import java.awt.*;

public class CreateAccountDialog extends JDialog {

    private JPanel formPanel;
    private GridBagConstraints gbc;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField firstName;
    private JTextField lastName;

    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton validateButton;

    private boolean confirmed = false;

    public CreateAccountDialog(Frame owner) {
        super(owner, "Create Account", true);
        initGui();
    }

    private void initGui() {
        setLayout(new BorderLayout(10, 10));

        setupForm();

        addEmail();
        addPassword();
        addFirstName();
        addLastName();

        add(formPanel, BorderLayout.CENTER);

        addButtons();
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());

        getRootPane().setDefaultButton(validateButton);
    }

    private void addButtons() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cancelButton = new JButton("cancel");
        validateButton = new JButton("Validate");

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        validateButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(validateButton);
    }

    private void addLastName() {
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Last name :"), gbc);

        gbc.gridx = 1;
        lastName = new JTextField(20);
        formPanel.add(lastName, gbc);
    }

    private void addFirstName() {
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("First name :"), gbc);

        gbc.gridx = 1;
        firstName = new JTextField(20);
        formPanel.add(firstName, gbc);
    }

    private void addPassword() {
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Password :"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
    }

    private void addEmail() {
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email :"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
    }

    private void setupForm() {
        formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    public String getEmail() {
        return emailField.getText();
    }
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    public String getFirstName() {
        return firstName.getText();
    }
    public String getLastName() {
        return lastName.getText();
    }
    public boolean isConfirmed() {
        return confirmed;
    }
}
