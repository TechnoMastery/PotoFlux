package net.minheur.potoflux.mod;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class ModdingUtils {
    public static Method getLambdaMethod(Object lambda) {
        try {
            Method write = lambda.getClass().getDeclaredMethod("writeReplace");
            write.setAccessible(true);

            SerializedLambda s = (SerializedLambda) write.invoke(lambda);
            String className = s.getImplClass().replace('/', '.');

            Class<?> implClass = Class.forName(className);

            for (Method m : implClass.getDeclaredMethods()) {
                if (m.getName().equals(s.getImplMethodName())) {
                    return m;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
