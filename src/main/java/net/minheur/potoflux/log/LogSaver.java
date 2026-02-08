package net.minheur.potoflux.log;

import net.minheur.potoflux.PotoFlux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * Class responsible for saving logs.
 */
public class LogSaver {
    /**
     * If this session's logs will be saved
     */
    private static boolean enabled = false;

    /**
     * Linked to {@link System#out}
     */
    private static PrintStream originalOut;
    /**
     * Linked to {@link System#err}
     */
    private static PrintStream originalErr;

    /**
     * Contains all the logs as bytes
     */
    private static ByteArrayOutputStream buffer;

    /**
     * Disables this class instantiating
     */
    private LogSaver() {}

    /**
     * Runs to set up the log saving system
     */
    public static void init() {

        originalOut = System.out;
        originalErr = System.err;

        buffer = new ByteArrayOutputStream(64 * 1024);

        PrintStream tee = new PrintStream(new TeeOutputStream(
                originalOut, buffer
        ), true);

        PrintStream teeErr = new PrintStream(new TeeOutputStream(
                originalErr, buffer
        ), true);

        System.setOut(tee);
        System.setErr(teeErr);
    }

    /**
     * Enables the log saving system
     */
    public static void enable() {
        enabled = true;
    }

    /**
     * Saves the logs to file
     */
    public static void flushAndSave() {
        if (!enabled || buffer == null) return;

        try {
            Path logsDir = PotoFlux.getProgramDir().resolve("logs");
            Files.createDirectories(logsDir);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(
                            "HH-mm-ss-[yyyy]-[MM]-[dd]"
                    ));

            Path lastest = logsDir.resolve("lastest.log");
            Files.write(lastest, buffer.toByteArray());

            Path folder = logsDir.resolve(timestamp);
            Files.createDirectories(folder);

            Path logFile = folder.resolve(timestamp  + ".log");
            Files.write(logFile, buffer.toByteArray());

            zipFolder(folder, folder.resolveSibling(timestamp + ".zip"));

            deleteRecursively(folder);

        } catch (Exception e) {
            originalErr.println("Failed to save logs:");
            e.printStackTrace(originalErr);
        }
    }

    /**
     * Make a zip file from a folder
     * @param sourceDir the initial folder to zip
     * @param zipFile where the zip folder will be
     * @throws IOException if could not read / write on disc
     */
    private static void zipFolder(Path sourceDir, Path zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(
                Files.newOutputStream(zipFile))) {

            Files.walk(sourceDir).forEach(path -> {
                try {
                    if (Files.isDirectory(path))
                        return;

                    ZipEntry entry = new ZipEntry(
                            sourceDir.relativize(path).toString()
                    );
                    zos.putNextEntry(entry);
                    Files.copy(path, zos);
                    zos.closeEntry();

                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    /**
     * Delete a folder and all its content
     * @param path the folder to delete
     * @throws IOException if could not modify the disc
     */
    private static void deleteRecursively(Path path) throws IOException {
        if (!Files.exists(path))
            return;

        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ignored) {}
                });
    }


}
