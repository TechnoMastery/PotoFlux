package net.minheur.potoflux.actionRuns;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public final class LogicDelayedPopupsRegistry {

    private static final List<Runnable> delayedRuns = new ArrayList<>();
    private static State state = State.READY;

    private LogicDelayedPopupsRegistry() {}

    public static void addItem(Runnable item) {
        if (state != State.OPEN)
            throw new IllegalStateException("Trying to fill logic delayed popup reg while not opened !");

        delayedRuns.add(item);
    }

    public static void run() {
        if (state != State.CLOSED)
            throw new IllegalStateException("Trying to run logic delayed popup reg while not ready or already ran !");

        runAllDelayed();

        state = State.RAN;

    }
    private static void runAllDelayed() {

        for (Runnable r : delayedRuns)
            Platform.runLater(r);

    }

    public static void open() {
        if (state != State.READY)
            throw new IllegalStateException("Trying to open logic delayed popup reg after READY stage !");

        state = State.OPEN;
    }
    public static void close() {
        if (state != State.OPEN)
            throw new IllegalStateException("Trying the close logic delayed popup reg while not opened !");

        state = State.CLOSED;
    }

    public static boolean isOpened() {
        return state == State.OPEN;
    }

    public enum State {
        READY, OPEN, CLOSED, RAN
    }

}
