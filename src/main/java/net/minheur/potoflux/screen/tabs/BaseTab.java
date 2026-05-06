package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;

/**
 * The base tab, overridden to create your own tabs.
 */
public abstract class BaseTab {
    /**
     * The actual {@link Pane}, that will be added to the tabbed pane.
     */
    protected Pane PANEL;

    /**
     * Constructor for the tab.<br>
     * It will do the preset if enabled, and execute (or invokeLater) the {@link #setPanel()} method, to add data to the {@link #PANEL}.
     */
    public BaseTab() {
        if (doPreset()) preset();
        if (invokeLater()) SwingUtilities.invokeLater(this::setPanel);
        else setPanel();
    }

    /**
     * The preset, that is by default enabled.<br>
     * It sets the layout and add the title.
     */
    protected void preset() {
        VBox panel = new VBox();
        panel.setSpacing(20);
        panel.setPadding(new Insets(30, 0, 0, 0));
        panel.setAlignment(Pos.TOP_CENTER);

        createTitle();
    }

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
     * Getter for the {@link #PANEL}.
     * @return the {@link #PANEL}
     */
    public Pane getNode() {
        return PANEL;
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
     * Creates the title, from the text given via {@link #getTitle()}.<br>
     * It is used in the {@link #preset()}
     */
    protected void createTitle() {
        Label title = new Label(getTitle());
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        PANEL.getChildren().add(title);
    }
}
