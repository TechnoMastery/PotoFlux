package net.minheur.potoflux.log;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;

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

public class LogSaver {
    private static boolean enabled = true;

    private static PrintStream originalOut;
    private static PrintStream originalErr;

    private static ByteArrayOutputStream buffer;

    private LogSaver() {}

    public static void init() {
        // if (PotoFluxLoadingContext.isDevEnv()) return;

        enabled = true;

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
