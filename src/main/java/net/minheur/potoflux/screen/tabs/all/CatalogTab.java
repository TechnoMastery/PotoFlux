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

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 * The catalog of mods of potoflux.<br>
 * Used to download mods
 */
public class CatalogTab extends BaseTab {
    /**
     * The list of all mod catalog found
     */
    private List<ModCatalog> catalog;
    /**
     * The tabbed pane containing all tabs of the catalog
     */
    private JTabbedPane tabbedPane;

    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    @Override
    protected void setPanel() {
        reloadCatalog();

        tabbedPane = new JTabbedPane();

        // add tabs
        tabbedPane.addTab("Mods catalog", mkModCatalog()); // TODO
        tabbedPane.addTab("My mods", mkMyMods()); // TODO

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

    /**
     * Creates the tab containing the list of all mods existing
     * @return the catalog panel
     */
    private JPanel mkModCatalog() {
        JPanel modsPanel = mkModPanel();

        fillModPanel(modsPanel);

        JScrollPane scrollPane = mkModScrollWithWrapper(modsPanel);

        JPanel root = new JPanel(new BorderLayout());
        root.add(scrollPane, BorderLayout.CENTER);

        return root;
    }

    @Nonnull
    private static JScrollPane mkModScrollWithWrapper(JPanel modsPanel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(modsPanel, BorderLayout.NORTH);
        wrapper.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void fillModPanel(JPanel modsPanel) {
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
    }

    @Nonnull
    private static JPanel mkModPanel() {
        JPanel modsPanel = new JPanel();
        modsPanel.setLayout(new BoxLayout(modsPanel, BoxLayout.Y_AXIS));
        modsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return modsPanel;
    }

    /**
     * Creates the tab containing all installed mods
     * @return the mod panel
     */
    private JPanel mkMyMods() {
        JPanel root = new JPanel();

        // TODO

        return root;
    }

    /**
     * Reloads the catalog content
     */
    public void reloadCatalog() {
        CatalogGetterHandler.buildCatalog();
        this.catalog = CatalogGetterHandler.getCatalog();
    }

    /**
     * Disables the preset
     * @return false
     */
    @Override
    protected boolean doPreset() {
        return false;
    }
}
