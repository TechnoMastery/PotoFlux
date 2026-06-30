package net.minheur.potoflux.loader;

import javafx.application.Platform;
import net.minheur.potoflux.PotoFlux;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Single instance handler class.
 */
public class SingleInstanceHandler {
    /**
     * The server socket field.
     */
    private static ServerSocket serverSocket;

    /**
     * Try to create lock.
     * @param port the port to lock
     * @return weather locked
     */
    public static boolean tryCreateLock(int port) {
        try {
            serverSocket = new ServerSocket(port);
            startListener();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Starts the listener.
     */
    public static void startListener() {
        Thread t = new Thread(() -> {

            while (true)
                try (Socket socket = serverSocket.accept()) {
                    bringToFront();
                } catch (Exception ignored) {
                }

        });

        t.setDaemon(true);
        t.start();
    }

    /**
     * Brings the app to front.
     */
    private static void bringToFront() {
        Platform.runLater(() -> {
            var stage = PotoFlux.app.getStage();

            if (stage.isIconified())
                stage.setIconified(false);

            stage.show();
            stage.toFront();
            stage.requestFocus();
        });
    }

    /**
     * Notify existing instance.
     * @param port the locked port
     */
    public static void notifyExistingInstance(int port) {
        try (Socket socket = new Socket("127.0.0.1", port)) {
            socket.getOutputStream().write(1);
        } catch (IOException ignored) {
        }
    }

}
