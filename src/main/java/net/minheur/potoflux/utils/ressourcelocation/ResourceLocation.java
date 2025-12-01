package net.minheur.potoflux.utils.ressourcelocation;

public class ResourceLocation {
    private static final char sep = ':';
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
        this(decompose(location, ':'));
    }

    public String getPath() {
        return this.path;
    }
    public String  getNamespace() {
        return this.namespace;
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.path;
    }

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
    private static String assertValidPath(String pNamespace, String pPath) {
        if (!isValidPath(pPath)) {
            throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + pNamespace + ":" + pPath);
        } else {
            return pPath;
        }
    }
    public static boolean isValidPath(String pPath) {
        for(int i = 0; i < pPath.length(); ++i) {
            if (!validPathChar(pPath.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    public static boolean isValidNamespace(String pNamespace) {
        for(int i = 0; i < pNamespace.length(); ++i) {
            if (!validNamespaceChar(pNamespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    private static String assertValidNamespace(String pNamespae, String pPath) {
        if (!isValidNamespace(pNamespae)) {
            throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + pNamespae + ":" + pPath);
        } else {
            return pNamespae;
        }
    }
    public static boolean validPathChar(char pPathChar) {
        return pPathChar == '_' || pPathChar == '-' || pPathChar >= 'a' && pPathChar <= 'z' || pPathChar >= '0' && pPathChar <= '9' || pPathChar == '/' || pPathChar == '.';
    }
    public static boolean validNamespaceChar(char pNamespaceChar) {
        return pNamespaceChar == '_' || pNamespaceChar == '-' || pNamespaceChar >= 'a' && pNamespaceChar <= 'z' || pNamespaceChar >= '0' && pNamespaceChar <= '9' || pNamespaceChar == '.';
    }
}
