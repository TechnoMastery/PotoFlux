package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.catalog.CatalogGetterHandler;
import net.minheur.potoflux.catalog.ModCatalog;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CatalogTab extends BaseTab {
    private List<ModCatalog> catalog;
    private JTabbedPane tabbedPane;

    @Override
    protected void setPanel() {
        reloadCatalog();

        PANEL.setLayout(new BoxLayout(PANEL, BoxLayout.X_AXIS));
        tabbedPane = new JTabbedPane();

        // Tab "Main" : liste des mods
        JPanel mainPanel = createModsPanel();
        tabbedPane.addTab("Main", mainPanel);

        // Ajouter le tabbedPane au PANEL principal
        PANEL.setLayout(new BorderLayout());
        PANEL.add(tabbedPane, BorderLayout.CENTER);

        PANEL.revalidate();
        PANEL.repaint();
    }

    private JPanel createModsPanel() {
        JPanel modsPanel = new JPanel();
        modsPanel.setLayout(new BoxLayout(modsPanel, BoxLayout.X_AXIS));
        modsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (catalog.isEmpty()) {
            modsPanel.add(new JLabel("No mods available."));
        } else {
            for (ModCatalog mod : catalog) {
                JPanel modPanel = new JPanel();
                modPanel.setLayout(new BorderLayout());
                JLabel label = new JLabel(mod.modId + " -- " + mod.isPublished);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                modPanel.add(label, BorderLayout.CENTER);

                modPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                modsPanel.add(modPanel);
            }
        }

        modsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return modsPanel;
    }

    public void reloadCatalog() {
        CatalogGetterHandler.buildCatalog();
        this.catalog = CatalogGetterHandler.getCatalog();
    }

    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.catalog.name");
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
