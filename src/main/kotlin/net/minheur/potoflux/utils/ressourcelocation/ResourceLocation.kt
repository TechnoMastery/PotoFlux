package net.minheur.potoflux.utils.ressourcelocation

/**
 * The ResourceLocation is used to have a unique identifier from the mod's namespace and the item id
 */
class ResourceLocation {
    companion object {
        private const val SEP: Char = ':'

        @JvmStatic
        private fun decompose(pLocation: String, pSeparator: Char): Array<String> {
            val string = arrayOf("potoflux", pLocation)
            val i = pLocation.indexOf(pSeparator)
            if (i >= 0) {
                string[1] = pLocation.substring(i + 1)
                if (i >= 1) {
                    string[0] = pLocation.substring(0, i)
                }
            }
            return string
        }

        @JvmStatic
        fun isValidPath(pPath: String): Boolean {
            for (ch in pPath) {
                if (!validPathChar(ch)) return false
            }
            return true
        }

        @JvmStatic
        fun isValidNamespace(pNamespace: String): Boolean {
            for (ch in pNamespace) {
                if (!validNamespaceChar(ch)) return false
            }
            return true
        }

        @JvmStatic
        private fun assertValidPath(pNamespace: String, pPath: String): String {
            if (!isValidPath(pPath)) {
                throw ResourceLocationException("Non [a-z0-9/._-] character in path of location: $pNamespace$SEP$pPath")
            }
            return pPath
        }

        @JvmStatic
        private fun assertValidNamespace(pNamespace: String, pPath: String): String {
            if (!isValidNamespace(pNamespace)) {
                throw ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: $pNamespace$SEP$pPath")
            }
            return pNamespace
        }

        @JvmStatic
        fun validPathChar(pPathChar: Char): Boolean {
            return pPathChar == '_' || pPathChar == '-' || (pPathChar in 'a'..'z') || (pPathChar in 'A'..'Z') || (pPathChar in '0'..'9') || pPathChar == '/' || pPathChar == '.'
        }

        @JvmStatic
        fun validNamespaceChar(pNamespaceChar: Char): Boolean {
            return pNamespaceChar == '_' || pNamespaceChar == '-' || (pNamespaceChar in 'a'..'z') || (pNamespaceChar in 'A'..'Z') || (pNamespaceChar in '0'..'9') || pNamespaceChar == '.'
        }
    }

    private val namespace: String
    private val path: String

    constructor(namespace: String, path: String) {
        this.namespace = assertValidNamespace(namespace, path)
        this.path = assertValidPath(namespace, path)
    }

    private constructor(decomposedLoc: Array<String>) : this(decomposedLoc[0], decomposedLoc[1])

    constructor(location: String) : this(decompose(location, SEP))

    fun getPath(): String = path
    fun getNamespace(): String = namespace

    override fun toString(): String = "$namespace$SEP$path"
}
