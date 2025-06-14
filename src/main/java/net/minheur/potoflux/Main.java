package net.minheur.potoflux;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("PotoFlux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showCustomDialog(frame);
    }

    public static void showCustomDialog(JFrame parent) {
        // Crée la boîte de dialogue
        JDialog dialog = new JDialog(parent, "Bienvenue", true);
        dialog.setLayout(new BorderLayout());

        // Texte affiché
        JLabel label = new JLabel("Bienvenue dans Potoflux !");
        label.setHorizontalAlignment(JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel();
        JButton bouton1 = new JButton("Continuer");
        JButton bouton2 = new JButton("Annuler");

        // Action pour "Continuer"
        bouton1.addActionListener(e -> {
            label.setText("Vous avez choisi de continuer !");
        });

        // Action pour "Annuler"
        bouton2.addActionListener(e -> {
            label.setText("Action annulée. Fermeture...");
            // On peut fermer la boîte après un court délai
            Timer timer = new Timer(1000, ev -> System.exit(0));
            timer.setRepeats(false);
            timer.start();
        });

        buttonPanel.add(bouton1);
        buttonPanel.add(bouton2);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Taille automatique et affichage centré
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
