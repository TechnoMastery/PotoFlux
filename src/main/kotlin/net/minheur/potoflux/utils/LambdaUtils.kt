package net.minheur.potoflux.utils

import java.io.Serializable
import java.lang.invoke.SerializedLambda
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Utility class for lambdas, mainly used in loading mods
 */
object LambdaUtils {

    /**
     * Gets the real method implemented by the reference / lambda.
     * @return the method declared in the impl class (where it exists).
     */
    @JvmStatic
    fun getImplMethod(lambda: Serializable): Method? {
        return try {
            // writeReplace exists on lambdas and returns a SerializedLambda
            val writeReplace = lambda.javaClass.getDeclaredMethod("writeReplace")
            writeReplace.isAccessible = true

            val replaced = writeReplace.invoke(lambda)
            if (replaced !is SerializedLambda) return null

            val implClassName = replaced.implClass.replace('/', '.')
            val implMethodName = replaced.implMethodName
            val implSignature = replaced.implMethodSignature

            val implClass = getImplClass(lambda, implClassName)

            getMethodDeclared(implClass.declaredMethods, implMethodName, implSignature)
                ?: getMethodDeclared(implClass.methods, implMethodName, implSignature)
        } catch (e: ReflectiveOperationException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Gets a declared method
     * @param methodImplClass the methods of the impl class
     * @param implMethodName the name of the method
     * @param implSignature the signature of the JVM
     * @return the method, or `null` if the method is not found
     */
    private fun getMethodDeclared(methodImplClass: Array<Method>, implMethodName: String, implSignature: String): Method? {
        return methodImplClass.find { m ->
            m.name == implMethodName && getJvmSignature(m) == implSignature
        }?.apply { isAccessible = true }
    }

    /**
     * Gets a class to get methods from
     * @param lambda the lambda that should be the class
     * @param implClassName the name of the class
     * @return the found class
     * @throws ClassNotFoundException if the class doesn't exist
     */
    private fun getImplClass(lambda: Serializable, implClassName: String): Class<*> {
        val cl = lambda.javaClass.classLoader ?: Thread.currentThread().contextClassLoader
        return Class.forName(implClassName, false, cl)
    }

    /**
     * Gets the JVM's signature for a method
     * @param m method to get the signature from
     * @return the signature for the method
     */
    private fun getJvmSignature(m: Method): String {
        return buildString {
            append('(')
            m.parameterTypes.forEach { p -> append(getJvmType(p)) }
            append(')')
            append(getJvmType(m.returnType))
        }
    }

    /**
     * Gets the JVM's type for a class
     * @param c class to get the type from
     * @return the type for the class
     */
    private fun getJvmType(c: Class<*>): String = when {
        c.isPrimitive -> when (c) {
            Void.TYPE -> "V"
            Int::class.javaPrimitiveType -> "I"
            Boolean::class.javaPrimitiveType -> "Z"
            Byte::class.javaPrimitiveType -> "B"
            Char::class.javaPrimitiveType -> "C"
            Short::class.javaPrimitiveType -> "S"
            Long::class.javaPrimitiveType -> "J"
            Float::class.javaPrimitiveType -> "F"
            Double::class.javaPrimitiveType -> "D"
            else -> "V"
        }
        c.isArray -> c.name.replace('.', '/')
        else -> "L" + c.name.replace('.', '/') + ";"
    }

    /**
     * Tries to extract caught instance (this) if the reference method is an instance reference.
     * WARNING: this works in most cases (method references non-static),
     * but can fail for certain lambda types (ex : lambdas without captures).
     * @param lambda method to extract from
     * @return caught instance
     */
    @JvmStatic
    fun getCapturingInstance(lambda: Any): Any? {
        return try {
            lambda.javaClass.declaredFields.firstOrNull { f ->
                f.isAccessible = true
                val value = f.get(lambda)
                value != null && value !is Class<*> && value !is String && !value.javaClass.isPrimitive
            }?.let { f ->
                f.isAccessible = true
                f.get(lambda)
            }
        } catch (e: IllegalAccessException) {
            null
        }
    }
}

