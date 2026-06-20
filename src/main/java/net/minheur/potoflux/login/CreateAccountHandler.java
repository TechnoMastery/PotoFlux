package net.minheur.potoflux.login;

import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.dialogData.NewAccountData;
import net.minheur.potoflux.ui.dialogs.CreateAccountDialog;
import net.minheur.potoflux.utils.Json;

import java.io.IOException;
import java.util.Optional;

import static net.minheur.potoflux.Functions.formatMessage;
import static net.minheur.potoflux.ui.UiUtils.showErrorPane;
import static net.minheur.potoflux.ui.UiUtils.showMessagePane;

/**
 * This class is in charge when you click the create account button.<br>
 * It only handles when you create your own account, not when an admin creates one.
 */
public class CreateAccountHandler {

    /**
     * Creates, displays and interprets a {@link CreateAccountDialog}.<br>
     * If validated, will send a request to create the account on the server
     */
    public static void create() {

        CreateAccountDialog dialog = new CreateAccountDialog();
        Optional<NewAccountData> result = dialog.showAndWait();

        if (result.isEmpty()) return;

        final String email = result.get().email;
        final String password = result.get().password;
        final String firstName = result.get().firstName;
        final String lastName = result.get().lastName;

        String content;
        try {
            content = RequestPoster.createAccount(
                    email, password,
                    firstName, lastName
            );
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);
        if (response.success) {
            showMessagePane(formatMessage(Translations.get("potoflux:tabs.account.createAccount.success"), email, password));
            return;
        }

        showErrorPane(
                response.error == null ? content :
                        switch (response.error) {
                            case "disabled" -> Translations.get("potoflux:tabs.account.createAccount.disabled");
                            case "invalid_email" ->
                                    Translations.get("potoflux:tabs.account.createAccount.invalidEmail");
                            case "email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                            default -> response.error;
                        }
        );

    }

}
