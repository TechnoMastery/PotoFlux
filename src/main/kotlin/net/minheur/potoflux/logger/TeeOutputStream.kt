package net.minheur.potoflux.logger

import java.io.IOException
import java.io.OutputStream

/**
 * Custom output stream that writes to two streams
 */
class TeeOutputStream(private val a: OutputStream, private val b: OutputStream) : OutputStream() {

    @Throws(IOException::class)
    override fun write(i: Int) {
        a.write(i)
        b.write(i)
    }

    @Throws(IOException::class)
    override fun write(bytes: ByteArray, off: Int, len: Int) {
        a.write(bytes, off, len)
        b.write(bytes, off, len)
    }

    @Throws(IOException::class)
    override fun flush() {
        a.flush()
        b.flush()
    }
}
