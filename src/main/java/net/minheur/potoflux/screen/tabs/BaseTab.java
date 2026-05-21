package net.minheur.potoflux.screen.tabs;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;

/**
 * The base tab, overridden to create your own tabs.
 */
public abstract class BaseTab<T extends Pane> {
    /**
     * The actual {@link Pane}, that will be added to the tabbed pane.
     */
    protected T PANEL;
    private final Tab builtTab;

    /**
     * Constructor for the tab.<br>
     * It will do the preset if enabled, and execute (or invokeLater) the {@link #setPanel()} method, to add data to the {@link #PANEL}.
     */
    public BaseTab() {
        instantiate();
        if (doPreset()) runPreset();

        if (invokeLater()) SwingUtilities.invokeLater(this::setPanel);
        else setPanel();

        this.builtTab = new Tab("A test Name");
        this.builtTab.setContent(PANEL);
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
    void runPreset() {};

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
     * Handles if the preset should be executed.<br>
     * True by default.
     * @return if the preset should be executed
     */
    protected boolean doPreset() {
        return true;
    }

    /**
     * Handles if the creation should be added to the swing EDT, otherwise it will be executed on the tab creation.
     * @return if the app should be {@link SwingUtilities#invokeLater(Runnable)}.
     */
    @Deprecated(since = "6.4")
    protected boolean invokeLater() {
        return false;
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
