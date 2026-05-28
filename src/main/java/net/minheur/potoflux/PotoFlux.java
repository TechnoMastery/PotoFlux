package net.minheur.potoflux;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.actionRuns.regs.CloseRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartUiRunRegistry;
import net.minheur.potoflux.logger.LogSaver;
import net.minheur.potoflux.screen.FXLoadingScreen;
import net.minheur.potoflux.screen.FXPotoScreen;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.close.EventPostException;
import net.minheur.potoflux.utils.close.ExitCode;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

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

    @Override
    public void start(Stage primaryStage) {

        FXLoadingScreen startScreen = new FXLoadingScreen();
        startScreen.setup();
        startScreen.show();

        Task<Void> bootstrap = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Bootstrap.bootstrap(this::updateMessage, getParameters().getRaw().toArray(new String[0]));
                return null;
            }
        };

        bootstrap.messageProperty().addListener((obs, old, value) ->
                startScreen.updateStage(value)
        );

        bootstrap.setOnSucceeded(event -> {
            startScreen.updateStage("Launching app...");
            app = new FXPotoScreen();
            startScreen.close();

            for (ActionRun ar : StartUiRunRegistry.getAll()) ar.run().run();
        });

        bootstrap.setOnFailed(event -> {
            Throwable e = bootstrap.getException();

            if (e != null)
                e.printStackTrace();

            if (e instanceof EventPostException)
                runProgramKill(ExitCode.REGISTRATION_FAILED);

            else runProgramKill(ExitCode.BOOTSTRAP_FAILED);

        });

        Thread bootstrapThread = new Thread(bootstrap);
        bootstrapThread.setDaemon(true);
        bootstrapThread.start();

    }

    @Override
    public void stop() {
        runExitLogic(ExitCode.SUCCESS);
    }

    /**
     * The main method, that runs PotoFlux.<br>
     * It will first check for args, then enable devEnv if args contains it.<br>
     * We then get version and log it, load all optionalFeatures, set the theme and set the loaded translations.<br>
     * We get the loading bus, register all into it then list and load mods.<br>
     * We post all events to the bus, and invoke the app.
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
     * @return the program's appData folder
     */
    public static Path getProgramDir() {
        Path dir = Paths.get(System.getenv("APPDATA"), "TechnoMastery", "PotoFlux");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    /**
     * Getter for the mod common folder in the appData folder.<br>
     * Mods should use this to resolve their modIds: this will be their own appData folder
     * @return the shared appData folder for mods
     */
    public static Path getModDataDir() {
        Path dir = getProgramDir().resolve("mod-data");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    /**
     * This method should be used to close the app. This allows the app to run extra saving code before exiting.
     * @param exitCode the code given on closing.
     */
    public static void runProgramKill(ExitCode exitCode) {
        // executes when program close
        runExitLogic(exitCode);
        System.exit(exitCode.code()); // close app
    }
    public static void runExitLogic(ExitCode exitCode) {
        if (exitCode.code() == 0) for (ActionRun ar : CloseRunRegistry.getAll()) {
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
     * @return the app version
     */
    public static String getVersion() {
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
     * @param loc the loc to add to the modId
     * @return a built {@link ResourceLocation} with potoflux's modId and the loc given
     */
    public static ResourceLocation fromModId(String loc) {
        return new ResourceLocation(ID, loc);
    }
}
