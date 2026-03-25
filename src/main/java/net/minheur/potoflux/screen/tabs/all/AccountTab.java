package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.*;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.login.ConnectionHandler.*;

public class AccountTab extends BaseTab {

    private JLabel titleLabel;
    private JLabel emailLabel;

    private JList<String> permsList;
    private DefaultListModel<String> permsModel;
    private JScrollPane permsScroll;

    private JButton authButton;

    @Override
    protected void setPanel() {
        initComponents();
        setupLayout();
        setupActions();
        reload();
    }

    private void initComponents() {
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 20));

        emailLabel = new JLabel();
        emailLabel.setFont(new Font("Consolas", Font.PLAIN, 13));

        permsModel = new DefaultListModel<>();
        permsList = new JList<>(permsModel);

        permsList.setFont(new Font("Consolas", Font.PLAIN, 13));
        permsList.setVisibleRowCount(5);

        permsScroll = new JScrollPane(permsList);
        permsScroll.setBorder(BorderFactory.createCompoundBorder(
                getPermBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        permsScroll.setPreferredSize(new Dimension(180, 100));
        permsScroll.setMaximumSize(new Dimension(180, 120));

        authButton = new JButton();

        align();
    }

    private void align() {
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        permsScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        authButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private Border getPermBorder() {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                Translations.get("common:perms"),
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segeo UI", Font.BOLD, 12),
                Color.LIGHT_GRAY
        );
    }

    private void setupLayout() {
        PANEL.setLayout(new BoxLayout(PANEL, BoxLayout.Y_AXIS));

        PANEL.add(Box.createVerticalStrut(30));
        PANEL.add(titleLabel);
        PANEL.add(Box.createVerticalStrut(10));

        PANEL.add(emailLabel);
        PANEL.add(Box.createVerticalStrut(10));

        PANEL.add(permsScroll);
        PANEL.add(Box.createVerticalStrut(15));

        PANEL.add(authButton);
    }

    private void setupActions() {
        authButton.addActionListener(e -> {
            if (isLogged)
                logout();
            else login();
            reload();
        });
    }

    private void login() {
        PtfLogger.info("Logging in...", LogCategories.ACCOUNT);

        JDialog dialog = new JDialog(PotoFlux.app.getFrame(), Translations.get("common:connection"), true);
        dialog.setSize(450, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        // FIELDS

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel emailLabel = new JLabel(Translations.get("common:emailField"));
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel(Translations.get("common:passwordField"));
        JPasswordField passwordField = new JPasswordField();

        fieldsPanel.add(emailLabel);
        fieldsPanel.add(emailField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        // BUTTONS

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton(Translations.get("common:cancel"));
        JButton loginButton = new JButton(Translations.get("common:connection"));

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(loginButton);

        // ACTIONS

        cancelButton.addActionListener(e -> {
            dialog.dispose();
            PtfLogger.info("Connection canceled.", LogCategories.ACCOUNT);
        });

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim().toLowerCase();
            String password = new String(passwordField.getPassword()).trim();

            logout();
            logWith(email, password);

            dialog.dispose();
        });

        dialog.getRootPane().setDefaultButton(loginButton);

        dialog.add(fieldsPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void reload() {
        updateTitle();
        updateEmail();
        updatePerms();
        updateButton();

        PANEL.revalidate();
        PANEL.repaint();
    }

    private void updateTitle() {
        if (isLogged)
            titleLabel.setText(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.title.connected"),
                    account.firstName, account.lastName
            ));
        else titleLabel.setText(Translations.get("potoflux:tabs.account.title"));
    }

    private void updateEmail() {
        if (isLogged) {
            emailLabel.setVisible(true);
            emailLabel.setText(account.email);
        }
        else emailLabel.setVisible(false);
    }

    private void updatePerms() {
        permsModel.clear();

        if (isLogged) {
            permsScroll.setVisible(true);

            for (Perms perm : Perms.values())
                permsModel.addElement(perm.getName());
        }
        else permsScroll.setVisible(false);
    }

    private void updateButton() {
        if (isLogged)
            authButton.setText(Translations.get("common:disconnect"));
        else authButton.setText(Translations.get("common:connect"));
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
