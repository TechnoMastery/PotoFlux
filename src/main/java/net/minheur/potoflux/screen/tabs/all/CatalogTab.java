package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.catalog.CatalogGetterHandler;
import net.minheur.potoflux.catalog.CatalogHelpers;
import net.minheur.potoflux.catalog.ModCatalog;
import net.minheur.potoflux.catalog.mods.CatalogTabRegistry;
import net.minheur.potoflux.catalog.mods.ModCatalogTab;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;

public class CatalogTab extends BaseTab {
    private List<ModCatalog> catalog;
    private JTabbedPane tabbedPane;

    @Override
    protected void setPanel() {
        reloadCatalog();

        tabbedPane = new JTabbedPane();

        // add tabs
        tabbedPane.addTab("Mods catalog", mkModCatalog()); // TODO

        // add mod tabs
        Collection<ModCatalogTab> allModTabs = CatalogTabRegistry.getAll();

        for (ModCatalogTab tab : allModTabs) {
            tabbedPane.add(Translations.get(tab.name()), tab.getPanel());
            PtfLogger.info("Added tab " + tab.id(), LogCategories.CATALOG);
        }

        // Ajouter le tabbedPane au PANEL principal
        PANEL.setLayout(new BorderLayout());
        PANEL.add(tabbedPane, BorderLayout.CENTER);

        PANEL.revalidate();
        PANEL.repaint();
    }

    public JPanel mkModCatalog() {
        JPanel modsPanel = new JPanel();
        modsPanel.setLayout(new BoxLayout(modsPanel, BoxLayout.Y_AXIS));
        modsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (catalog.isEmpty()) {
            JLabel empty = new JLabel("No mods available."); // TODO
            empty.setForeground(Color.LIGHT_GRAY);
            modsPanel.add(empty);
        } else {
            for (ModCatalog mod : catalog) {
                modsPanel.add(CatalogHelpers.buildCard(mod));
                modsPanel.add(Box.createVerticalStrut(8)); // separate cards
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(modsPanel, BorderLayout.NORTH);
        wrapper.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel root = new JPanel(new BorderLayout());
        root.add(scrollPane, BorderLayout.CENTER);

        return root;
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
