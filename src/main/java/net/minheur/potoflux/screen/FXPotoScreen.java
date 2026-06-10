package net.minheur.potoflux.screen;

import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.minheur.potoflux.Bootstrap;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.menu.MenuRegistry;
import net.minheur.potoflux.screen.menu.PotoMenuItem;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.screen.tabs.TabRegistry;
import net.minheur.potoflux.screen.tabs.TabSides;
import net.minheur.potoflux.settings.OptionalFeaturesManager;
import net.minheur.potoflux.settings.Settings;
import net.minheur.potoflux.settings.UserPrefsManager;
import net.minheur.potoflux.styles.StylesheetEntry;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Potoflux's screen
 */
public class FXPotoScreen {

    /**
     * Actual screen
     */
    private final Stage stage;
    private final Scene scene;
    /**
     * Potoflux's menu
     */
    private final MenuBar menu = new MenuBar();
    /**
     * All items to be added to the {@link #menu}
     */
    private final List<PotoMenuItem> menuItems = new ArrayList<>();

    /**
     * Map containing all tabs to put in {@link #tabs}
     */
    private final Map<Tab, BaseTab<?>> tabMap = new HashMap<>();
    /**
     * Tabs of Potoflux
     */
    private final TabPane tabs = new TabPane();

    /**
     * The constructor will init the stage, the layout, then add every item to it
     */
    public FXPotoScreen(Stage stage) {
        this.stage = stage;

        BorderPane root = new BorderPane();
        this.scene = new Scene(root, 854, 512);
        addStyles();

        setupStage();
        addIcon();
        addMenu();
        addTabs();

        root.setTop(menu);
        root.setCenter(tabs);

        this.stage.setScene(scene);
        this.stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Adds the tabs to the stage
     */
    private void addTabs() {
        TabSides placement = TabSides.getFromCode(
                (String) UserPrefsManager.getValueFor(Settings.TAB_PLACEMENT.get())
        );
        if (placement == null) placement = TabSides.LEFT;
        tabs.setSide(placement.getSide());

        List<Tab> allTabs = Bootstrap.tabEvent.reg.getAll().stream()
                .sorted(Comparator.comparing(
                        tab -> !tab.id().getNamespace().equals(PotoFlux.ID)
                ))
                .toList();

        fillTabMap(allTabs);
    }

    /**
     * With the list of tabs, instancies all tabs and adds them in the {@link #tabMap} and in {@link #tabs}
     * @param allTabs list of all {@link Tab}
     */
    private void fillTabMap(@NotNull List<Tab> allTabs) {

        for (Tab tabType : allTabs) {
            BaseTab<?> instance = tabType.createInstance();
            if (instance != null) {

                tabs.getTabs().add(instance.getBuiltTab());
                tabMap.put(tabType, instance);

            }
        }

    }

    /**
     * Getter for the {@link #tabMap}
     * @return {@link #tabMap}
     */
    public Map<Tab, BaseTab<?>> getTabMap() {
        return tabMap;
    }

    /**
     * Adds the menu to the UI
     */
    private void addMenu() {
        menuItems.clear();
        menuItems.addAll(Bootstrap.menuEvent.reg.getAll().stream()
                .sorted(Comparator.comparing(
                        item -> !item.id().getNamespace().equals(PotoFlux.ID)
                ))
                .toList()
        );

        for (PotoMenuItem item : menuItems) menu.getMenus().add(item.content());
    }

    /**
     * Adds Potoflux's icon
     */
    private void addIcon() {
        stage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/textures/main.png")))
        );
    }

    /**
     * Setup the {@linkplain #stage}, adding the title and properties
     */
    private void setupStage() {
        stage.setTitle("Potoflux");

        boolean isResizable = OptionalFeaturesManager.getBoolean("resizableWindow", false);
        stage.setResizable(isResizable);
    }

    private void addStyles() {
        List<StylesheetEntry> entries = Bootstrap.stylesheetsEvent.reg.getAll()
                .stream()
                .sorted(Comparator.comparing(
                        stylesheet -> !stylesheet.id().getNamespace().equals(PotoFlux.ID)
                )).toList();

        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(
                entries.stream().map(
                        StylesheetEntry::stylesheetDir
                ).toList()
        );
    }

    /**
     * Helper to open a given tab
     * @param tab to open
     */
    public void setOpenedTab(Tab tab) {
        if (!tabMap.containsKey(tab)) {
            UiUtils.showMessagePane(Translations.get("potoflux:screen.tabHereNotHere"));
            return;
        }
        tabs.getSelectionModel().select(tabMap.get(tab).getBuiltTab());
    }
}
