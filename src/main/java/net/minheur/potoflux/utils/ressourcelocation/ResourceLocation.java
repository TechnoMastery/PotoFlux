package net.minheur.potoflux.utils.ressourcelocation;

/**
 * The ResourceLocation is used to have a unique identifier from the mod's namespace and the item id
 */
public class ResourceLocation {
    /**
     * Separator used between namespace and item ID
     */
    private static final char sep = ':';
    /**
     * The namespace of the mod registering the ResourceLocation
     */
    private final String namespace;
    /**
     * The path (ID) of the item
     */
    private final String path;

    /**
     * Creates a ResourceLocation from a namespace and an ID
     * @param namespace the ID of the mod registering
     * @param path the ID of the item registered
     */
    public ResourceLocation(String namespace, String path) {
        this.namespace = assertValidNamespace(namespace, path);
        this.path = assertValidPath(namespace, path);
    }
    /**
     * Private builder to create from a decomposed loc
     * @param decomposedLoc an array containing the namespace and the ID
     */
    private ResourceLocation(String[] decomposedLoc) {
        this(decomposedLoc[0], decomposedLoc[1]);
    }
    /**
     * Creates a resourceLocation with a unique string, that gets decomposed
     * @param location loc to decompose
     */
    public ResourceLocation(String location) {
        this(decompose(location, sep));
    }

    /**
     * Get the ID of the loc
     * @return the loc's ID
     */
    public String getPath() {
        return this.path;
    }
    /**
     * Get the namespace (modID) of the loc
     * @return the loc's namespace
     */
    public String  getNamespace() {
        return this.namespace;
    }

    /**
     * Merge correctly the loc together
     * @return the full ID
     */
    @Override
    public String toString() {
        return this.namespace + sep + this.path;
    }

    /**
     * Decompose a single string into an array containing the modId and the ID
     * @param pLocation full loc to decompose
     * @param pSeparator char to search in the loc to separate the modId and the ID
     * @return an array containing the namespace and the ID
     */
    protected static String[] decompose(String pLocation, char pSeparator) {
        String[] astring = new String[]{"potoflux", pLocation};
        int i = pLocation.indexOf(pSeparator);
        if (i >= 0) {
            astring[1] = pLocation.substring(i + 1);
            if (i >= 1) {
                astring[0] = pLocation.substring(0, i);
            }
        }

        return astring;
    }
    /**
     * Checks if the resource location's ID is correct
     * @param pNamespace namespace of the loc
     * @param pPath ID if the loc
     * @return the path if valid
     */
    private static String assertValidPath(String pNamespace, String pPath) {
        if (!isValidPath(pPath)) {
            throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + pNamespace + sep + pPath);
        } else {
            return pPath;
        }
    }
    /**
     * Checks if the path is correct
     * @param pPath ID of the loc
     * @return if the ID is correct
     */
    public static boolean isValidPath(String pPath) {
        for(int i = 0; i < pPath.length(); ++i) {
            if (!validPathChar(pPath.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    /**
     * Checks if the namespace is valid
     * @param pNamespace namespace of the loc
     * @return if the namespace is correct
     */
    public static boolean isValidNamespace(String pNamespace) {
        for(int i = 0; i < pNamespace.length(); ++i) {
            if (!validNamespaceChar(pNamespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    /**
     * Checks if the resource location's namespace is correct
     * @param pNamespace namespace of the loc
     * @param pPath ID if the loc
     * @return the path if valid
     */
    private static String assertValidNamespace(String pNamespace, String pPath) {
        if (!isValidNamespace(pNamespace)) {
            throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + pNamespace + sep + pPath);
        } else {
            return pNamespace;
        }
    }
    /**
     * Checks a char of the ID
     * @param pPathChar the char to check
     * @return if char is valid
     */
    public static boolean validPathChar(char pPathChar) {
        return pPathChar == '_' || pPathChar == '-' || pPathChar >= 'a' && pPathChar <= 'z' || pPathChar >= 'A' && pPathChar <= 'Z' || pPathChar >= '0' && pPathChar <= '9' || pPathChar == '/' || pPathChar == '.';
    }
    /**
     * Checks a char of the namespace
     * @param pNamespaceChar the char to check
     * @return if char is valid
     */
    public static boolean validNamespaceChar(char pNamespaceChar) {
        return pNamespaceChar == '_' || pNamespaceChar == '-' || pNamespaceChar >= 'a' && pNamespaceChar <= 'z' || pNamespaceChar >= 'A' && pNamespaceChar <= 'Z' || pNamespaceChar >= '0' && pNamespaceChar <= '9' || pNamespaceChar == '.';
    }
}
