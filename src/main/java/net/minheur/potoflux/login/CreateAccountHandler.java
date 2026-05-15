package net.minheur.potoflux.login;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.dialogData.NewAccountData;
import net.minheur.potoflux.ui.dialogs.CreateAccountDialog;
import net.minheur.potoflux.utils.Json;

import java.io.IOException;
import java.util.Optional;

import static net.minheur.potoflux.Functions.formatMessage;
import static net.minheur.potoflux.ui.UiUtils.*;

/**
 * This class is in charge when you click the create account button.<br>
 * It only handles when you create your own account, not when an admin creates one.
 */
public class CreateAccountHandler {

    public static void create() {

        CreateAccountDialog dialog = new CreateAccountDialog();
        Optional<NewAccountData> result = dialog.showAndWait();

        final String[] email = new String[1];
        final String[] password = new String[1];
        final String[] firstName = new String[1];
        final String[] lastName = new String[1];
        boolean[] validated = {false};

        result.ifPresent(data -> {
            email[0] = data.email;
            password[0] = data.password;
            firstName[0] = data.firstName;
            lastName[0] = data.lastName;
            validated[0] = true;
        });

        if (!validated[0]) return;

        String content;
        try {
            content = RequestPoster.createAccount(
                    email[0], password[0],
                    firstName[0], lastName[0]
            );
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);
        if (response.success) {
            showMessagePane(formatMessage(Translations.get("potoflux:tabs.account.createAccount.success"), email[0], password));
            return;
        }

        showErrorPane(
                response.error == null ? content :
                switch (response.error) {
                    case "disabled" -> Translations.get("potoflux:tabs.account.createAccount.disabled");
                    case "invalid_email" -> Translations.get("potoflux:tabs.account.createAccount.invalidEmail");
                    case "email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                    default -> response.error;
                }
        );

    }

}
