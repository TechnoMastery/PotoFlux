package net.minheur.potoflux.terminal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Terminal {
    private final JTextArea outputArea;
    private final JTextField inputField;

    public Terminal(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // output system
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        // style
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        // adding
        JScrollPane scrollPanel = new JScrollPane(outputArea);

        // area filling

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

        // main adding to panel
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollPanel,
                inputPanel
        );
        splitPane.setResizeWeight(1.0);
        splitPane.setDividerSize(5);
        splitPane.setDividerLocation(0.9);
        panel.add(splitPane, BorderLayout.CENTER);
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }
}
