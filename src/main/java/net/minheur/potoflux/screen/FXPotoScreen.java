package net.minheur.potoflux.screen;

import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.screen.menu.MenuRegistry;
import net.minheur.potoflux.screen.menu.PotoMenuItem;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.screen.tabs.TabRegistry;

import java.util.*;

public class FXPotoScreen {

    private final Stage stage;
    private final MenuBar menu = new MenuBar();
    private final List<PotoMenuItem> menuItems = new ArrayList<>();

    private final Map<Tab, BaseTab> tabMap = new HashMap<>();
    private final TabPane tabs = new TabPane();

    public FXPotoScreen() {
        stage = new Stage();

        BorderPane root = new BorderPane();

        setupStage();
        addIcon();
        addMenu();
        addTabs();

        root.setTop(menu);
        root.setCenter(tabs);

        Scene scene = new Scene(root, 854, 512);
        stage.setScene(scene);
        stage.show();
    }

    private void addTabs() {
        tabs.setSide(Side.LEFT);

        List<Tab> allTabs = TabRegistry.getAll().stream()
                .sorted(Comparator.comparing(
                        tab -> !tab.id().getNamespace().equals(PotoFlux.ID)
                ))
                .toList();

        fillTabMap(allTabs);
    }

    private void fillTabMap(List<Tab> allTabs) {

        for (Tab tabType : allTabs) {
            BaseTab instance = tabType.createInstance();
            if (instance != null) {
                javafx.scene.control.Tab fxTab = new javafx.scene.control.Tab(tabType.name());

                fxTab.setContent(instance.getNode());
                tabs.getTabs().add(fxTab);

                tabMap.put(tabType, instance);
            }
        }

    }

    private void addMenu() {
        menuItems.clear();
        menuItems.addAll(MenuRegistry.getAll().stream()
                .sorted(Comparator.comparing(
                        item -> !item.id().getNamespace().equals(PotoFlux.ID)
                ))
                .toList()
        );

        for (PotoMenuItem item : menuItems) menu.getMenus().add(item.getNode());
    }

    private void addIcon() {
        stage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/textures/main.png")))
        );
    }

    private void setupStage() {
        stage.setTitle("Potoflux");

        stage.setOnCloseRequest(e -> {
            e.consume(); // anti auto-close
            PotoFlux.runProgramClosing(0);
        });

        Properties optionalFeatures = PotoFluxLoadingContext.getOptionalFeatures();
        boolean isResizable = Boolean.parseBoolean(optionalFeatures.getProperty("resizableWindow", "false"));
        stage.setResizable(isResizable);
    }
}
