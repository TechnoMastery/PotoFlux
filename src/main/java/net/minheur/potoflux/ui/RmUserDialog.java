package net.minheur.potoflux.ui;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;

public class RmUserDialog extends JDialog {

    private JPanel formPanel;
    private GridBagConstraints gbc;

    private JTextField rmUserEmail;

    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton validateButton;

    private boolean confirmed = false;

    public RmUserDialog(Frame owner) {
        super(owner, "Remove a user", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        setupForm();

        addEmail();
        add(formPanel, BorderLayout.CENTER);

        addButtons();
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void addButtons() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cancelButton = new JButton(Translations.get("common:cancel"));
        validateButton = new JButton(Translations.get("common:validate"));

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        validateButton.addActionListener(e -> {
            showConfirm();
            dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(validateButton);
    }

    private void showConfirm() {
        int check = JOptionPane.showConfirmDialog(this,
                Functions.formatMessage(
                        Translations.get("potoflux:tabs.account.rmUser.check"),
                        rmUserEmail.getText()
                ),
                Translations.get("common:confirm"),
                JOptionPane.YES_NO_OPTION
        );

        if (check == JOptionPane.YES_OPTION)
            confirmed = true;
    }

    private void addEmail() {
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel(Translations.get("common:emailField")), gbc);

        gbc.gridx = 1;
        rmUserEmail = new JTextField(20);
        formPanel.add(rmUserEmail, gbc);
    }

    private void setupForm() {
        formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    public String getEmail() {
        return rmUserEmail.getText();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
