package hzg.wpn.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 26.05.14
 */
public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... args) {
        try {
            //TODO get from hierarchy
            clazz.getDeclaredMethod(methodName, args);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static <T extends Exception> Object invoke(Method method, Object object, Object[] args, Class<T> exceptionToThrow) throws T {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            try {
                throw exceptionToThrow.getConstructor(Throwable.class).newInstance(e);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                throw new RuntimeException(String.format("Can not invoke method[%1$s] on object[%2$s] with args[%3$s]", method.getName(), object.toString(), Arrays.toString(args)), e);
            }
        }

    }
}
