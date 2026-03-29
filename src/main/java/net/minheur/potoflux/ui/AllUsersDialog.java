package net.minheur.potoflux.ui;

import net.minheur.potoflux.login.Account;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class AllUsersDialog extends JDialog {

    private final List<Account> accounts;

    private JScrollPane listScrollPane;
    private JPanel listPanel;
    private Border lineBorder;

    public AllUsersDialog(Frame parent, List<Account> accounts) {
        super(parent, "All users", true);

        this.accounts = accounts;

        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(parent);

        setupPanel();
        setupBorder();

        fillAccount();

        setupScroll();

        setupButton();

    }

    private void setupButton() {
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupScroll() {
        listScrollPane = new JScrollPane(listPanel);
        add(listScrollPane);
    }

    private void fillAccount() {
        for (Account account : accounts)
            listPanel.add(buildLine(account));
    }

    private JPanel buildLine(Account account) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                lineBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel emailLabel = new JLabel(account.email);
        row.add(emailLabel, BorderLayout.WEST);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetails(account));
        row.add(detailsButton, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapper.add(row, BorderLayout.CENTER);

        return wrapper;
    }

    private void showDetails(Account account) {
        JOptionPane.showMessageDialog(this,
                "Email: " + account.email +
                        "\nName: " + account.firstName + " " + account.lastName +
                        "\nRank: " + account.rank +
                        "\nPerms: " + String.join(", ",
                        account.perms != null
                                ? java.util.Arrays.stream(account.perms).map(Enum::name).toList()
                                : java.util.List.of()
                ),
                "Account Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void setupBorder() {
        lineBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }

    private void setupPanel() {
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    }
}
