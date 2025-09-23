package net.minheur.potoflux.screen;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.terminal.CommandProcessor;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PotoScreen {
    private final JFrame frame;
    private final Map<Tabs, BaseTab> tabMap = new HashMap<>();
    private final JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);

    public PotoScreen() {
        frame = new JFrame("PotoFlux");
        frame.setSize(854, 512);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        addIcon();
        addPanels();

        frame.setVisible(true);
    }

    private void addPanels() {
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        for (Tabs tab : Tabs.values()) {
            BaseTab instance = tab.createInstance();
            if (instance != null) {
                tabs.add(tab.getName(), instance.getPanel());
                this.tabMap.put(tab, instance);
            }
        }

        frame.add(tabs);
    }

    private void addIcon() {
        Image icon600 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/main.png"))).getImage();
        List<Image> icons = new ArrayList<>();
        icons.add(icon600.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons.add(icon600.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        icons.add(icon600.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        frame.setIconImages(icons);
    }

    public Map<Tabs, BaseTab> getTabMap() {
        return tabMap;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setOpenedTab(Tabs tab) {
        if (!tabMap.containsKey(tab)) {
            JOptionPane.showMessageDialog(frame, "ERREUR: cet tab est détecté mais n'est pas ajouté !");
            return;
        }
        tabs.setSelectedComponent(tabMap.get(tab).getPanel());
    }
}
