package net.minheur.potoflux.utils

import org.jetbrains.annotations.NotNull
import java.util.function.Supplier

/**
 * The smart supplier is a [Supplier] that doesn't recreate the field each time.
 * Once the value is created from the [supplier] once, it is stored in [value] and given each time [get] is used
 * @param T type of value stored in the supplier
 */
class SmartSupplier<T>(@field:NotNull private val supplier: Supplier<@NotNull T>) : Supplier<T> {

    /**
     * [T] containing the value after it has been created once
     */
    private var value: T? = null

    /**
     * Gets the value. If [value] is `null`, creates it from the [supplier]
     * @return [value]
     */
    override fun get(): T {
        if (value == null) {
            value = supplier.get()
        }
        return value!!
    }
}
