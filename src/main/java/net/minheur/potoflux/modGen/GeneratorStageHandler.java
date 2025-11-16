package net.minheur.potoflux.modGen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GeneratorStageHandler {
    // --- setup vars ---
    private final JPanel owner;
    // --- hard-needed vars ---
    private File outputDir;
    private String modId;
    private String modPackage;
    // --- actual files
    private Set<MainModules> selectedModules = new HashSet<>();

    public GeneratorStageHandler(JPanel owner) {
        this.owner = owner;
    }
    
    public static GeneratorStageHandler createGenerator(JPanel owner) {
        return new GeneratorStageHandler(owner);
    }

    public void cancel() {
        throw new ModGenCanceledException();
    }
    public void run() {
        try {
            getOutputDir();
            getModIdAndPackage();
            getMainOptionalModules();
            if (askDatagen()) {
                getDatagenModules();
            }
        } catch (ModGenCanceledException ignored) {}
    }

    private void getDatagenModules() {
        // --- panel setup ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- def checkboxes ---
        JCheckBox cbLoot = new JCheckBox("Loot table provider");
        JCheckBox cbBlockLoot = new JCheckBox("   ↳ Block loot");
        JCheckBox cbAdvancement = new JCheckBox("Advancement provider");
        JCheckBox cbBlockStates = new JCheckBox("Block states provider");
        JCheckBox cbBlockTags = new JCheckBox("Block tags provider");
        JCheckBox cbLootModifiers = new JCheckBox("Loot modifiers");
        JCheckBox cbItemModel = new JCheckBox("Item Models provider");
        JCheckBox cbItemTag = new JCheckBox("Item tags provider");
        JCheckBox cbLang = new JCheckBox("Lang provider");
        JCheckBox cbPoiTypeTag = new JCheckBox("Poi type tags provider");
        JCheckBox cbRecipe = new JCheckBox("Recipe provider");
    }

    private boolean askDatagen() {
        int result = JOptionPane.showConfirmDialog(owner,
                new JLabel("Do you want a datagen in your build ?"), "Datagen integration",
                JOptionPane.YES_NO_OPTION);

        return result == JOptionPane.YES_OPTION;
    }

    private void getMainOptionalModules() {
        // --- panel setup ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- def checkboxes ---
        JCheckBox cbAdvancements = new JCheckBox("ModAdvancements");
        JCheckBox cbBlocks = new JCheckBox("ModBlocks");
        JCheckBox cbBlockEntities = new JCheckBox("   ↳ ModBlockEntities");
        JCheckBox cbCommands = new JCheckBox("ModCommands");
        JCheckBox cbEffects = new JCheckBox("ModEffects");
        JCheckBox cbEntities = new JCheckBox("ModEntities");
        JCheckBox cbVillagers = new JCheckBox("   ↳ ModVillagers");
        JCheckBox cbItems = new JCheckBox("ModItems");
        JCheckBox cbToolTiers = new JCheckBox("   ↳ ModToolTiers");
        JCheckBox cbFoods = new JCheckBox("   ↳ ModFoods");
        JCheckBox cbArmorMaterials = new JCheckBox("   ↳ ModArmorMaterials");
        JCheckBox cbCreativeTabs = new JCheckBox("ModCreativeModTabs");
        JCheckBox cbRecipes = new JCheckBox("ModRecipes");
        JCheckBox cbMenuTypes = new JCheckBox("ModMenuTypes");
        JCheckBox cbSounds = new JCheckBox("ModSounds");
        JCheckBox cbTags = new JCheckBox("ModTags");
        JCheckBox cbDamageTypes = new JCheckBox("ModDamageTypes");
        JCheckBox cbWoodTypes = new JCheckBox("ModWoodTypes");

        // --- first disabled ---
        cbToolTiers.setEnabled(false);
        cbFoods.setEnabled(false);
        cbArmorMaterials.setEnabled(false);
        cbBlockEntities.setEnabled(false);
        cbVillagers.setEnabled(false);

        // --- logic enabled ---
        cbBlocks.addActionListener(e -> { // block
            boolean enabled = cbBlocks.isSelected();
            cbBlockEntities.setEnabled(enabled);
            if (!enabled) cbBlockEntities.setSelected(false);
        });
        cbItems.addActionListener(e -> { // item
            boolean enabled = cbItems.isSelected();
            cbToolTiers.setEnabled(enabled);
            cbFoods.setEnabled(enabled);
            cbArmorMaterials.setEnabled(enabled);
            if (!enabled) {
                cbToolTiers.setSelected(false);
                cbFoods.setSelected(false);
                cbArmorMaterials.setSelected(false);
            }
        });
        cbEntities.addActionListener(e -> { // entity
            boolean enabled = cbEntities.isSelected();
            cbVillagers.setEnabled(enabled);
            if (!enabled) cbVillagers.setSelected(false);
        });

        // --- adding to panel ---
        panel.add(cbAdvancements);
        panel.add(cbBlocks);
        panel.add(cbBlockEntities);
        panel.add(cbCommands);
        panel.add(cbEffects);
        panel.add(cbEntities);
        panel.add(cbVillagers);
        panel.add(cbItems);
        panel.add(cbToolTiers);
        panel.add(cbFoods);
        panel.add(cbArmorMaterials);
        panel.add(cbCreativeTabs);
        panel.add(cbRecipes);
        panel.add(cbMenuTypes);
        panel.add(cbSounds);
        panel.add(cbTags);
        panel.add(cbDamageTypes);
        panel.add(cbWoodTypes);

        // --- scroll pane ---
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(350, 400));

        // --- return logic ---
        int result = JOptionPane.showConfirmDialog(owner, scroll,
                "Select optional classes to generate", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            this.selectedModules.clear();

            // --- get selected modules ---
            if (cbAdvancements.isSelected()) selectedModules.add(MainModules.ADVANCEMENT);
            if (cbBlocks.isSelected()) selectedModules.add(MainModules.BLOCKS);
            if (cbBlockEntities.isSelected()) selectedModules.add(MainModules.BLOCK_ENTITIES);
            if (cbCommands.isSelected()) selectedModules.add(MainModules.COMMANDS);
            if (cbEffects.isSelected()) selectedModules.add(MainModules.EFFECTS);
            if (cbEntities.isSelected()) selectedModules.add(MainModules.ENTITIES);
            if (cbItems.isSelected()) selectedModules.add(MainModules.ITEMS);
            if (cbToolTiers.isSelected()) selectedModules.add(MainModules.TOOL_TIER);
            if (cbFoods.isSelected()) selectedModules.add(MainModules.FOODS);
            if (cbArmorMaterials.isSelected()) selectedModules.add(MainModules.ARMOR_MATERIALS);
            if (cbCreativeTabs.isSelected()) selectedModules.add(MainModules.CREATIVE_TABS);
            if (cbRecipes.isSelected()) selectedModules.add(MainModules.RECIPES);
            if (cbMenuTypes.isSelected()) selectedModules.add(MainModules.MENUS);
            if (cbSounds.isSelected()) selectedModules.add(MainModules.SOUNDS);
            if (cbTags.isSelected()) selectedModules.add(MainModules.TAGS);
            if (cbDamageTypes.isSelected()) selectedModules.add(MainModules.DAMAGES);
            if (cbWoodTypes.isSelected()) selectedModules.add(MainModules.WOOD);
            if (cbVillagers.isSelected()) selectedModules.add(MainModules.VILLAGERS);

            JOptionPane.showMessageDialog(owner,
                    "Modules selected:\n" + String.join(", ", selectedModules.toString()),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(owner, "Canceled by user.", "Canceled", JOptionPane.WARNING_MESSAGE);
            cancel();
        }

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
