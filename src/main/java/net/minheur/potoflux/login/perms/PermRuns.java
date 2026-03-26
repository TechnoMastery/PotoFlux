package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.InvalidTokenException;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.login.TokenHandler;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.AddUserDialog;
import net.minheur.potoflux.ui.RmUserDialog;
import net.minheur.potoflux.utils.Json;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.ui.UiUtils.*;

public class PermRuns {
    static void addUser() {
        AddUserDialog dialog = new AddUserDialog(PotoFlux.app.getFrame());
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) return;

        String email = dialog.getEmail();
        String password = dialog.getPassword();
        String firstName = dialog.getFirstName();
        String lastName = dialog.getLastName();

        List<Perms> perms = dialog.getSelectedPerms();
        List<String> permsCode = new ArrayList<>();

        for (Perms p : perms)
            permsCode.add(p.getCode());

        String content;
        try {
            content = RequestPoster.addUser(
                    TokenHandler.get(),
                    email, password,
                    firstName, lastName,
                    permsCode.toArray(new String[0])
            );
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
            return;
        } catch (IOException e) {
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);
        if (response.success) {
            showMessagePane(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.addUser.success"),
                    email
            ));
            return;
        }

        showErrorPane(
                switch (response.error) {
                    case "no_permission" -> Translations.get("potoflux:tabs.account.addUser.noPerm");
                    case "email_used" -> Translations.get("potoflux:tabs.account.addUser.emailUsed");
                    case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                    case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                    default -> response.error;
                }
        );
    }

    public static void rmUser() {
        RmUserDialog dialog = new RmUserDialog(PotoFlux.app.getFrame());
        dialog.setVisible(true);
    }
}
