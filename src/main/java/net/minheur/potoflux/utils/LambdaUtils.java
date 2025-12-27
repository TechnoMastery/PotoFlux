package net.minheur.potoflux.utils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.*;

public class LambdaUtils {

    private LambdaUtils() {}

    /**
     * Récupère la Method réelle implémentée par la method reference / lambda.
     * Retourne la Method déclarée dans la classe impl (où la méthode existe).
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

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class<?> implClass = Class.forName(implClassName, false, cl);

            for (Method m : implClass.getDeclaredMethods())
                if (m.getName().equals(implMethodName) && getJvmSignature(m).equals(implSignature)) {
                    m.setAccessible(true);
                    return m;
                }

            for (Method m : implClass.getMethods())
                if (m.getName().equals(implMethodName) && getJvmSignature(m).equals(implSignature)) {
                    m.setAccessible(true);
                    return m;
                }

            return null;

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getJvmSignature(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> p : m.getParameterTypes())
            sb.append(getJvmType(p));
        sb.append(')');
        sb.append(getJvmType(m.getReturnType()));
        return sb.toString();
    }

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
     * Essaye d'extraire l'instance capturée (this) si la method reference est une référence d'instance.
     * ATTENTION: ceci fonctionne pour la plupart des cas (method references non statiques),
     * mais peut échouer pour certaines formes de lambda (ex : lambdas sans capture).
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

