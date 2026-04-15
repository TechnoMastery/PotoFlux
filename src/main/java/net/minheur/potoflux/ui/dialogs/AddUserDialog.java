package net.minheur.potoflux.ui.dialogs;

import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.ui.CheckboxListRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minheur.potoflux.login.ConnectionHandler.account;

public class AddUserDialog extends JDialog {

    private JPanel formPanel;
    private GridBagConstraints gbc;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField firstName;
    private JTextField lastName;
    private JSpinner rankSpinner;

    private JScrollPane permScroll;
    private JList<Perms> permsList;

    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton validateButton;

    private boolean confirmed = false;

    public AddUserDialog(Frame owner) {
        super(owner, "Add user", true); // TODO
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        setupForm();

        addEmail();
        addPassword();
        addFirstName();
        addLastName();
        addRank();
        addPermList();

        add(formPanel, BorderLayout.CENTER);

        addButtons();
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());

        getRootPane().setDefaultButton(validateButton);
    }

    private void addRank() {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Rank :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        rankSpinner = new JSpinner(
                new SpinnerNumberModel(
                        account.rank +1, 0, 100, 1
                )
        );
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(rankSpinner, "#");
        rankSpinner.setEditor(editor);

        formPanel.add(rankSpinner, gbc);
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

    private void addPermList() {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Permissions :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        permsList = new JList<>();
        permsList.setCellRenderer(new CheckboxListRenderer());
        permsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        permsList.setFocusable(false);

        permsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = permsList.locationToIndex(e.getPoint());

                if (index >= 0) {
                    if (permsList.isSelectedIndex(index))
                        permsList.removeSelectionInterval(index, index);
                    else permsList.addSelectionInterval(index, index);
                }
            }
        });
        permsList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {}
        });

        List<Perms> available = new ArrayList<>();
        for (Perms perm : Perms.values()) {
            if (Arrays.asList(account.perms).contains(perm))
                available.add(perm);
        }
        permsList.setListData(available.toArray(Perms[]::new));

        permScroll = new JScrollPane(permsList);
        permScroll.setPreferredSize(new Dimension(200, 100));

        formPanel.add(permScroll, gbc);
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

    public boolean isConfirmed() {
        return confirmed;
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

    public List<Perms> getSelectedPerms() {
        return permsList.getSelectedValuesList();
    }

    public int getRank() {
        return ((int) rankSpinner.getValue());
    }
}
