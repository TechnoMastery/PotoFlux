package net.minheur.potoflux.loader.mod;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.*;

public final class LambdaUtils {

    private LambdaUtils() {}

    /**
     * Récupère la Method réelle implémentée par la method reference / lambda.
     * Retourne la Method déclarée dans la classe impl (où la méthode existe).
     */
    public static Method getImplMethod(Object lambda) {
        try {
            // writeReplace existe sur les lambdas et retourne un SerializedLambda
            Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            Object replaced = writeReplace.invoke(lambda);
            if (!(replaced instanceof SerializedLambda)) return null;

            SerializedLambda s = (SerializedLambda) replaced;
            String implClassName = s.getImplClass().replace('/', '.');
            String implMethodName = s.getImplMethodName();

            Class<?> implClass = Class.forName(implClassName);

            // trouver la méthode avec le bon nom et signature (on utilise param count 1)
            for (Method m : implClass.getDeclaredMethods()) {
                if (m.getName().equals(implMethodName) && m.getParameterCount() == 1) {
                    m.setAccessible(true);
                    return m;
                }
            }

            // si pas trouvée dans declaredMethods, tenter getMethods()
            for (Method m : implClass.getMethods()) {
                if (m.getName().equals(implMethodName) && m.getParameterCount() == 1) {
                    m.setAccessible(true);
                    return m;
                }
            }

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
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

