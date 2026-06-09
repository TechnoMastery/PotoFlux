package net.minheur.potoflux.screen.tabs;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * The base tab, overridden to create your own tabs.
 * @param <T> type of {@link Pane} added to the tab
 */
public abstract class BaseTab<T extends Pane> {
    /**
     * The actual {@link Pane}, that will be added to the tabbed pane.
     */
    protected T PANEL;
    /**
     * The built tab, with the graphic, name and panel
     */
    private final Tab builtTab;

    /**
     * Constructor for the tab.<br>
     * It will do the preset if enabled, and execute (or invokeLater) the {@link #setPanel()} method, to add data to the {@link #PANEL}.
     */
    public BaseTab() {
        instantiate();
        if (doPreset()) runPreset();

        setPanel();

        this.builtTab = new Tab();
        setupBuiltTab();
    }

    /**
     * Creates the {@linkplain #builtTab}, and adds the name, panel and graphic
     */
    private void setupBuiltTab() {
        builtTab.setContent(PANEL);
        builtTab.setClosable(false);

        builtTab.setText(getName());
    }
    public Tab getBuiltTab() {
        return builtTab;
    }

    /**
     * You need to instantiate {@link #PANEL}.
     */
    protected abstract void instantiate();

    /**
     * This is reserved to other Base tabs, allowing presets
     */
    void runPreset() {}

    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    protected abstract void setPanel();
    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    protected String getTitle() {
        return "[NO TITLE SET]";
    }
    /**
     * The name of the tab in the list displayed
     * @return the tab's name
     */
    public abstract String getName();
    /**
     * Sets the graphic displayed in the tab list
     * @return the tab's icon
     */
    protected @Nullable Node getIcon() {
        return null;
    }

    /**
     * Handles if the preset should be executed.<br>
     * True by default.
     * @return if the preset should be executed
     */
    protected boolean doPreset() {
        return true;
    }

    /**
     * Creates the title, from the text given via {@link #getTitle()}.
     */
    protected Label mkTitle() {
        Label title = new Label(getTitle());
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        return title;
    }
}
