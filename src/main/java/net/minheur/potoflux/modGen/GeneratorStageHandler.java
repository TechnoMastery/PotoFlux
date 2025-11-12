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
        try {
            getOutputDir();
            getModIdAndPackage();
        } catch (ModGenCanceledException ignored) {}

    }
    public void cancel() {
        throw new ModGenCanceledException();
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
        if (result == JOptionPane.OK_OPTION) {
            String modIdIn = modIdField.getText().trim();
            String packageIn = packageField.getText().trim();

            // --- checks ---
            if (modIdIn.isEmpty() || packageIn.isEmpty()) { // empty check
                JOptionPane.showMessageDialog(owner, "Both fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                getModIdAndPackage(); // rerun this dialog
                return;
            }
            if (!modIdIn.matches("[a-z0-9_]+")) { // mod id regex check
                JOptionPane.showMessageDialog(owner, "Mod ID must contain only lowercase letters, digits, or underscores.", "Error", JOptionPane.ERROR_MESSAGE);
                getModIdAndPackage();
                return;
            }
            if (!packageIn.matches("([a-zA-Z_][a-zA-Z0-9_]*)(\\.[a-zA-Z_][a-zA-Z0-9_]*)*")) { // package regex check
                JOptionPane.showMessageDialog(owner, "Invalid package format.", "Error", JOptionPane.ERROR_MESSAGE);
                getModIdAndPackage();
                return;
            }

            // --- saving modId & package
            this.modId = modIdIn;
            this.modPackage = packageIn + "." + modIdIn;
            JOptionPane.showMessageDialog(owner,
                    "Mod info set !\n" +
                    "Mod ID: " + modId + "\n" +
                    "Package: " + modPackage,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(owner, "Canceled by user.", "Canceled", JOptionPane.WARNING_MESSAGE);
            cancel();
        }
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
            this.outputDir = dir;
            JOptionPane.showMessageDialog(owner,
                    "Folder selected : " + outputDir.getAbsolutePath(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            // --- if user canceled ---
            JOptionPane.showMessageDialog(owner,
                    "No selection made.",
                    "Canceled",
                    JOptionPane.WARNING_MESSAGE);
            cancel();
        }
    }
}
