package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;

/**
 * Tab class for the home tab
 */
public class HomeTab extends BaseVTab<StackPane> {
    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    @Override
    protected void setPanel() {
        addDesc();
        addVersion();
    }

    @Override
    protected void instantiate() {
        PANEL = new StackPane();
    }

    /**
     * Adds the description of the app
     */
    private void addDesc() {
        Label desc = new Label(Translations.get("potoflux:tabs.home.credit"));
        desc.setFont(Font.font("Consolas", FontWeight.NORMAL, 15));
        vContent.getChildren().add(desc);
    }

    /**
     * Adds the version of the app
     */
    private void addVersion() {
        String name = Functions.formatMessage(Translations.get("potoflux:tabs.home.version"),
                PotoFlux.getVersion());

        Label version = new Label(name);
        version.setFont(Font.font("Consolas", FontWeight.NORMAL, 15));
        vContent.getChildren().add(version);
    }

    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.home.title");
    }
    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    @Override
    protected String getName() {
        return Translations.get("potoflux:tabs.home.name");
    }
}
