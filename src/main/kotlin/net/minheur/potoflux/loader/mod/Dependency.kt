package net.minheur.potoflux.loader.mod

/**
 * Represents a dependency with id and minimal/maximal versions
 */
class Dependency {
    @JvmField
    val id: String

    @JvmField
    val minVersion: String

    @JvmField
    val maxVersion: String

    constructor(id: String, version: String) {
        this.id = id
        this.minVersion = version
        this.maxVersion = version
    }

    constructor(formatted: String) {
        val parts = formatted.split(":".toRegex())
        this.id = parts[0]
        this.minVersion = parts[1]
        this.maxVersion = if (parts.size == 2) parts[1] else parts[2]
    }
}
