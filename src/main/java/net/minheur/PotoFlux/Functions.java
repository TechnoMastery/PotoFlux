package net.minheur.PotoFlux;

import javax.swing.*;
import java.awt.*;

public class Functions {
    // to call in main to run initial PotoFlux code
    public static void openWindow() {
        JFrame frame = new JFrame("PotoFlux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Functions.setupDialog(frame);
        Timer timer1 = new Timer(500, ev -> {
            JOptionPane.showMessageDialog(frame, "Bienvenu dans PotoFlux !", "PotoFlux", JOptionPane.INFORMATION_MESSAGE);
        });

        timer1.setRepeats(false);
        timer1.start();
    }

    public static void setupDialog(JFrame parent) {
        // Crée la boîte de dialogue
        JDialog dialog = new JDialog(parent, "Bienvenue", false);
        dialog.setLayout(new BorderLayout());

        // Texte affiché
        JLabel label = new JLabel("Bienvenue dans PotoFlux !");
        label.setHorizontalAlignment(JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        // setup panneau pour les boutons
        JPanel buttonPanel = getJPanel(parent, label, dialog);

        // ajout du panel au dialogue
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Taille automatique et affichage centré
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static JPanel getJPanel(JFrame parent, JLabel label, JDialog dialog) {
        JPanel buttonPanel = new JPanel();
        // setup boutons
        JButton buttonL = new JButton("Start");
        JButton buttonR = new JButton("Close");

        // Action pour "Left"
        buttonL.addActionListener(e -> {
            label.setText("Starting game...");
            buttonL.setEnabled(false);
            buttonR.setEnabled(false);

            parent.setTitle("PotoFlux");
            parent.setSize(500, 400);
            parent.setVisible(true);
            parent.setLocationRelativeTo(dialog);

            dialog.setVisible(false);
        });

        // Action pour "Right"
        buttonR.addActionListener(e -> {
            label.setText("Fermeture...");
            // closing
            exit(500, 0);
        });

        // ajout des boutons au panel
        buttonPanel.add(buttonL);
        buttonPanel.add(buttonR);
        return buttonPanel;
    }

    public static void exit(int delay, int status) {
        Timer exitDelay = new Timer(delay, ev -> System.exit(status));
        exitDelay.setRepeats(false);
        exitDelay.start();
    }
}
