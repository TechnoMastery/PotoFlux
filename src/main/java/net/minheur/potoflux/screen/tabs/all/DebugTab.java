package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.screen.tabs.BaseVTab;

import java.io.IOException;

public class DebugTab extends BaseVTab<BorderPane> {
    @Override
    protected void instantiate() {
        PANEL = new BorderPane();
        vContent = new VBox();
        PANEL.setCenter(vContent);
        vContent.getChildren().add(new Label("DEBUG tab"));
    }

    @Override
    protected void setPanel() {
        Button btn = new Button("MK ACCOUNT");
        btn.setOnAction((event) -> {
            try {
                String content = RequestPoster.selfCreateAccount("adrien.rousselet@hotmail.com", "PotatoChef");
                PtfLogger.info(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        vContent.getChildren().add(btn);
    }

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
