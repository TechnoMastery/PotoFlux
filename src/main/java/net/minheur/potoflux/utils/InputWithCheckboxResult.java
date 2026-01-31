package net.minheur.potoflux.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Utility class used in the TodoMod.<br>
 * Old, but still needs to be moved to the mod
 */
@Deprecated(since = "6.4", forRemoval = true)
public class InputWithCheckboxResult {
    public final String text;
    public final boolean pinned;

    public InputWithCheckboxResult(String text, boolean pinned) {
        this.text = text;
        this.pinned = pinned;
    }

    public static InputWithCheckboxResult showInputWithCheckboxDialog(Component parent, String title, String message, String checkboxLabel) {
        JTextField textField = new JTextField(20);
        JCheckBox checkBox = new JCheckBox(checkboxLabel);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel msg = new JLabel(message);
        msg.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(msg);
        panel.add(Box.createVerticalStrut(8));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(checkBox);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return new InputWithCheckboxResult(textField.getText().trim(), checkBox.isSelected());
        }
        return null; // Cancel
    }
}
