package net.minheur.PotoFlux;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("PotoFlux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Functions.setupDialog(frame);
        Timer timer1 = new Timer(500, ev -> {
            JOptionPane.showMessageDialog(frame, "Bienvenu dans PotoFlux !", "PotoFlux", JOptionPane.INFORMATION_MESSAGE);
        });

        timer1.setRepeats(false);
        timer1.start();
    }
}
