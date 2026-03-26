package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.InvalidTokenException;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.login.TokenHandler;
import net.minheur.potoflux.ui.AddUserDialog;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.Functions.showErrorPane;

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
            showErrorPane("Invalid token error !");
            return;
        } catch (IOException e) {
            showErrorPane("Failed to contact the server !");
            return;
        }
    }
}
