package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AccountTab extends BaseTab {

    private JLabel titleLabel;
    private JLabel emailLabel;

    private JList<String> permsList;
    private DefaultListModel<String> permsModel;
    private JScrollPane permsScroll;

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

        // PANEL.add(permsPanel);
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
        permsModel.clear();

        if (isLogged) {
            permsScroll.setVisible(true);

            // TODO: import String[]
            String[] perms = {"read", "mael", "manger"};

            for (String perm : perms)
                permsModel.addElement(perm);
        }
        else permsScroll.setVisible(false);
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
