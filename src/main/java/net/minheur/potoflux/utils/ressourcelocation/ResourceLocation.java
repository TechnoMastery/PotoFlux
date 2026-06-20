package net.minheur.potoflux.utils.ressourcelocation;

/**
 * The ResourceLocation is used to have a unique identifier from the mod's namespace and the item id
 */
public class ResourceLocation {
    private static final char SEP = ':';

    private static String[] decompose(String pLocation, char pSeparator) {
        String[] string = new String[]{"potoflux", pLocation};
        int i = pLocation.indexOf(pSeparator);
        if (i >= 0) {
            string[1] = pLocation.substring(i + 1);
            if (i >= 1) {
                string[0] = pLocation.substring(0, i);
            }
        }
        return string;
    }

    public static boolean isValidPath(String pPath) {
        for (int i = 0; i < pPath.length(); i++) {
            if (!validPathChar(pPath.charAt(i))) return false;
        }
        return true;
    }

    public static boolean isValidNamespace(String pNamespace) {
        for (int i = 0; i < pNamespace.length(); i++) {
            if (!validNamespaceChar(pNamespace.charAt(i))) return false;
        }
        return true;
    }

    private static String assertValidPath(String pNamespace, String pPath) {
        if (!isValidPath(pPath)) {
            throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + pNamespace + SEP + pPath);
        }
        return pPath;
    }

    private static String assertValidNamespace(String pNamespace, String pPath) {
        if (!isValidNamespace(pNamespace)) {
            throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + pNamespace + SEP + pPath);
        }
        return pNamespace;
    }

    public static boolean validPathChar(char pPathChar) {
        return pPathChar == '_' || pPathChar == '-' || (pPathChar >= 'a' && pPathChar <= 'z') || (pPathChar >= 'A' && pPathChar <= 'Z') || (pPathChar >= '0' && pPathChar <= '9') || pPathChar == '/' || pPathChar == '.';
    }

    public static boolean validNamespaceChar(char pNamespaceChar) {
        return pNamespaceChar == '_' || pNamespaceChar == '-' || (pNamespaceChar >= 'a' && pNamespaceChar <= 'z') || (pNamespaceChar >= 'A' && pNamespaceChar <= 'Z') || (pNamespaceChar >= '0' && pNamespaceChar <= '9') || pNamespaceChar == '.';
    }

    private final String namespace;
    private final String path;

    public ResourceLocation(String namespace, String path) {
        this.namespace = assertValidNamespace(namespace, path);
        this.path = assertValidPath(namespace, path);
    }

    private ResourceLocation(String[] decomposedLoc) {
        this(decomposedLoc[0], decomposedLoc[1]);
    }

    public ResourceLocation(String location) {
        this(decompose(location, SEP));
    }

    public String getPath() {
        return path;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public String toString() {
        return namespace + SEP + path;
    }
}
