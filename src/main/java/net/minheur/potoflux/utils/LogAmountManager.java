package net.minheur.potoflux.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.login.TokenHandler;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;
import net.minheur.potoflux.ui.dialogData.NewAccountData;
import net.minheur.potoflux.ui.dialogs.CreateAccountDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

import static net.minheur.potoflux.ui.UiUtils.*;

/**
 * Manages the log amount, storing and getting the value
 */
public final class LogAmountManager {
    /**
     * The data, in the prefs
     */
    private static final Preferences data = Preferences.userNodeForPackage(LogAmountManager.class);
    /**
     * The key to get log amount field.
     */
    private static final String KEY_LOG_AMOUNT = "log_amount";
    /**
     * Weather app already started the manager
     */
    private static final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * Locks class's instantiation
     */
    private LogAmountManager() {}

    /**
     * Initializes.<br>
     * If {@linkplain #started} is {@code true}, passes, or else sets it to {@code true}.<br>
     * If is in dev env, passes.<br>
     * Increases log amount by one.
     */
    public static synchronized void init() {
        if (!started.compareAndSet(false, true)) return;
        if (PotoFluxLoadingContext.isDevEnv()) return;

        int logAmount = getLogAmount() + 1;
        data.putInt(KEY_LOG_AMOUNT, logAmount);
    }

    /**
     * Gets the log amount.
     * @return the log amount
     */
    public static int getLogAmount() {
        return data.getInt(KEY_LOG_AMOUNT, 0);
    }

    /**
     * Display welcome, if not in dev env.
     */
    public static void displayWelcome() {
        if (PotoFluxLoadingContext.isDevEnv()) return;

        Alert welcomeAlert = new Alert(Alert.AlertType.INFORMATION);
        welcomeAlert.setTitle(Translations.get("potoflux:welcome.title"));
        welcomeAlert.setHeaderText(Translations.get("potoflux:welcome.header"));
        welcomeAlert.setContentText(Translations.get("potoflux:welcome.content"));

        welcomeAlert.getDialogPane().getButtonTypes().clear();
        welcomeAlert.getDialogPane().getButtonTypes().addAll(okButton.get());

        ((Button) welcomeAlert.getDialogPane()
                .lookupButton(okButton.get()))
                .setDefaultButton(true);

        welcomeAlert.show();
    }
}
