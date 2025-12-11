package net.minheur.potoflux.screen;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.TabRegistry;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class PotoScreen {
    private final JFrame frame;
    private final Map<Tab, BaseTab> tabMap = new HashMap<>();
    private final JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);

    public PotoScreen() {
        frame = new JFrame("PotoFlux");
        frame.setSize(854, 512);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PotoFlux.runProgramClosing(0);
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        addIcon();
        addPanels();

        frame.setVisible(true);
    }

    private void addPanels() {
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        List<Tab> allTabs = TabRegistry.getAll().stream() // get all tabs
                .sorted(Comparator.comparing( // compare
                        tab -> !tab.id().getNamespace().equals(PotoFlux.ID) // true → added after
                ))
                .toList();

        // register all tabs in ones
        for (Tab tabType : allTabs) {
            BaseTab instance = tabType.createInstance();
            if (instance != null) {
                tabs.add(tabType.name(), instance.getPanel());
                this.tabMap.put(tabType, instance);
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

    public Map<Tab, BaseTab> getTabMap() {
        return tabMap;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setOpenedTab(Tab tab) {
        if (!tabMap.containsKey(tab)) {
            JOptionPane.showMessageDialog(frame, Translations.get("screen.tabHereNotHere"));
            return;
        }
        tabs.setSelectedComponent(tabMap.get(tab).getPanel());
    }
}
