package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
    protected VBox content;

    /**
     * Constructor for the tab.<br>
     * It will do the preset if enabled, and execute (or invokeLater) the {@link #setPanel()} method, to add data to the {@link #PANEL}.
     */
    public BaseTab() {
        instantiate();

        if (doPreset()) preset();
        if (invokeLater()) SwingUtilities.invokeLater(this::setPanel);
        else setPanel();
    }

    /**
     * You need to instantiate {@link #PANEL} here, and also {@link #content} if {@link #doPreset()} is off
     */
    protected abstract void instantiate();

    /**
     * The preset, that is by default enabled.<br>
     * It sets the layout and add the title.
     */
    protected void preset() {
        content = new VBox();
        content.setSpacing(20);
        content.setPadding(new Insets(30, 0, 0, 0));
        content.setAlignment(Pos.TOP_CENTER);

        VBox.setVgrow(content, Priority.ALWAYS);

        PANEL.getChildren().add(content);
        content.getChildren().add(mkTitle());
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
    protected Label mkTitle() {
        Label title = new Label(getTitle());
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        return title;
    }
}
