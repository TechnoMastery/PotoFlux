package net.minheur.potoflux.terminal;

import java.util.ArrayList;
import java.util.List;

/**
 * Store, handle and save the command history
 */
public class CommandHistorySaver {
    /**
     * History length. There is no more commands stored as this number.
     */
    public static final int MAX_SIZE = 100;
    /**
     * Array of all stored commands
     */
    private static final List<String> history = new ArrayList<>();

    /**
     * Save a command to the history.<br>
     * If the command is already the lastest, do nothing.<br>
     * Inserts the command in the first position, then if the history is already full, remove the last command.
     *
     * @param command to store in the history
     */
    public static void save(String command) {
        if (!history.isEmpty() && history.get(0).equals(command)) return;

        history.add(0, command);

        if (history.size() > MAX_SIZE)
            history.remove(history.size() - 1);
    }

    /**
     * Getter for the entire history
     *
     * @return {@link #history}
     */
    public static List<String> get() {
        return history;
    }

    /**
     * Load all history from another list, after clearing all existing commands.
     *
     * @param pHistory list to load from
     */
    public static void loadFrom(List<String> pHistory) {
        history.clear();
        history.addAll(pHistory);
    }
}
