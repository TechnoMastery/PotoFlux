package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.ui.AddUserDialog;

import javax.swing.*;
import java.util.List;

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
    }
}
