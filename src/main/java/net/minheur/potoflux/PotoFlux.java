package net.minheur.potoflux;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import net.minheur.potoflux.actionRuns.LogicDelayedPopupsRegistry;
import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.logger.LogSaver;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.screen.FXLoadingScreen;
import net.minheur.potoflux.screen.FXPotoScreen;
import net.minheur.potoflux.utils.close.EventPostException;
import net.minheur.potoflux.utils.close.ExitCode;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Main class for PotoFlux. This should be set as mainClass in Gradle.
 */
public class PotoFlux extends Application {
    /**
     * The ID for potoflux (namespace)
     */
    public static final String ID = "potoflux";
    /**
     * The actual app.<br>
     * This contains the JFrame and will be instantiated when the app will run.
     */
    public static FXPotoScreen app;

    /**
     * The main method, that runs PotoFlux.<br>
     * It defines the uncaught exception handler, then runs {@link #launch}
     *
     * @param args what you give to the app. Can contain 'devEnv' to enable PotoFlux's dev mod
     */
    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            runProgramKill(ExitCode.UNCAUGHT_EXCEPTION);
        });

        launch(args);

    }

    /**
     * Getter for the main program AppData folder.
     *
     * @return the program's appData folder
     */
    public static @NotNull Path getProgramDir() {
        Path dir = Paths.get(System.getenv("APPDATA"), "TechnoMastery", "PotoFlux");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {
        }
        return dir;
    }

    /**
     * Getter for the mod common folder in the appData folder.<br>
     * Mods should use this to resolve their modIds: this will be their own appData folder
     *
     * @return the shared appData folder for mods
     */
    public static @NotNull Path getModDataDir() {
        Path dir = getProgramDir().resolve("mod-data");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {
        }
        return dir;
    }

    /**
     * This method should be used to kill the app.<br>
     * Used when crashing the app mainly
     *
     * @param exitCode the code given on closing.
     */
    public static void runProgramKill(ExitCode exitCode) {
        // executes when program close
        runExitLogic(exitCode);
        System.exit(exitCode.code()); // close app
    }

    /**
     * Runs the logic on exit, saves things and launches close action runs if exit code is {@code 0}.<br>
     * This allows the app to run extra saving code before exiting.
     *
     * @param exitCode the code given on closing.
     */
    public static void runExitLogic(@NotNull ExitCode exitCode) {
        if (exitCode.code() == 0) for (ActionRun ar : Bootstrap.runEvent.closeReg.getAll()) {
            try {
                ar.run().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (exitCode.code() != 0) {
            PtfLogger.error("Execution finished with non-0 exit code: " + exitCode);
            PtfLogger.error("For more info, please check the github page at https://github.com/TechnoMastery/PotoFlux");
        }

        // saves logs
        LogSaver.flushAndSave();
    }

    /**
     * Getter for the app's version.<br>
     * If unable to get the version, return null.
     *
     * @return the app version
     */
    public static @Nullable String getVersion() {
        try {
            Properties props = new Properties();
            props.load(PotoFlux.class.getResourceAsStream("/version.properties"));

            return props.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get version !");
            return null;
        }
    }

    /**
     * PotoFlux's reserved method to get a {@link ResourceLocation} directly with PotoFlux's modId.
     *
     * @param loc the loc to add to the modId
     * @return a built {@link ResourceLocation} with potoflux's modId and the loc given
     */
    @Contract("!null -> new; null -> fail")
    public static @NotNull ResourceLocation fromModId(String loc) {
        return new ResourceLocation(ID, loc);
    }

    /**
     * The entry point for Potoflux's UI.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * <p>
     * Will first display the {@link FXLoadingScreen}, then launch the bootstrap in a {@link Task}
     * (making sure it won't freeze the app).
     * It finally creates {@link FXPotoScreen} if the bootstrap succeeded
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        FXLoadingScreen startScreen = new FXLoadingScreen();
        startScreen.setup();
        startScreen.show();

        Task<Void> bootstrap = new Task<Void>() {
            @Override
            protected @Nullable Void call() throws Exception {
                Bootstrap.bootstrap(this::updateMessage, getParameters().getRaw().toArray(new String[0]));
                return null;
            }
        };

        bootstrap.messageProperty().addListener((obs, old, value) ->
                startScreen.updateStage(value)
        );

        bootstrap.setOnSucceeded(event -> {
            startScreen.updateStage("Launching app...");
            app = new FXPotoScreen(primaryStage);
            startScreen.close();

            LogicDelayedPopupsRegistry.run();

            for (ActionRun ar : Bootstrap.runEvent.startUiReg.getAll()) ar.run().run();
        });

        bootstrap.setOnFailed(event -> {
            Throwable e = bootstrap.getException();

            if (e != null)
                e.printStackTrace();

            if (e instanceof EventPostException)
                runProgramKill(ExitCode.REGISTRATION_FAILED);

            else runProgramKill(ExitCode.BOOTSTRAP_FAILED);

        });

        RequestPoster.warmupTls();

        Thread bootstrapThread = new Thread(bootstrap);
        bootstrapThread.setDaemon(true);
        bootstrapThread.start();

    }

    /**
     * When the app is closed normally in the JavaFX system, this gets called to run the exit logic.
     */
    @Override
    public void stop() {
        runExitLogic(ExitCode.SUCCESS);
    }
}
