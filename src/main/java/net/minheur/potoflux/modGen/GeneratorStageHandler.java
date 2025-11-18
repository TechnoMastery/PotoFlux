package net.minheur.potoflux.modGen;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.modGen.data.DatagenModules;
import net.minheur.potoflux.modGen.data.MainModules;
import net.minheur.potoflux.modGen.data.ModData;
import net.minheur.potoflux.modGen.data.ModDependency;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneratorStageHandler {
    // --- setup vars ---
    private final JPanel owner;
    // --- hard-needed vars ---
    private File outputDir;
    private String modId;
    private String modPackage;
    // --- actual data ---
    private final Set<MainModules> selectedModules = new HashSet<>();
    private final Set<DatagenModules> selectedDatagenModules = new HashSet<>();
    private final List<ModDependency> modDependencies = new ArrayList<>();
    private final ModData modData = new ModData();

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
            getModData();
            getMainOptionalModules();
            if (askDatagen()) getDatagenModules();
            askDependencies();
        } catch (ModGenCanceledException ignored) {}
    }

    private void askDependencies() {
        // --- main dialog ---
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(owner),
                "Mod dependencies", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(owner);
        dialog.setLayout(new BorderLayout());

        // --- list ---
        DefaultListModel<ModDependency> listModel = new DefaultListModel<>();

        JList<ModDependency> depList = new JList<>(listModel);
        depList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        dialog.add(new JScrollPane(depList), BorderLayout.CENTER);

        // --- buttons ---
        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Delete");
        JButton okBtn = new JButton("OK");

        buttons.add(addBtn);
        buttons.add(removeBtn);
        buttons.add(okBtn);

        dialog.add(buttons, BorderLayout.SOUTH);

        // --- action : add ---
        addBtn.addActionListener(e -> {
            ModDependency dep = showAddDependencyDialog();
            if (dep != null) listModel.addElement(dep);
        });

        // --- action : delete ---
        removeBtn.addActionListener(e -> {
            int index = depList.getSelectedIndex();
            if (index >= 0) listModel.remove(index);
        });

        // --- action : OK ---
        okBtn.addActionListener(e -> {
            modDependencies.clear();
            for (int i = 0; i < listModel.size(); i++) modDependencies.add(listModel.get(i));
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private ModDependency showAddDependencyDialog() {
        // --- panel ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- mod id ---
        JPanel p1 = new JPanel(new java.awt.GridLayout(1, 2, 5, 5));
        p1.add(new JLabel("modId:"));
        JTextField modIdField = new JTextField(15);
        p1.add(modIdField);
        panel.add(p1);
        // --- version range ---
        JPanel p2 = new JPanel(new java.awt.GridLayout(1, 2, 5, 5));
        p2.add(new JLabel("Version range:"));
        JTextField versionField = new JTextField("[1.0,)", 15);
        p2.add(versionField);
        panel.add(p2);
        // --- ordering
        JPanel p3 = new JPanel(new java.awt.GridLayout(1, 2, 5, 5));
        p3.add(new JLabel("Ordering:"));
        JComboBox<String> orderingBox = new JComboBox<>(new String[]{"NONE", "BEFORE", "AFTER"});
        p3.add(orderingBox);
        panel.add(p3);
        // --- side ---
        JPanel p4 = new JPanel(new java.awt.GridLayout(1, 2, 5, 5));
        p4.add(new JLabel("Side:"));
        JComboBox<String> sideBox = new JComboBox<>(new String[]{"BOTH", "CLIENT", "SERVER"});
        p4.add(sideBox);
        panel.add(p4);
        // --- mandatory ---
        JCheckBox mandatoryBox = new JCheckBox("Mandatory");
        mandatoryBox.setSelected(true);
        panel.add(mandatoryBox);

        // --- result logic ---
        int result = JOptionPane.showConfirmDialog(
                owner, panel,
                "Add a dependencies",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) return null;
        if (modIdField.getText().isBlank()) return null;

        ModDependency dep = new ModDependency(
                modIdField.getText(),
                mandatoryBox.isSelected(),
                versionField.getText(),
                ((String) orderingBox.getSelectedItem()),
                ((String) sideBox.getSelectedItem())
        );

        return dep;
    }

    private void getModData() {
        // --- panel setup ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- actual field ---
        JTextField nameField = new JTextField();
        JTextField licenceField = new JTextField("MIT"); // default
        JTextField versionField = new JTextField("1.0.0"); // default
        JTextField authorField = new JTextField(); JCheckBox cbAuthor = new JCheckBox("Author:");
        JTextField creditsField = new JTextField(); JCheckBox cbCredits = new JCheckBox("Credits:");
        JTextArea descArea = new JTextArea(4, 20);
        JTextField issueField = new JTextField(); JCheckBox cbIssue = new JCheckBox("Issues URL:");
        JTextField updateJSONField = new JTextField(); JCheckBox cbUpdateURL = new JCheckBox("Update JSON URL:");
        JTextField displayURLField = new JTextField(); JCheckBox cbDisplayURL = new JCheckBox("Display URL");

        // --- helper : desc ---
        panel.add(new JLabel("Mod Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Mod Licence:"));
        panel.add(licenceField);
        panel.add(new JLabel("Initial Version:"));
        panel.add(versionField);
        panel.add(cbAuthor);
        panel.add(authorField);
        panel.add(cbCredits);
        panel.add(creditsField);
        panel.add(new JLabel("Description: (use symbol $ to add \"\\n\""));
        JScrollPane descScroll = new JScrollPane(descArea);
        panel.add(descScroll);
        panel.add(cbIssue);
        panel.add(issueField);
        panel.add(cbUpdateURL);
        panel.add(updateJSONField);
        panel.add(cbDisplayURL);
        panel.add(displayURLField);

        // --- resize panel ---
        panel.setPreferredSize(new Dimension(350, 500));

        // --- dialog ---
        int result = JOptionPane.showConfirmDialog(
                owner, panel,
                "Enter your mod's general data",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // --- running logic ---
        if (result == JOptionPane.OK_OPTION) {
            modData.setModName(nameField.getText().trim());
            modData.setModLicence(licenceField.getText().trim());
            modData.setModInitialVersion(versionField.getText().trim());
            if (cbAuthor.isSelected()) modData.setModAuthor(authorField.getText().trim());
            if (cbCredits.isSelected()) modData.setModCredits(creditsField.getText().trim());
            if (cbIssue.isSelected()) modData.setIssueURL(issueField.getText().trim());
            if (cbUpdateURL.isSelected()) modData.setUpdateJSONURL(updateJSONField.getText().trim());
            if (cbDisplayURL.isSelected()) modData.setDisplayURL(displayURLField.getText().trim());

            String newDesc = descArea.getText().replaceAll("[$]", "\n");
            modData.setModDesc(newDesc.trim());

            JOptionPane.showMessageDialog(owner,
                    "Mod data saved!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(owner,
                    "Canceled by user.",
                    "Canceled",
                    JOptionPane.WARNING_MESSAGE);
            cancel();
        }
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

        // --- first disabled ---
        cbBlockLoot.setEnabled(false);

        // --- logic gettable from modules ---
        if (!selectedModules.contains(MainModules.ADVANCEMENT)) cbAdvancement.setEnabled(false);
        final boolean cbBlockLootActivated;
        if (!selectedModules.contains(MainModules.BLOCKS)) {
            cbBlockLootActivated = false;
            cbBlockStates.setEnabled(false);
            cbBlockTags.setEnabled(false);
        } else {
            cbBlockLootActivated = true;
        }
        if (!selectedModules.contains(MainModules.ITEMS)) {
            cbItemModel.setEnabled(false);
            cbItemTag.setEnabled(false);
        }

        // --- logic enabled ---
        cbLoot.addActionListener(e -> {
            boolean enabled = cbLoot.isSelected();
            cbBlockLoot.setEnabled(enabled && cbBlockLootActivated);
            if (!enabled) cbBlockLoot.setSelected(false);
        });

        // --- add to panel ---
        panel.add(cbLoot);
        panel.add(cbBlockLoot);
        panel.add(cbAdvancement);
        panel.add(cbBlockStates);
        panel.add(cbBlockTags);
        panel.add(cbLootModifiers);
        panel.add(cbItemModel);
        panel.add(cbItemTag);
        panel.add(cbLang);
        panel.add(cbPoiTypeTag);
        panel.add(cbRecipe);

        // --- scroll pane ---
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(350, 400));

        // --- return logic ---
        int result = JOptionPane.showConfirmDialog(owner, scroll,
                "Select optional classes to generate", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            if (cbLoot.isSelected()) selectedDatagenModules.add(DatagenModules.LOOT);
            if (cbBlockLoot.isSelected()) selectedDatagenModules.add(DatagenModules.BLOCK_LOOT);
            if (cbAdvancement.isSelected()) selectedDatagenModules.add(DatagenModules.ADVANCEMENT);
            if (cbBlockStates.isSelected()) selectedDatagenModules.add(DatagenModules.BLOCK_STATE);
            if (cbBlockTags.isSelected()) selectedDatagenModules.add(DatagenModules.BLOCK_TAG);
            if (cbLootModifiers.isSelected()) selectedDatagenModules.add(DatagenModules.LOOT_MODIFIERS);
            if (cbItemModel.isSelected()) selectedDatagenModules.add(DatagenModules.ITEM_MODEL);
            if (cbItemTag.isSelected()) selectedDatagenModules.add(DatagenModules.ITEM_TAG);
            if (cbLang.isSelected()) selectedDatagenModules.add(DatagenModules.LANG);
            if (cbPoiTypeTag.isSelected()) selectedDatagenModules.add(DatagenModules.POI_TYPE_TAG);
            if (cbRecipe.isSelected()) selectedDatagenModules.add(DatagenModules.RECIPE);

            JOptionPane.showMessageDialog(owner,
                    "Datagen modules selected:\n" + String.join(", ", selectedDatagenModules.toString()),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(owner, "Canceled by user.", "Canceled", JOptionPane.WARNING_MESSAGE);
            cancel();
        }
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
