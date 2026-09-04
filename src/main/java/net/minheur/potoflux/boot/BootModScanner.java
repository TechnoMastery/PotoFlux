package net.minheur.potoflux.boot;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

final class BootModScanner {
    private final boolean devEnv;
    private final Set<String> availableClasses = new HashSet<>();

    BootModScanner(boolean devEnv) {
        this.devEnv = devEnv;
    }

    ScanResult scan() {
        try {
            List<BootModMetadata> mods = devEnv ? scanDevClasspath() : scanProdMods();
            return new ScanResult(mods, availableClasses);
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan mods", e);
        }
    }

    private List<BootModMetadata> scanProdMods() throws IOException {
        Files.createDirectories(getModsDir());

        List<BootModMetadata> mods = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(getModsDir())) {
            for (Path jar : stream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".jar")).toList()) {
                scanJar(jar, mods);
            }
        }
        return mods;
    }

    private List<BootModMetadata> scanDevClasspath() throws IOException {
        List<BootModMetadata> mods = new ArrayList<>();
        Set<Path> paths = new HashSet<>();

        paths.add(Paths.get(System.getProperty("user.dir"), "build", "classes", "java", "main"));
        for (String path : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (!path.isBlank()) paths.add(Paths.get(path));
        }

        for (Path path : paths) {
            if (Files.isDirectory(path)) scanDirectory(path, mods);
            else if (Files.isRegularFile(path) && path.toString().endsWith(".jar")) scanJar(path, mods);
        }
        return mods;
    }

    static Path getProgramDir() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appDataDir;

        if (os.contains("win"))
            appDataDir = Path.of(System.getenv("APPDATA"));
        else if (os.contains("mac"))
            appDataDir = Path.of(System.getProperty("user.home"), "Library", "Application Support");
        else {
            String xdg = System.getenv("XDG_DATA_HOME");
            if (xdg == null || xdg.isBlank()) xdg = System.getProperty("user.home") + "/.local/share";
            appDataDir = Path.of(xdg);
        }

        return appDataDir.resolve("TechnoMastery").resolve("PotoFlux");
    }

    static Path getModsDir() {
        return getProgramDir().resolve("mods");
    }

    private void scanJar(Path jarPath, List<BootModMetadata> mods) throws IOException {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            for (JarEntry entry : jar.stream().toList()) {
                if (!entry.getName().endsWith(".class")) continue;

                String className = entry.getName().replace('/', '.').replace(".class", "");
                availableClasses.add(className);

                try (InputStream input = jar.getInputStream(entry)) {
                    BootModMetadata metadata = readMetadata(input, className, jarPath);
                    if (metadata != null) mods.add(metadata);
                }
            }
        }
    }

    private void scanDirectory(Path root, List<BootModMetadata> mods) throws IOException {
        if (!Files.exists(root)) return;

        try (Stream<Path> stream = Files.walk(root)) {
            for (Path classFile : stream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".class")).toList()) {
                String className = root.relativize(classFile).toString()
                        .replace('\\', '.')
                        .replace('/', '.')
                        .replace(".class", "");
                availableClasses.add(className);

                try (InputStream input = Files.newInputStream(classFile)) {
                    BootModMetadata metadata = readMetadata(input, className, root);
                    if (metadata != null) mods.add(metadata);
                }
            }
        }
    }

    private BootModMetadata readMetadata(InputStream input, String fallbackClassName, Path sourcePath) throws IOException {
        ModClassVisitor visitor = new ModClassVisitor(fallbackClassName, sourcePath);
        new ClassReader(input).accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        return visitor.metadata;
    }

    record ScanResult(List<BootModMetadata> mods, Set<String> availableClasses) {
    }

    private static final class ModClassVisitor extends ClassVisitor {
        private final String fallbackClassName;
        private final Path sourcePath;
        private BootModMetadata metadata;

        private ModClassVisitor(String fallbackClassName, Path sourcePath) {
            super(Opcodes.ASM9);
            this.fallbackClassName = fallbackClassName;
            this.sourcePath = sourcePath;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            if (name != null) {
                metadataClassName = name.replace('/', '.');
            }
        }

        private String metadataClassName;

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (!BootConstants.MOD_ANNOTATION_DESCRIPTOR.equals(descriptor)) return null;

            metadata = new BootModMetadata();
            metadata.className = metadataClassName == null ? fallbackClassName : metadataClassName;
            metadata.sourcePath = sourcePath.toAbsolutePath().normalize().toString();
            metadata.compatibleVersions.add("-1");
            return new ModAnnotationVisitor(metadata);
        }
    }

    private static final class ModAnnotationVisitor extends AnnotationVisitor {
        private final BootModMetadata metadata;

        private ModAnnotationVisitor(BootModMetadata metadata) {
            super(Opcodes.ASM9);
            this.metadata = metadata;
        }

        @Override
        public void visit(String name, Object value) {
            if ("modId".equals(name)) metadata.modId = (String) value;
            else if ("version".equals(name)) metadata.version = (String) value;
            else if ("compatibleVersionUrl".equals(name)) metadata.compatibleVersionUrl = (String) value;
        }

        @Override
        public AnnotationVisitor visitArray(String name) {
            return new StringArrayVisitor(getTargetList(name), "compatibleVersions".equals(name));
        }

        private List<String> getTargetList(String name) {
            return switch (name) {
                case "compatibleVersions" -> metadata.compatibleVersions;
                case "dependenciesIds" -> metadata.dependenciesIds;
                case "externalDependencies" -> metadata.externalDependencies;
                case "mixinConfigs" -> metadata.mixinConfigs;
                default -> new ArrayList<>();
            };
        }
    }

    private static final class StringArrayVisitor extends AnnotationVisitor {
        private final List<String> target;
        private final boolean replacesDefault;
        private boolean first = true;

        private StringArrayVisitor(List<String> target, boolean replacesDefault) {
            super(Opcodes.ASM9);
            this.target = target;
            this.replacesDefault = replacesDefault;
        }

        @Override
        public void visit(String name, Object value) {
            if (replacesDefault && first) target.clear();
            first = false;
            target.add((String) value);
        }
    }
}
