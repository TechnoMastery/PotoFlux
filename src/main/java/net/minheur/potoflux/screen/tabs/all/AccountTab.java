package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.CreateAccountHandler;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Arrays;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;
import static net.minheur.potoflux.login.ConnectionHandler.*;

/**
 * Tab class for account tab
 */
public class AccountTab extends BaseTab {

    private JLabel titleLabel;
    private JLabel emailLabel;

    private JPanel permsPanel;
    private JList<Perms> permsList;
    private DefaultListModel<Perms> permsModel;
    private JScrollPane permsScroll;
    private JButton executePerm;

    private JButton authButton;
    private JButton createAccountButton;

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

        executePerm = new JButton(Translations.get("potoflux:tabs.account.executePermButton"));

        permsScroll = new JScrollPane(permsList);

        permsPanel = new JPanel();
        permsPanel.setLayout(new BorderLayout());

        permsPanel.add(permsScroll, BorderLayout.CENTER);
        permsPanel.add(executePerm, BorderLayout.SOUTH);

        permsPanel.setBorder(BorderFactory.createCompoundBorder(
                getPermBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        permsPanel.setMaximumSize(new Dimension(permsPanel.getPreferredSize().width, 150));

        authButton = new JButton();
        createAccountButton = new JButton(Translations.get("potoflux:tabs.account.createAccount.button"));

        align();
    }

    private void align() {
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        executePerm.setAlignmentX(Component.CENTER_ALIGNMENT);
        permsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        PANEL.add(permsPanel);
        PANEL.add(Box.createVerticalStrut(15));

        PANEL.add(authButton);
        PANEL.add(Box.createVerticalStrut(5));
        PANEL.add(createAccountButton);
    }

    private void setupActions() {
        authButton.addActionListener(e -> {
            if (isLogged)
                logout();
            else login();
            reload();
        });

        executePerm.addActionListener(e -> {
            if (!isLogged) return;

            Perms selected = permsList.getSelectedValue();
            if (selected.getPermAction() == null) {

                if (selected.getNoRunFallback() == null)
                    showErrorPane(Translations.get("potoflux:tabs.account.error.noPermRun"));
                else showErrorPane(selected.getNoRunFallback());

                return;
            }

            selected.getPermAction().run();
        });

        createAccountButton.addActionListener(e -> {
            if (isLogged) return;
            CreateAccountHandler.create();
        });
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
            permsPanel.setVisible(true);

            for (Perms perm : Perms.values())
                if (Arrays.asList(account.perms).contains(perm))
                    permsModel.addElement(perm);
        }
        else permsPanel.setVisible(false);
    }

    private void updateButton() {
        authButton.setText(getAuthButtonStatus());
        createAccountButton.setVisible(!isLogged);
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
