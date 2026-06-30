package net.minheur.potoflux.utils.ressourcelocation;

/**
 * The ResourceLocation is used to have a unique identifier from the mod's namespace and the item id
 */
public class ResourceLocation {
    /**
     * The separator used between ID and path
     */
    private static final char SEP = ':';

    /**
     * Decomposes a location into an ID and a path
     * @param pLocation the location
     * @param pSeparator the separator
     * @return the decomposed ID and path in an array
     */
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

    /**
     * Checks if the path is valid.
     * @param pPath the path
     * @return whether the path is valid
     */
    public static boolean isValidPath(String pPath) {
        for (int i = 0; i < pPath.length(); i++) {
            if (!validPathChar(pPath.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Checks if the namespace is valid.
     * @param pNamespace the namespace
     * @return whether the namespace is valid
     */
    public static boolean isValidNamespace(String pNamespace) {
        for (int i = 0; i < pNamespace.length(); i++) {
            if (!validNamespaceChar(pNamespace.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Asserts a valid path.
     * @param pNamespace the namespace
     * @param pPath the path
     * @return the asserted path
     */
    private static String assertValidPath(String pNamespace, String pPath) {
        if (!isValidPath(pPath)) {
            throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + pNamespace + SEP + pPath);
        }
        return pPath;
    }

    /**
     * Asserts a valid namespace.
     * @param pNamespace the namespace
     * @param pPath the path
     * @return the asserted namespace
     */
    private static String assertValidNamespace(String pNamespace, String pPath) {
        if (!isValidNamespace(pNamespace)) {
            throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + pNamespace + SEP + pPath);
        }
        return pNamespace;
    }

    /**
     * Check if a char is valid for the path.
     * @param pPathChar the path char
     * @return weather char is valid
     */
    public static boolean validPathChar(char pPathChar) {
        return pPathChar == '_' || pPathChar == '-' || (pPathChar >= 'a' && pPathChar <= 'z') || (pPathChar >= 'A' && pPathChar <= 'Z') || (pPathChar >= '0' && pPathChar <= '9') || pPathChar == '/' || pPathChar == '.';
    }

    /**
     * Check if a char is valid for the namespace.
     * @param pNamespaceChar the namespace char
     * @return weather char is valid
     */
    public static boolean validNamespaceChar(char pNamespaceChar) {
        return pNamespaceChar == '_' || pNamespaceChar == '-' || (pNamespaceChar >= 'a' && pNamespaceChar <= 'z') || (pNamespaceChar >= 'A' && pNamespaceChar <= 'Z') || (pNamespaceChar >= '0' && pNamespaceChar <= '9') || pNamespaceChar == '.';
    }

    /**
     * The namespace.
     */
    private final String namespace;
    /**
     * The path.
     */
    private final String path;

    /**
     * Constructs a new ResourceLocation.
     */
    public ResourceLocation(String namespace, String path) {
        this.namespace = assertValidNamespace(namespace, path);
        this.path = assertValidPath(namespace, path);
    }

    /**
     * Constructs a new ResourceLocation.
     */
    private ResourceLocation(String[] decomposedLoc) {
        this(decomposedLoc[0], decomposedLoc[1]);
    }

    /**
     * Constructs a new ResourceLocation.
     */
    public ResourceLocation(String location) {
        this(decompose(location, SEP));
    }

    /**
     * Gets the path.
     * @return {@link #path}
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the namespace.
     * @return {@link #namespace}
     */
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String toString() {
        return namespace + SEP + path;
    }
}
