package net.minheur.potoflux.utils.ressourcelocation

/**
 * Thrown when there's an error in a ResourceLocation
 */
class ResourceLocationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
