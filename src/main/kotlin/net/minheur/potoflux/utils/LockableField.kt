package net.minheur.potoflux.utils

/**
 * LockableField holds a value that can be locked to prevent modification
 */
class LockableField<T>(private var data: T, private val locked: Boolean) {

    fun get(): T = data

    fun set(value: T): Boolean {
        if (locked) return false
        data = value
        return true
    }

    fun getIsLocked(): Boolean = locked
}
