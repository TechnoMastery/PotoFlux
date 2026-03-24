package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AccountTab extends BaseTab {

    private JLabel titleLabel;
    private JLabel emailLabel;
    private JPanel permsPanel;
    private JButton authButton;

    private boolean isLogged = false; // TODO

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

        permsPanel = new JPanel();
        permsPanel.setLayout(new BoxLayout(permsPanel, BoxLayout.Y_AXIS));
        permsPanel.setBorder(BorderFactory.createCompoundBorder(
                getPermBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        permsPanel.setBackground(new Color(40, 40, 40));
        permsPanel.setOpaque(true);
        permsPanel.setMinimumSize(new Dimension(150, 50));
        permsPanel.setPreferredSize(new Dimension(150, 50));

        authButton = new JButton();

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        permsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private Border getPermBorder() {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Perms", // TODO
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

        PANEL.add(permsPanel);
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
        isLogged = true;
        // TODO
    }
    private void logout() {
        isLogged = false;
        // TODO
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
            titleLabel.setText("Bienvenu, "); // TODO
        else titleLabel.setText("Yo"); // TODO
    }

    private void updateEmail() {
        if (isLogged) {
            emailLabel.setVisible(true);
            emailLabel.setText("email"); // TODO
        }
        else emailLabel.setVisible(false);
    }

    private void updatePerms() {
        permsPanel.removeAll();

        if (isLogged) {
            permsPanel.setVisible(true);

            // TODO: import String[]
            String[] perms = {};

            for (String perm : perms) {

                JLabel permLabel = new JLabel("- " + perm);
                permLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                permsPanel.add(permLabel);
                permsPanel.add(Box.createVerticalStrut(5));

            }
        }
        else permsPanel.setVisible(false);
    }

    private void updateButton() {
        if (isLogged)
            authButton.setText("Se déconnecter"); // TODO
        else authButton.setText("Se connecter"); // TODO
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
