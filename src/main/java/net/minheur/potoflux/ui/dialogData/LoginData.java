package net.minheur.potoflux.ui.dialogData;

/**
 * Login data class.
 */
public class LoginData {
    /**
     * The username field.
     */
    private final String username;
    /**
     * The password field.
     */
    private final String password;

    /**
     * Constructs a new LoginData.
     */
    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username
     * @return {@link #username}
     */
    public String username() {
        return username;
    }

    /**
     * Gets the password
     * @return {@link #password}
     */
    public String password() {
        return password;
    }
}
