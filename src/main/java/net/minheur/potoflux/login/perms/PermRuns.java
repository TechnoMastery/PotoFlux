package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.ui.AddUserDialog;

import javax.swing.*;

public class PermRuns {
    static void addUser() {
        JDialog dialog = new AddUserDialog(PotoFlux.app.getFrame());
        dialog.setVisible(true);
    }
}
