package net.minheur.potoflux.screen;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.Tabs;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PotoScreen {
    private final JFrame frame;
    private final Map<Tabs, BaseTab> tabs = new HashMap<>();

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
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        for (Tabs tab : Tabs.values()) {
            BaseTab instance = tab.createInstance();
            if (instance != null) {
                tabs.add(tab.getName(), instance.getPanel());
                this.tabs.put(tab, instance);
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

    public Map<Tabs, BaseTab> getTabs() {
        return tabs;
    }

    public JFrame getFrame() {
        return frame;
    }
}
