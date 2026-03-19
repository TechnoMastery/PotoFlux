package net.minheur.potoflux.terminal;

import java.util.ArrayList;
import java.util.List;

public class CommandHistorySaver {
    private static final List<String> history = new ArrayList<>();
    private static final int MAX_SIZE = 100;

    public static void save(String command) {
        if (!history.isEmpty() && history.get(0).equals(command)) return;

        history.add(0, command);

        if (history.size() > MAX_SIZE)
            history.remove(history.size() -1);
    }

    public static List<String> get() {
        return history;
    }
}
