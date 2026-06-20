package net.minheur.potoflux.ui.dialogData;

public class LoginData {
    private final String _username;
    private final String _password;

    public LoginData(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public String username() {
        return _username;
    }

    public String password() {
        return _password;
    }
}
