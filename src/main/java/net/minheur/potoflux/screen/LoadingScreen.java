package net.minheur.potoflux.screen;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JFrame {
    private JLabel label;
    private JLabel stage;

    public LoadingScreen() {
        setTitle("Loading PotoFlux...");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        label = new JLabel("PotoFlux is starting...");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        stage = new JLabel();
        stage.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(stage);

        panel.add(Box.createVerticalGlue());

        add(panel);
    }

    public void updateStage(String text) {
        stage.setText(text);
    }

    public void close() {
        dispose();
    }
}
