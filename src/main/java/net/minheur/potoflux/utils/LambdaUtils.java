package net.minheur.potoflux.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.*;

/**
 * Utility class for lambas, mainly used in loading mods
 */
public class LambdaUtils {

    private LambdaUtils() {}

    /**
     * Gets the real method implemented by the reference / lambda.
     * @return the method declared in the impl class (where it exists).
     */
    public static Method getImplMethod(Serializable lambda) {
        try {
            // writeReplace existe sur les lambdas et retourne un SerializedLambda
            Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);

            Object replaced = writeReplace.invoke(lambda);
            if (!(replaced instanceof SerializedLambda sl))
                return null;

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
     * @param methodImplClass the methods of the impl class
     * @param implMethodName the name of the method
     * @param implSignature the signature of the JVM
     * @return the method, or {@code null} if the method is not found
     */
    @Nullable
    private static Method getMethodDeclared(@Nonnull Method[] methodImplClass, String implMethodName, String implSignature) {
        for (Method m : methodImplClass)
            if (m.getName().equals(implMethodName) && getJvmSignature(m).equals(implSignature)) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }

    /**
     * Gets a class to get methods from
     * @param lambda the lamba that should be the class
     * @param implClassName the name of the class
     * @return the found class
     * @throws ClassNotFoundException if the class doesn't exist
     */
    @Nonnull
    private static Class<?> getImplClass(Serializable lambda, String implClassName) throws ClassNotFoundException {
        ClassLoader cl = lambda.getClass().getClassLoader();
        if (cl == null)
            cl = Thread.currentThread().getContextClassLoader();
        return Class.forName(implClassName, false, cl);
    }

    /**
     * Gets the JVM's signature for a method
     * @param m method to get the signature from
     * @return the signature for the method
     */
    private static String getJvmSignature(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> p : m.getParameterTypes())
            sb.append(getJvmType(p));
        sb.append(')');
        sb.append(getJvmType(m.getReturnType()));
        return sb.toString();
    }

    /**
     * Gets the JVM's type for a class
     * @param c class to get the type from
     * @return the type for the class
     */
    private static String getJvmType(Class<?> c) {
        if (c.isPrimitive()) {
            if (c == void.class) return "V";
            if (c == int.class) return "I";
            if (c == boolean.class) return "Z";
            if (c == byte.class) return "B";
            if (c == char.class) return "C";
            if (c == short.class) return "S";
            if (c == long.class) return "J";
            if (c == float.class) return "F";
            if (c == double.class) return "D";
        }
        if (c.isArray())
            return c.getName().replace('.', '/');
        return "L" + c.getName().replace('.', '/') + ";";
    }

    /**
     * Tries to extract caught instance (this) if the reference method is an instance reference.
     * WARNING: this works in most cases (method references non-static),
     * but can fail for certains lambda types (ex : lambdas without captures).
     * @param lambda method to extract from
     * @return caught instance
     */
    public static Object getCapturingInstance(Object lambda) {
        try {
            // On tente de trouver un champ synthétique capturant 'this' dans l'objet lambda
            for (Field f : lambda.getClass().getDeclaredFields()) {
                // les champs capturés sont non-static et non-final généralement, mais on check le type
                f.setAccessible(true);
                Object value = f.get(lambda);
                // heuristique : si la valeur n'est pas primitive/serializable simple, on la retourne
                if (value != null && !(value instanceof java.lang.Class) && !(value instanceof String)
                        && !(value.getClass().isPrimitive())) {
                    return value;
                }
            }
        } catch (IllegalAccessException e) {
            // ignore — fallback null
        }
        return null;
    }
}

