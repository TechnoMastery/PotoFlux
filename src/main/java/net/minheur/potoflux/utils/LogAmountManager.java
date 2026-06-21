package net.minheur.potoflux.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.CreateAccountHandler;
import net.minheur.potoflux.ui.UiUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

/**
 * Manages the log amount, storing and getting the value
 */
public final class LogAmountManager {
    private static final Preferences data = Preferences.userNodeForPackage(LogAmountManager.class);
    private static final String KEY_LOG_AMOUNT = "log_amount";
    private static final AtomicBoolean started = new AtomicBoolean(false);

    private LogAmountManager() {}

    public static synchronized void init() {
        if (!started.compareAndSet(false, true)) return;
        if (PotoFluxLoadingContext.isDevEnv()) return;

        int logAmount = getLogAmount() + 1;
        data.putInt(KEY_LOG_AMOUNT, logAmount);
    }

    public static int getLogAmount() {
        return data.getInt(KEY_LOG_AMOUNT, 0);
    }

    public static void displayWelcome() {
        Alert welcomeAlert = new Alert(Alert.AlertType.INFORMATION);
        welcomeAlert.setTitle("Welcome in PotoFlux!");
        welcomeAlert.setHeaderText("It is your first connection on PotoFlux.\nThank you for downloading it !");
        welcomeAlert.setContentText("Would you like to create / log in to your account ?");

        ButtonType create = new ButtonType("Create an account", ButtonBar.ButtonData.YES);
        ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.YES);

        welcomeAlert.getDialogPane().getButtonTypes().clear();
        welcomeAlert.getDialogPane().getButtonTypes().addAll(create, login, UiUtils.noButton.get());

        ((Button) welcomeAlert.getDialogPane()
                .lookupButton(create))
                .setDefaultButton(true);
        ((Button) welcomeAlert.getDialogPane()
                .lookupButton(login))
                .setDefaultButton(false);
        ((Button) welcomeAlert.getDialogPane()
                .lookupButton(UiUtils.noButton.get()))
                .setCancelButton(true);

        ButtonType selected = welcomeAlert.showAndWait().orElse(UiUtils.noButton.get());

        if (selected == create) CreateAccountHandler.create();
        if (selected == login) ConnectionHandler.login();
    }
}
