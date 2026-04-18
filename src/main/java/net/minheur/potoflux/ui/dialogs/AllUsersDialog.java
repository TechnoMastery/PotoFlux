package net.minheur.potoflux.ui.dialogs;

import net.minheur.potoflux.login.Account;
import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

/**
 * Contains the list of all users, each with a button allowing the user to see their details.
 */
public class AllUsersDialog extends JDialog {

    private final List<Account> accounts;

    private JScrollPane listScrollPane;
    private JPanel listPanel;
    private Border lineBorder;

    public AllUsersDialog(Frame parent, List<Account> accounts) {
        super(parent, "All users", true);

        this.accounts = accounts;

        setLayout(new BorderLayout());

        setupPanel();
        setupBorder();
        fillAccounts();
        setupScroll();
        setupButton();

        pack();
        setMaximumSize(new Dimension(400, 500));

        setLocationRelativeTo(parent);
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

    private void fillAccounts() {
        for (Account account : accounts) {
            JPanel row = mkRow(account);

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            wrapper.add(row, BorderLayout.CENTER);

            listPanel.add(wrapper);
        }
    }

    private @NotNull JPanel mkRow(Account account) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        row.setBorder(BorderFactory.createCompoundBorder(
                lineBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel emailLabel = new JLabel(account.email);

        JButton detailsButton = new JButton(Translations.get("common:details"));
        detailsButton.addActionListener(e -> showDetails(account));

        row.add(emailLabel);
        row.add(Box.createHorizontalGlue());
        row.add(Box.createHorizontalStrut(15));
        row.add(detailsButton);

        return row;
    }

    private void showDetails(Account account) {

        AccountDetailsDialog dialog = new AccountDetailsDialog(this, account);
        dialog.setVisible(true);

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
