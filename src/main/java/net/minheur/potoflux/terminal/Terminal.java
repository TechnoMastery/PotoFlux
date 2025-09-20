package net.minheur.potoflux.terminal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Terminal {
    private final JTextArea outputArea;
    private final JTextField inputField;

    public Terminal() {
        JFrame frame = new JFrame("PotoFLux terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // output system
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        // style
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        // adding
        JScrollPane scrollPanel = new JScrollPane(outputArea);

        // input system
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                CommandProcessor.processCommand(command);
                inputField.setText("");
            }
        });

        // input panel prefix
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel prompt = new JLabel("  > " );
        prompt.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputField.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputPanel.add(prompt, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);

        // toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // create settings button
        JButton settingsButton = new JButton("⚙");
        settingsButton.addActionListener(e -> {
            // create window -basic-
            JDialog dialog = new JDialog(frame, "Settings", true);
            dialog.setSize(300, 150);
            dialog.setLayout(new BorderLayout());
            dialog.setLocationRelativeTo(frame);
            // font size -setting-
            JTextField fontSizeParameterField = new JTextField();
            fontSizeParameterField.setText(String.valueOf(outputArea.getFont().getSize()));
            // apply button -base-
            JButton applyButton = new JButton("Apply");
            applyButton.addActionListener(ev -> {
                try {
                    int newSize = Integer.parseInt(fontSizeParameterField.getText().trim());
                    // check font not to small
                    if (newSize > 5) {
                        Font newFont = outputArea.getFont().deriveFont(((float) newSize));
                        outputArea.setFont(newFont);
                        inputField.setFont(newFont);
                    }
                    // close window
                    dialog.dispose();
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(dialog, "Please input a valid number !");
                }
            });
            // settings pane
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.add(new JLabel("Font size :"), BorderLayout.NORTH);
            panel.add(fontSizeParameterField, BorderLayout.CENTER);
            panel.add(applyButton, BorderLayout.SOUTH);

            dialog.add(panel, BorderLayout.CENTER);
            dialog.setVisible(true);
        });

        // adding buttons
        toolBar.add(settingsButton);

        // main adding to frame
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(scrollPanel, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }
}
