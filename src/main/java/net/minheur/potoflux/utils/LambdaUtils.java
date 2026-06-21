package net.minheur.potoflux.utils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utility class for lambdas, mainly used in loading mods
 */
public final class LambdaUtils {

    private LambdaUtils() {}

    /**
     * Gets the real method implemented by the reference / lambda.
     *
     * @return the method declared in the impl class (where it exists).
     */
    public static Method getImplMethod(Serializable lambda) {
        try {
            // writeReplace exists on lambdas and returns a SerializedLambda
            Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);

            Object replaced = writeReplace.invoke(lambda);
            if (!(replaced instanceof SerializedLambda)) return null;

            SerializedLambda sl = (SerializedLambda) replaced;
            String implClassName = sl.getImplClass().replace('/', '.');
            String implMethodName = sl.getImplMethodName();
            String implSignature = sl.getImplMethodSignature();

            Class<?> implClass = getImplClass(lambda, implClassName);

            Method m = getMethodDeclared(implClass.getDeclaredMethods(), implMethodName, implSignature);
            if (m != null) return m;
            return getMethodDeclared(implClass.getMethods(), implMethodName, implSignature);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a declared method
     *
     * @param methodImplClass the methods of the impl class
     * @param implMethodName  the name of the method
     * @param implSignature   the signature of the JVM
     * @return the method, or `null` if the method is not found
     */
    private static Method getMethodDeclared(
            Method[] methodImplClass,
            String implMethodName,
            String implSignature
    ) {
        for (Method m : methodImplClass) {
            if (m.getName().equals(implMethodName) && getJvmSignature(m).equals(implSignature)) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    /**
     * Gets a class to get methods from
     *
     * @param lambda        the lambda that should be the class
     * @param implClassName the name of the class
     * @return the found class
     * @throws ClassNotFoundException if the class doesn't exist
     */
    private static Class<?> getImplClass(Serializable lambda, String implClassName) throws ClassNotFoundException {
        ClassLoader cl = lambda.getClass().getClassLoader();
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        return Class.forName(implClassName, false, cl);
    }

    /**
     * Gets the JVM's signature for a method
     *
     * @param m method to get the signature from
     * @return the signature for the method
     */
    private static String getJvmSignature(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> p : m.getParameterTypes()) {
            sb.append(getJvmType(p));
        }
        sb.append(')');
        sb.append(getJvmType(m.getReturnType()));
        return sb.toString();
    }

    /**
     * Gets the JVM's type for a class
     *
     * @param c class to get the type from
     * @return the type for the class
     */
    private static String getJvmType(Class<?> c) {
        if (c.isPrimitive()) {
            if (c == Void.TYPE) return "V";
            if (c == Integer.TYPE) return "I";
            if (c == Boolean.TYPE) return "Z";
            if (c == Byte.TYPE) return "B";
            if (c == Character.TYPE) return "C";
            if (c == Short.TYPE) return "S";
            if (c == Long.TYPE) return "J";
            if (c == Float.TYPE) return "F";
            if (c == Double.TYPE) return "D";
            return "V";
        }
        if (c.isArray()) {
            return c.getName().replace('.', '/');
        }
        return "L" + c.getName().replace('.', '/') + ";";
    }

    /**
     * Tries to extract caught instance (this) if the reference method is an instance reference.
     * WARNING: this works in most cases (method references non-static),
     * but can fail for certain lambda types (ex : lambdas without captures).
     *
     * @param lambda method to extract from
     * @return caught instance
     */
    public static Object getCapturingInstance(Object lambda) {
        try {
            for (Field f : lambda.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object value = f.get(lambda);
                if (value != null && !(value instanceof Class<?>) && !(value instanceof String) && !value.getClass().isPrimitive()) {
                    return value;
                }
            }
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
