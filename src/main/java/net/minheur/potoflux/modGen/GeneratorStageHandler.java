package net.minheur.potoflux.modGen;

import javax.swing.*;
import java.io.File;

public class GeneratorStageHandler {
    // --- setup vars ---
    private final JPanel owner;
    // --- hard-needed vars ---
    private File outputDir;
    private String modId;
    private String modPackage;

    public GeneratorStageHandler(JPanel owner) {
        this.owner = owner;
    }
    
    public static GeneratorStageHandler createGenerator(JPanel owner) {
        return new GeneratorStageHandler(owner);
    }

    public void run() {
        getOutputDir();
        getModIdAndPackage();
    }

    private void getModIdAndPackage() {
        // --- panel for both inputs ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- input fields ---
        JTextField modIdField = new JTextField();
        JTextField packageField = new JTextField();

        panel.add(new JLabel("Mod ID (ex: mymodid) :"));
        panel.add(modIdField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Base package (ex: net.minheur) :"));
        panel.add(packageField);

        // --- actual dialog ---
        int result = JOptionPane.showConfirmDialog(owner,
                panel, "Mod info",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        // --- NEXT ---
    }

    private void getOutputDir() {
        // --- setup ---
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select or create the folder where your mod will go.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(owner);
        if (result == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();

            // --- create if not existing ---
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    JOptionPane.showMessageDialog(owner,
                            "Impossible to create selected dir.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // --- empty check ---
            if (dir.listFiles() != null && dir.listFiles().length > 0) {
                JOptionPane.showMessageDialog(owner,
                        "The folder needs to be empty to create a new mod!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- path validation logger ---
            JOptionPane.showMessageDialog(owner,
                    "Folder selected : " + dir.getAbsolutePath(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            this.outputDir = dir;

        } else {
            // --- if user canceled ---
            JOptionPane.showMessageDialog(owner,
                    "No selection made.",
                    "Canceled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
