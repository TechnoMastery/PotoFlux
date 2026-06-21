package net.minheur.potoflux.utils;

import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

import static net.minheur.potoflux.ui.UiUtils.*;

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
        if (PotoFluxLoadingContext.isDevEnv()) return;

        Alert welcomeAlert = new Alert(Alert.AlertType.INFORMATION);
        welcomeAlert.setTitle("Welcome in PotoFlux!"); // todo
        welcomeAlert.setHeaderText("It is your first connection on PotoFlux.\nThank you for downloading it !"); // todo
        welcomeAlert.setContentText("Would you like to create / log in to your account ?"); // todo

        ButtonType create = new ButtonType("Create an account", ButtonBar.ButtonData.YES); // todo
        ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.YES); // todo

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

        if (selected == login) ConnectionHandler.login();
        if (selected == create) mkAccount();
    }

    private static void mkAccount() {
        CreateAccountDialog createDialog = new CreateAccountDialog();
        Optional<NewAccountData> createResult = createDialog.showAndWait();

        if (createResult.isEmpty()) {
            boolean retry = askRetry();
            if (retry) mkAccount();
            return;
        }
        NewAccountData newAccount = createResult.get();

        String createContent;
        try {
            createContent = RequestPoster.createAccount(
                    newAccount.email,
                    newAccount.password,
                    newAccount.firstName,
                    newAccount.lastName
            );
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));

            boolean retry = askRetry();
            if (retry) mkAccount();
            return;
        }

        BaseResponse createResponse = Json.GSON.fromJson(createContent, BaseResponse.class);
        if (!createResponse.success) {
            showErrorPane(
                    createResponse.error == null ? createContent :
                            switch (createResponse.error) {
                                case "disabled" -> Translations.get("potoflux:tabs.account.createAccount.disabled");
                                case "invalid_email" ->
                                        Translations.get("potoflux:tabs.account.createAccount.invalidEmail");
                                case "email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                                default -> createResponse.error;
                            }
            );
            if (createResponse.error.equals("disabled")) return;

            boolean retry = askRetry();
            if (retry) mkAccount();
            return;
        }

        showMessagePane("Account created with those IDs:\nEmail: " + newAccount.email, "\nPassword: " + newAccount.password +
                "\nPlease remember them ! The app will now try to automatically connect you..."); // todo

        tryAutoConnect(newAccount.email, newAccount.password);

    }

    private static void tryAutoConnect(String email, String password) {
        ConnectionHandler.checkAndRemoveExistingToken();
        String newToken = ConnectionHandler.getToken(email, password);

        if (newToken == null) {
            boolean retry = askRetry();
            if (retry) tryAutoConnect(email, password);
            return;
        }

        TokenHandler.save(newToken);
        ConnectionHandler.accountFor(newToken);
    }
}
