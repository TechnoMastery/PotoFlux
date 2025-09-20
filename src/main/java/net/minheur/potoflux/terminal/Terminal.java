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

        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(scrollPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }
}
