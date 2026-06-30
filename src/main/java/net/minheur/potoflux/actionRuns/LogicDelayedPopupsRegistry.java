package net.minheur.potoflux.actionRuns;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic delayed popups registry class.<br>
 * Stores all methods that are pulled in logic init but need to run on the FX thread.
 */
public final class LogicDelayedPopupsRegistry {

    /**
     * The delayed runs list.
     */
    private static final List<Runnable> delayedRuns = new ArrayList<>();
    /**
     * The state of the reg.
     */
    private static State state = State.READY;

    /**
     * Locks class's instantiation
     */
    private LogicDelayedPopupsRegistry() {}

    /**
     * Adds an item ot the reg.
     * @param item the item to add
     */
    public static void addItem(Runnable item) {
        if (state != State.OPEN)
            throw new IllegalStateException("Trying to fill logic delayed popup reg while not opened !");

        delayedRuns.add(item);
    }

    /**
     * Handles checking {@linkplain #state} to run popups
     */
    public static void run() {
        if (state != State.CLOSED)
            throw new IllegalStateException("Trying to run logic delayed popup reg while not ready or already ran !");

        runAllDelayed();

        state = State.RAN;

    }

    /**
     * Runs all delayed.
     */
    private static void runAllDelayed() {

        for (Runnable r : delayedRuns)
            Platform.runLater(r);

    }

    /**
     * Opens the reg
     */
    public static void open() {
        if (state != State.READY)
            throw new IllegalStateException("Trying to open logic delayed popup reg after READY stage !");

        state = State.OPEN;
    }

    /**
     * Closes the reg
     */
    public static void close() {
        if (state != State.OPEN)
            throw new IllegalStateException("Trying the close logic delayed popup reg while not opened !");

        state = State.CLOSED;
    }

    /**
     * Checks if the reg is opened.
     * @return whether the reg is opened
     */
    public static boolean isOpened() {
        return state == State.OPEN;
    }

    /**
     * State enum.
     */
    public enum State {
        READY, OPEN, CLOSED, RAN
    }

}
